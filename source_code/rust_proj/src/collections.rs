//! Collection, string, iterator, and closure examples.
//!
//! Test scenarios cover HashMap counting, String normalization, safe Unicode
//! character access, iterator ownership modes, mutable iteration, and closure
//! capture through `move`.

use std::collections::HashMap;

pub fn word_count(input: &str) -> HashMap<String, usize> {
    // 创建可变 HashMap，用来保存 单词 -> 次数。
    let mut counts = HashMap::new();
    // split_whitespace 遍历单词，不会保留空白。
    for word in input.split_whitespace() {
        // entry 可以同时处理“已有 key”和“首次出现 key”两种情况。
        *counts.entry(word.to_lowercase()).or_insert(0) += 1;
    }
    counts
}

pub fn normalize_title(input: &str) -> String {
    // trim 去掉首尾空白；replace 把换行替换成普通空格。
    input.trim().replace('\n', " ")
}

pub fn sum_even_squares(values: &[i32]) -> i32 {
    // 下面是一条迭代器流水线：借用 -> 复制值 -> 过滤 -> 映射 -> 求和。
    values
        .iter()
        // copied 把 &i32 变成 i32，因为 i32 是 Copy。
        .copied()
        // 只保留偶数。
        .filter(|value| value % 2 == 0)
        // 把偶数变成平方值。
        .map(|value| value * value)
        .sum()
}

pub fn make_adder(base: i32) -> impl Fn(i32) -> i32 {
    // move 让闭包捕获 base 的值；返回的闭包以后还能使用它。
    move |value| base + value
}

pub fn consume_iter(values: Vec<String>) -> usize {
    // into_iter 会拿走 Vec 中每个 String 的所有权。
    values.into_iter().map(|value| value.len()).sum()
}

pub fn borrow_iter(values: &[String]) -> Vec<&str> {
    // iter 只借用 String，再把每个 String 转成 &str。
    values.iter().map(|value| value.as_str()).collect()
}

pub fn mutate_iter(values: &mut [String]) {
    // iter_mut 逐个拿到 &mut String，因此可以原地修改。
    values
        .iter_mut()
        .for_each(|value| value.make_ascii_uppercase());
}

pub fn safe_char_at(input: &str, index: usize) -> Option<char> {
    // Rust 字符串不能直接按字节下标取字符；chars().nth 更安全。
    input.chars().nth(index)
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn covers_vec_hashmap_string_and_unicode_access() {
        // word_count 会把 Rust 和 rust 统一为小写 key。
        let counts = word_count("Rust rust safe");
        assert_eq!(counts["rust"], 2);
        assert_eq!(counts["safe"], 1);
        assert_eq!(normalize_title(" hello\nrust "), "hello rust");
        assert_eq!(safe_char_at("中a", 0), Some('中'));
        assert_eq!(safe_char_at("中a", 2), None);
    }

    #[test]
    fn covers_iterator_ownership_and_closure_capture() {
        // 偶数是 2 和 4，平方和是 4 + 16。
        assert_eq!(sum_even_squares(&[1, 2, 3, 4]), 20);
        let add_ten = make_adder(10);
        assert_eq!(add_ten(5), 15);

        let mut values = vec!["a".to_string(), "bb".to_string()];
        assert_eq!(borrow_iter(&values), vec!["a", "bb"]);
        mutate_iter(&mut values);
        assert_eq!(values, vec!["A".to_string(), "BB".to_string()]);
        assert_eq!(consume_iter(values), 3);
    }
}
