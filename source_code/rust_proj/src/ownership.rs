//! Ownership, move, Copy, Clone, borrow, mutable borrow, lifetime, and Drop
//! examples.
//!
//! Test scenarios cover move-and-return, Copy duplication, explicit clone,
//! borrowed-vs-owned parameter intent, mutable access, lifetime-constrained
//! return values, and reverse drop order.

use std::cell::RefCell;
use std::rc::Rc;

#[derive(Debug, Clone, PartialEq, Eq)]
pub struct OwnedName(pub String);

pub fn move_string_then_return(value: String) -> (String, usize) {
    // value 是 String，进入函数时所有权已经从调用方移动到这里。
    let len = value.len();
    // 把 value 放进返回值中，所有权再移动回调用方。
    (value, len)
}

pub fn copy_number(value: i32) -> (i32, i32) {
    // i32 实现 Copy，赋值会复制值，原变量仍然可用。
    let copied = value;
    (value, copied)
}

pub fn clone_name(value: &OwnedName) -> OwnedName {
    // value 是借用，不能直接拿走；clone 明确创建一份新数据。
    value.clone()
}

pub fn append_suffix(value: &mut String, suffix: &str) {
    // &mut String 表示调用方允许我们原地修改这个 String。
    value.push_str(suffix);
}

pub fn longest<'a>(left: &'a str, right: &'a str) -> &'a str {
    // 生命周期 `'a` 表示返回引用必须来自 left/right 的共同有效范围。
    if left.len() >= right.len() {
        left
    } else {
        right
    }
}

pub fn parameter_strategy_borrowed(value: &str) -> usize {
    // 只需要读取时用 &str，调用方不需要交出所有权。
    value.len()
}

pub fn parameter_strategy_owned<T: Into<String>>(value: T) -> String {
    // 需要持有数据时转成 String；Into<String> 让 API 更灵活。
    value.into()
}

pub struct DropProbe {
    name: &'static str,
    log: Rc<RefCell<Vec<&'static str>>>,
}

impl DropProbe {
    pub fn new(name: &'static str, log: Rc<RefCell<Vec<&'static str>>>) -> Self {
        // Rc 让多个 DropProbe 共享同一个日志；RefCell 允许运行时可变借用。
        Self { name, log }
    }
}

impl Drop for DropProbe {
    fn drop(&mut self) {
        // Drop 自动在值离开作用域时执行，这里记录“谁先被释放”。
        self.log.borrow_mut().push(self.name);
    }
}

pub fn drop_order_demo() -> Vec<&'static str> {
    // 日志需要被两个 DropProbe 共享，所以放进 Rc<RefCell<_>>。
    let log = Rc::new(RefCell::new(Vec::new()));
    {
        // 同一作用域中的局部变量会按创建顺序的反方向释放。
        let _first = DropProbe::new("first", Rc::clone(&log));
        let _second = DropProbe::new("second", Rc::clone(&log));
    }
    // 先保存到局部变量，让 RefCell 的 borrow guard 在返回前结束。
    let dropped = log.borrow().clone();
    dropped
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn covers_move_copy_clone_and_parameter_intent() {
        // move_string_then_return 会拿走 String 后再归还。
        assert_eq!(
            move_string_then_return("rust".to_string()),
            ("rust".to_string(), 4)
        );
        assert_eq!(copy_number(7), (7, 7));
        assert_eq!(
            clone_name(&OwnedName("borrowed".into())),
            OwnedName("borrowed".into())
        );
        assert_eq!(parameter_strategy_borrowed("slice"), 5);
        assert_eq!(parameter_strategy_owned("owned"), "owned".to_string());
    }

    #[test]
    fn covers_mut_borrow_lifetime_and_drop() {
        // 这里创建可变 String，后面传给 append_suffix 做原地修改。
        let mut name = "Rust".to_string();
        append_suffix(&mut name, "acean");
        assert_eq!(name, "Rustacean");
        assert_eq!(longest("short", "longer"), "longer");
        assert_eq!(longest("same", "tiny"), "same");
        assert_eq!(drop_order_demo(), vec!["second", "first"]);
    }
}
