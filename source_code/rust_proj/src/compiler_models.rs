//! Executable models for borrow-checker concepts that are usually explained
//! through MIR, NLL, move paths, reborrowing, temporary lifetimes, and drop check.
//!
//! Test scenarios cover partial move, reborrow, NLL-style later mutation,
//! temporary lifetime behavior, explicit borrow decisions, and borrowed data
//! surviving through a wrapper type.

#[derive(Debug, Clone, PartialEq, Eq)]
pub struct Person {
    pub name: String,
    pub age: u8,
}

pub fn partial_move_keeps_copy_field(person: Person) -> (String, u8) {
    // name 是 String，赋值会 move 出来。
    let name = person.name;
    // age 是 u8，u8 实现 Copy，所以复制后仍然安全。
    let age = person.age;
    (name, age)
}

pub fn reborrow_increment(value: &mut i32) -> i32 {
    // 先通过可变引用读取当前值。
    let shared_before = *value;
    {
        // reborrow：从已有的 &mut i32 再临时借出一个 &mut i32。
        let mutable = &mut *value;
        // 修改结束后，mutable 在这个小作用域末尾失效。
        *mutable += 1;
    }
    // mutable 已失效，所以这里可以再次读取 value。
    shared_before + *value
}

pub fn nll_allows_later_mutation() -> String {
    // NLL 会根据实际最后使用位置缩短不可变借用范围。
    let mut value = String::from("read");
    let len = value.len();
    // len 之后不再借用 value，所以这里允许可变修改。
    value.push_str("-write");
    format!("{len}:{value}")
}

pub fn longest_elided(left: &str, right: &str) -> String {
    // 返回 String 而不是 &str，可以避免把返回引用生命周期绑到参数上。
    if left.len() > right.len() {
        left.to_string()
    } else {
        right.to_string()
    }
}

pub fn temporary_lifetime_value() -> usize {
    // 临时 String 只在这一行表达式求值期间有效；这里只读取长度，不返回引用。
    String::from("temporary").as_str().len()
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum BorrowDecision {
    Allow,
    RejectAliasingMutable,
    RejectMutationWhileShared,
}

pub fn borrow_decision(
    shared_borrows: usize,
    mutable_borrow: bool,
    wants_mutation: bool,
) -> BorrowDecision {
    // 已有可变借用时，不能同时存在共享借用。
    if mutable_borrow && shared_borrows > 0 {
        BorrowDecision::RejectAliasingMutable
    // 有共享借用时，不能执行需要可变访问的操作。
    } else if wants_mutation && shared_borrows > 0 {
        BorrowDecision::RejectMutationWhileShared
    } else {
        BorrowDecision::Allow
    }
}

pub struct DropCheck<'a> {
    pub borrowed: &'a str,
}

impl<'a> DropCheck<'a> {
    pub fn describe(&self) -> String {
        // self 只借用外部 str，不拥有它。
        format!("borrowed:{}", self.borrowed)
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn covers_move_paths_reborrow_nll_and_temporary_lifetime() {
        // 这些断言把抽象的 MIR/NLL 概念转成可运行小例子。
        let person = Person {
            name: "Ferris".into(),
            age: 8,
        };
        assert_eq!(partial_move_keeps_copy_field(person), ("Ferris".into(), 8));
        let mut value = 10;
        assert_eq!(reborrow_increment(&mut value), 21);
        assert_eq!(value, 11);
        assert_eq!(nll_allows_later_mutation(), "4:read-write");
        assert_eq!(temporary_lifetime_value(), 9);
    }

    #[test]
    fn covers_lifetime_constraints_drop_check_and_decision_rules() {
        // 这些断言覆盖生命周期关系、drop check 和借用规则判断。
        assert_eq!(longest_elided("a", "bb"), "bb".to_string());
        let owned = String::from("safe");
        let checked = DropCheck {
            borrowed: owned.as_str(),
        };
        assert_eq!(checked.describe(), "borrowed:safe");
        assert_eq!(borrow_decision(0, false, true), BorrowDecision::Allow);
        assert_eq!(
            borrow_decision(1, true, false),
            BorrowDecision::RejectAliasingMutable
        );
        assert_eq!(
            borrow_decision(1, false, true),
            BorrowDecision::RejectMutationWhileShared
        );
    }
}
