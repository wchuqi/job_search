//! Production-readiness models for redaction, tracing, metrics, graceful
//! shutdown, release gates, incident reviews, and quality gates.
//!
//! Test scenarios cover secret redaction, span fields, empty and non-empty
//! metrics, shutdown states, pass/fail release readiness, complete/incomplete
//! incident review, and required quality gates.

use std::collections::BTreeSet;

pub fn redact_secrets(input: &str) -> String {
    // 按空白切分日志字段，逐个判断是否包含敏感 key。
    input
        .split_whitespace()
        .map(|part| {
            // 只脱敏 token/password，保留其它字段便于排查。
            if part.starts_with("token=") || part.starts_with("password=") {
                let key = part.split('=').next().unwrap();
                format!("{key}=<redacted>")
            } else {
                part.to_string()
            }
        })
        .collect::<Vec<_>>()
        .join(" ")
}

#[derive(Debug, Clone, PartialEq, Eq)]
pub struct TraceSpan {
    pub request_id: String,
    pub route: String,
    pub status: u16,
}

pub fn request_span(
    request_id: impl Into<String>,
    route: impl Into<String>,
    status: u16,
) -> TraceSpan {
    // TraceSpan 模拟 tracing span 中最核心的请求上下文字段。
    TraceSpan {
        request_id: request_id.into(),
        route: route.into(),
        status,
    }
}

#[derive(Debug, Clone, PartialEq)]
pub struct Metrics {
    pub requests: u64,
    pub errors: u64,
    pub latencies_ms: Vec<u64>,
}

impl Metrics {
    pub fn error_rate(&self) -> f64 {
        // 请求数为 0 时不能做除法，直接定义错误率为 0。
        if self.requests == 0 {
            0.0
        } else {
            self.errors as f64 / self.requests as f64
        }
    }

    pub fn max_latency(&self) -> Option<u64> {
        // 没有延迟样本时返回 None。
        self.latencies_ms.iter().copied().max()
    }
}

#[derive(Debug, Clone, PartialEq, Eq)]
pub struct ReleaseChecklist {
    pub fixed_toolchain: bool,
    pub lockfile_committed: bool,
    pub ci_passed: bool,
    pub rollback_plan: bool,
}

pub fn release_ready(checklist: &ReleaseChecklist) -> bool {
    // 发布门禁必须全部满足，一个失败就不应发布。
    checklist.fixed_toolchain
        && checklist.lockfile_committed
        && checklist.ci_passed
        && checklist.rollback_plan
}

#[derive(Debug, Clone, PartialEq, Eq)]
pub struct IncidentReview {
    pub timeline: String,
    pub impact: String,
    pub root_cause: String,
    pub evidence: String,
    pub fix: String,
    pub regression_test: String,
}

pub fn incident_review_complete(review: &IncidentReview) -> bool {
    // 事故复盘要求关键字段都不为空，避免只有结论没有证据。
    !review.timeline.is_empty()
        && !review.impact.is_empty()
        && !review.root_cause.is_empty()
        && !review.evidence.is_empty()
        && !review.fix.is_empty()
        && !review.regression_test.is_empty()
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum ShutdownState {
    Accepting,
    Draining,
    Flushed,
}

pub fn graceful_shutdown_steps(in_flight: usize) -> Vec<ShutdownState> {
    // 优雅关闭先停止接收，再等待在途请求 drain。
    let mut steps = vec![ShutdownState::Accepting, ShutdownState::Draining];
    if in_flight == 0 {
        // 没有在途请求时，可以直接 flush 完成。
        steps.push(ShutdownState::Flushed);
    }
    steps
}

pub fn required_quality_gates() -> BTreeSet<&'static str> {
    // BTreeSet 让门禁名称稳定排序，便于比较和输出。
    ["fmt", "clippy", "test", "audit", "coverage"]
        .into_iter()
        .collect()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn covers_observability_redaction_metrics_and_shutdown() {
        // 验证敏感字段会被脱敏，普通字段保留。
        assert_eq!(
            redact_secrets("user=a token=abc password=secret"),
            "user=a token=<redacted> password=<redacted>"
        );
        assert_eq!(
            request_span("r1", "/tasks", 200),
            TraceSpan {
                request_id: "r1".into(),
                route: "/tasks".into(),
                status: 200
            }
        );
        let empty = Metrics {
            requests: 0,
            errors: 0,
            latencies_ms: vec![],
        };
        assert_eq!(empty.error_rate(), 0.0);
        assert_eq!(empty.max_latency(), None);
        let metrics = Metrics {
            requests: 10,
            errors: 2,
            latencies_ms: vec![10, 30, 20],
        };
        assert_eq!(metrics.error_rate(), 0.2);
        assert_eq!(metrics.max_latency(), Some(30));
        assert_eq!(
            graceful_shutdown_steps(0),
            vec![
                ShutdownState::Accepting,
                ShutdownState::Draining,
                ShutdownState::Flushed
            ]
        );
        assert_eq!(
            graceful_shutdown_steps(2),
            vec![ShutdownState::Accepting, ShutdownState::Draining]
        );
    }

    #[test]
    fn covers_release_governance_incident_review_and_quality_gates() {
        // 所有发布门禁为 true 时才 release_ready。
        assert!(release_ready(&ReleaseChecklist {
            fixed_toolchain: true,
            lockfile_committed: true,
            ci_passed: true,
            rollback_plan: true,
        }));
        assert!(!release_ready(&ReleaseChecklist {
            fixed_toolchain: true,
            lockfile_committed: false,
            ci_passed: true,
            rollback_plan: true,
        }));
        assert!(incident_review_complete(&IncidentReview {
            timeline: "t".into(),
            impact: "i".into(),
            root_cause: "r".into(),
            evidence: "e".into(),
            fix: "f".into(),
            regression_test: "test".into(),
        }));
        assert!(!incident_review_complete(&IncidentReview {
            timeline: String::new(),
            impact: "i".into(),
            root_cause: "r".into(),
            evidence: "e".into(),
            fix: "f".into(),
            regression_test: "test".into(),
        }));
        let gates = required_quality_gates();
        assert!(gates.contains("fmt"));
        assert!(gates.contains("coverage"));
    }
}
