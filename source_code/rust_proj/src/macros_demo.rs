//! Declarative macro and derive macro examples.
//!
//! Test scenarios cover macro expression expansion, repetition, trailing-comma
//! handling, Debug/Clone/PartialEq/Eq derives, and calling exported macros from
//! crate scope.

#[macro_export]
macro_rules! task_title {
    ($name:expr) => {
        // 宏展开后会生成一段 format! 调用代码。
        format!("task: {}", $name)
    };
}

#[macro_export]
macro_rules! string_vec {
    ($($value:expr),* $(,)?) => {
        // `$()*` 表示重复匹配；`$(,)?` 表示允许结尾多一个逗号。
        vec![$($value.to_string()),*]
    };
}

#[derive(Debug, Clone, PartialEq, Eq)]
pub struct DerivedTask {
    pub id: u64,
    pub title: String,
}

pub fn derive_traits_demo(task: &DerivedTask) -> DerivedTask {
    // Clone derive 让结构体可以从借用复制出一个拥有所有权的新值。
    task.clone()
}

pub fn macro_generated_title(input: &str) -> String {
    // 使用 crate:: 前缀调用 #[macro_export] 导出的宏。
    crate::task_title!(input)
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn covers_macro_rules_repetition_and_derive_macros() {
        // 验证 task_title! 宏展开后能生成预期字符串。
        assert_eq!(crate::task_title!("learn"), "task: learn");
        assert_eq!(
            crate::string_vec!("a", "b"),
            vec!["a".to_string(), "b".to_string()]
        );
        let task = DerivedTask {
            id: 1,
            title: "macro".into(),
        };
        assert_eq!(derive_traits_demo(&task), task);
        assert_eq!(
            format!("{task:?}"),
            "DerivedTask { id: 1, title: \"macro\" }"
        );
        assert_eq!(macro_generated_title("expand"), "task: expand");
    }
}
