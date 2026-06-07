//! Application-boundary examples for CLI, route matching, configuration,
//! retries, and health checks.
//!
//! Test scenarios cover command dispatch, route parsing, configuration
//! precedence, idempotent retry policy, and dependency health classification.

use std::collections::BTreeMap;

#[derive(Debug, Clone, PartialEq, Eq)]
pub enum Route {
    Health,
    ListTasks,
    GetTask(u64),
    NotFound,
}

pub fn match_route(method: &str, path: &str) -> Route {
    // 同时匹配 HTTP method 和 path，模拟最小路由器。
    match (method, path) {
        ("GET", "/health") => Route::Health,
        ("GET", "/tasks") => Route::ListTasks,
        ("GET", path) if path.starts_with("/tasks/") => path
            // 从 `/tasks/7` 中取出 `7`。
            .trim_start_matches("/tasks/")
            // 尝试把字符串 id 解析成 u64。
            .parse()
            // 解析成功时包装成 Route::GetTask。
            .map(Route::GetTask)
            // 解析失败时返回 NotFound。
            .unwrap_or(Route::NotFound),
        _ => Route::NotFound,
    }
}

pub fn word_count_cli(args: &[&str], input: &str) -> Result<usize, &'static str> {
    // args 模拟命令行参数；这里只有 word-count 一个合法命令。
    if args == ["word-count"] {
        // lines().count() 统计输入文本行数。
        Ok(input.lines().count())
    } else {
        Err("unsupported command")
    }
}

#[derive(Debug, Clone, PartialEq, Eq)]
pub struct Config {
    pub host: String,
    pub port: u16,
}

pub fn load_config(file: &BTreeMap<String, String>, env: &BTreeMap<String, String>) -> Config {
    // 配置优先级：环境变量 > 文件配置 > 默认值。
    let host = env
        .get("APP_HOST")
        .or_else(|| file.get("host"))
        .cloned()
        .unwrap_or_else(|| "127.0.0.1".to_string());
    let port = env
        .get("APP_PORT")
        .or_else(|| file.get("port"))
        .and_then(|value| value.parse().ok())
        .unwrap_or(8080);
    Config { host, port }
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub struct RetryPolicy {
    pub max_attempts: u8,
    pub base_delay_ms: u64,
    pub idempotent: bool,
}

pub fn retry_delays(policy: RetryPolicy) -> Vec<u64> {
    // 非幂等操作不自动重试，避免重复写入。
    if !policy.idempotent {
        Vec::new()
    } else {
        // 幂等操作使用指数退避：base, base*2, base*4...
        (0..policy.max_attempts)
            .map(|attempt| policy.base_delay_ms * 2u64.pow(attempt as u32))
            .collect()
    }
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum Health {
    Ready,
    Degraded,
    Down,
}

pub fn health_from_dependencies(db_ok: bool, queue_depth: usize, queue_limit: usize) -> Health {
    // DB 不可用是硬失败，直接 Down。
    if !db_ok {
        Health::Down
    // 队列超过上限表示服务还能跑，但已经退化。
    } else if queue_depth > queue_limit {
        Health::Degraded
    } else {
        Health::Ready
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn covers_cli_route_matching_and_health() {
        // 验证路由表的命中、参数解析和未命中分支。
        assert_eq!(match_route("GET", "/health"), Route::Health);
        assert_eq!(match_route("GET", "/tasks"), Route::ListTasks);
        assert_eq!(match_route("GET", "/tasks/7"), Route::GetTask(7));
        assert_eq!(match_route("GET", "/tasks/x"), Route::NotFound);
        assert_eq!(match_route("POST", "/tasks"), Route::NotFound);
        assert_eq!(word_count_cli(&["word-count"], "a\nb"), Ok(2));
        assert_eq!(
            word_count_cli(&["unknown"], "a"),
            Err("unsupported command")
        );
        assert_eq!(health_from_dependencies(true, 1, 2), Health::Ready);
        assert_eq!(health_from_dependencies(true, 3, 2), Health::Degraded);
        assert_eq!(health_from_dependencies(false, 0, 2), Health::Down);
    }

    #[test]
    fn covers_config_precedence_and_retry_governance() {
        // file 模拟配置文件，env 模拟环境变量。
        let mut file = BTreeMap::new();
        file.insert("host".to_string(), "file-host".to_string());
        file.insert("port".to_string(), "9000".to_string());
        let env = BTreeMap::new();
        assert_eq!(
            load_config(&file, &env),
            Config {
                host: "file-host".into(),
                port: 9000
            }
        );

        let mut env = BTreeMap::new();
        env.insert("APP_HOST".to_string(), "env-host".to_string());
        env.insert("APP_PORT".to_string(), "7000".to_string());
        assert_eq!(
            load_config(&file, &env),
            Config {
                host: "env-host".into(),
                port: 7000
            }
        );
        assert_eq!(
            load_config(&BTreeMap::new(), &BTreeMap::new()),
            Config {
                host: "127.0.0.1".into(),
                port: 8080
            }
        );

        assert_eq!(
            retry_delays(RetryPolicy {
                max_attempts: 3,
                base_delay_ms: 10,
                idempotent: true
            }),
            vec![10, 20, 40]
        );
        assert_eq!(
            retry_delays(RetryPolicy {
                max_attempts: 3,
                base_delay_ms: 10,
                idempotent: false
            }),
            Vec::<u64>::new()
        );
    }
}
