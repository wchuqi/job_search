//! Memory-model examples for Box, recursive types, Rc, Arc, RefCell, Cow,
//! layout, alignment, and niche optimization.
//!
//! Test scenarios cover recursive list traversal, reference-count changes,
//! interior mutability, borrowed-vs-owned Cow behavior, and pointer-sized
//! Option<NonZeroUsize>.

use std::borrow::Cow;
use std::cell::RefCell;
use std::mem::{align_of, size_of};
use std::num::NonZeroUsize;
use std::rc::Rc;
use std::sync::Arc;

#[derive(Debug, PartialEq, Eq)]
pub enum List {
    Cons(i32, Box<List>),
    Nil,
}

impl List {
    pub fn sum(&self) -> i32 {
        // 递归 enum 必须用 Box 间接存储，否则类型大小无限。
        match self {
            List::Cons(value, next) => value + next.sum(),
            List::Nil => 0,
        }
    }
}

#[derive(Debug, Default)]
pub struct Ledger {
    entries: RefCell<Vec<i32>>,
}

impl Ledger {
    pub fn push(&self, value: i32) {
        // RefCell 允许在只有 &self 的情况下进行运行时可变借用。
        self.entries.borrow_mut().push(value);
    }

    pub fn total(&self) -> i32 {
        // borrow() 得到不可变借用，iter().sum() 只读数据。
        self.entries.borrow().iter().sum()
    }
}

pub fn rc_counts() -> (usize, usize) {
    // Rc 用于单线程共享所有权。
    let value = Rc::new(1);
    let before = Rc::strong_count(&value);
    // clone Rc 只增加引用计数，不复制里面的 i32。
    let cloned = Rc::clone(&value);
    let after = Rc::strong_count(&value);
    drop(cloned);
    (before, after)
}

pub fn arc_counts() -> (usize, usize) {
    // Arc 是线程安全引用计数，适合跨线程共享。
    let value = Arc::new(1);
    let before = Arc::strong_count(&value);
    let cloned = Arc::clone(&value);
    let after = Arc::strong_count(&value);
    drop(cloned);
    (before, after)
}

pub fn cow_trim(input: &str) -> Cow<'_, str> {
    // Cow 可以在“不需要修改”时借用，在“需要修改”时拥有新数据。
    let trimmed = input.trim();
    if trimmed.len() == input.len() {
        // 没有空白需要去掉，直接借用原字符串。
        Cow::Borrowed(input)
    } else {
        // 去掉空白后内容变了，所以创建新的 String。
        Cow::Owned(trimmed.to_string())
    }
}

pub fn niche_optimization_sizes() -> (usize, usize) {
    // Option<NonZeroUsize> 可以利用 0 这个无效值编码 None。
    (size_of::<usize>(), size_of::<Option<NonZeroUsize>>())
}

pub fn layout_of_i64() -> (usize, usize) {
    // 返回 i64 的大小和对齐，帮助理解内存布局。
    (size_of::<i64>(), align_of::<i64>())
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn covers_box_recursive_list_rc_arc_and_refcell() {
        // Box 让 List 可以递归嵌套。
        let list = List::Cons(1, Box::new(List::Cons(2, Box::new(List::Nil))));
        assert_eq!(list.sum(), 3);
        assert_eq!(rc_counts(), (1, 2));
        assert_eq!(arc_counts(), (1, 2));
        let ledger = Ledger::default();
        ledger.push(4);
        ledger.push(6);
        assert_eq!(ledger.total(), 10);
    }

    #[test]
    fn covers_cow_layout_and_niche_optimization() {
        // 没有变化时 Cow 应该是 Borrowed；需要 trim 时应该是 Owned。
        assert!(matches!(cow_trim("rust"), Cow::Borrowed("rust")));
        assert!(matches!(cow_trim(" rust "), Cow::Owned(value) if value == "rust"));
        let (usize_size, option_nonzero_size) = niche_optimization_sizes();
        assert_eq!(usize_size, option_nonzero_size);
        assert_eq!(layout_of_i64(), (8, 8));
    }
}
