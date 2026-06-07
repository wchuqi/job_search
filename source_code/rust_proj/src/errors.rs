//! Option, Result, domain error, display, problem-details, and retryability
//! examples.
//!
//! Test scenarios cover successful and failed parsing, empty input validation,
//! missing records, every domain error variant, HTTP-style status mapping, and
//! retryable external failures.

use std::error::Error;
use std::fmt::{Display, Formatter};

#[derive(Debug, Clone, PartialEq, Eq)]
pub enum AppError {
    // id 字符串解析失败。
    InvalidId,
    // 标题为空或全是空白。
    EmptyTitle,
    // 找不到指定 id 的任务，把 id 带在错误里方便排查。
    NotFound(u64),
    // 业务冲突，例如版本不一致。
    Conflict(&'static str),
    // 外部服务失败，同时记录是否可以重试。
    External {
        service: &'static str,
        retryable: bool,
    },
}

pub type AppResult<T> = Result<T, AppError>;

impl Display for AppError {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        // Display 决定错误展示给人看的文本。
        match self {
            AppError::InvalidId => write!(f, "invalid id"),
            AppError::EmptyTitle => write!(f, "empty title"),
            AppError::NotFound(id) => write!(f, "task {id} not found"),
            AppError::Conflict(reason) => write!(f, "conflict: {reason}"),
            AppError::External { service, retryable } => {
                write!(
                    f,
                    "external service {service} failed, retryable={retryable}"
                )
            }
        }
    }
}

impl Error for AppError {}

#[derive(Debug, Clone, PartialEq, Eq)]
pub struct ProblemDetails {
    pub status: u16,
    pub title: String,
    pub retryable: bool,
}

impl From<&AppError> for ProblemDetails {
    fn from(value: &AppError) -> Self {
        // 把领域错误映射成类似 HTTP Problem Details 的结构。
        match value {
            AppError::InvalidId | AppError::EmptyTitle => Self {
                // 输入错误通常是 400。
                status: 400,
                title: value.to_string(),
                retryable: false,
            },
            AppError::NotFound(_) => Self {
                // 资源不存在通常是 404。
                status: 404,
                title: value.to_string(),
                retryable: false,
            },
            AppError::Conflict(_) => Self {
                // 状态或版本冲突通常是 409。
                status: 409,
                title: value.to_string(),
                retryable: false,
            },
            AppError::External { retryable, .. } => Self {
                // 外部依赖不可用通常映射为 503。
                status: 503,
                title: value.to_string(),
                retryable: *retryable,
            },
        }
    }
}

pub fn parse_task_id(input: &str) -> AppResult<u64> {
    // parse 失败时，把标准库错误转换成自己的 AppError。
    input.parse::<u64>().map_err(|_| AppError::InvalidId)
}

pub fn require_title(input: &str) -> AppResult<&str> {
    // trim 后再检查，避免 `"   "` 被当成合法标题。
    let title = input.trim();
    if title.is_empty() {
        Err(AppError::EmptyTitle)
    } else {
        Ok(title)
    }
}

pub fn find_title(id: u64, rows: &[(u64, String)]) -> AppResult<&str> {
    // iter() 表示只借用 rows，不拿走 Vec 或 String 的所有权。
    rows.iter()
        // 找到第一个 id 相等的元素。
        .find(|(row_id, _)| *row_id == id)
        // 找到后只返回 title 的 &str 视图。
        .map(|(_, title)| title.as_str())
        // 如果没找到，把 None 转成 NotFound 错误。
        .ok_or(AppError::NotFound(id))
}

pub fn checked_divide(left: i32, right: i32) -> Option<i32> {
    // 除数为 0 时不 panic，而是用 None 表示没有合法结果。
    if right == 0 {
        None
    } else {
        Some(left / right)
    }
}

pub fn retryable(error: &AppError) -> bool {
    // 复用 ProblemDetails 的映射结果，避免重复写重试判断逻辑。
    ProblemDetails::from(error).retryable
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn covers_option_result_and_error_boundaries() {
        // checked_divide 用 Option 表达“可能没有结果”。
        assert_eq!(checked_divide(8, 2), Some(4));
        assert_eq!(checked_divide(8, 0), None);
        assert_eq!(parse_task_id("12"), Ok(12));
        assert_eq!(parse_task_id("abc"), Err(AppError::InvalidId));
        assert_eq!(require_title(" rust "), Ok("rust"));
        assert_eq!(require_title("   "), Err(AppError::EmptyTitle));
    }

    #[test]
    fn maps_domain_errors_to_problem_details() {
        // rows 模拟数据库查询结果。
        let rows = vec![(1, "one".to_string())];
        assert_eq!(find_title(1, &rows), Ok("one"));
        assert_eq!(find_title(2, &rows), Err(AppError::NotFound(2)));

        let cases = [
            (AppError::InvalidId, 400, false),
            (AppError::EmptyTitle, 400, false),
            (AppError::NotFound(9), 404, false),
            (AppError::Conflict("version"), 409, false),
            (
                AppError::External {
                    service: "db",
                    retryable: true,
                },
                503,
                true,
            ),
            (
                AppError::External {
                    service: "cache",
                    retryable: false,
                },
                503,
                false,
            ),
        ];
        for (error, status, retryable) in cases {
            // 每个错误变体都要能映射成稳定的状态码和重试标记。
            let problem = ProblemDetails::from(&error);
            assert_eq!(problem.status, status);
            assert_eq!(problem.retryable, retryable);
            assert_eq!(retryable, super::retryable(&error));
            assert!(!problem.title.is_empty());
        }
    }
}
