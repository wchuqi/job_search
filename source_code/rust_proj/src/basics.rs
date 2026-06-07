//! Basic syntax, expression, control-flow, matching, type, and Unicode examples.
//!
//! Test scenarios cover all grade branches, valid and invalid command parsing,
//! expression return values, shadowing, Unicode byte/char differences, and
//! tuple-vs-struct coordinate readability.

#[derive(Debug, Clone, PartialEq, Eq)]
pub enum Command {
    // `add <name>` 会携带任务名称，所以这个变体保存一个 String。
    Add(String),
    // `remove <id>` 会携带任务 id，u64 表示非负整数。
    Remove(u64),
    // `list` 不需要额外数据，所以变体后面没有字段。
    List,
}

#[derive(Debug, Clone, Copy, PartialEq)]
pub struct PointTuple(pub f64, pub f64);

#[derive(Debug, Clone, Copy, PartialEq)]
pub struct PointStruct {
    pub x: f64,
    pub y: f64,
}

pub fn fahrenheit_to_celsius(f: f64) -> f64 {
    // 函数最后一行没有分号，这个表达式就是返回值。
    (f - 32.0) * 5.0 / 9.0
}

pub fn grade(score: u8) -> Option<char> {
    // Option 表示“可能有值，也可能没有值”；非法分数返回 None。
    match score {
        // `90..=100` 是闭区间，包含 90 和 100。
        90..=100 => Some('A'),
        80..=89 => Some('B'),
        70..=79 => Some('C'),
        60..=69 => Some('D'),
        0..=59 => Some('F'),
        _ => None,
    }
}

pub fn parse_command(input: &str) -> Option<Command> {
    // split_whitespace 按空白切分字符串，返回一个惰性迭代器。
    let mut parts = input.split_whitespace();
    // 这里一次取出前三段，用 match 判断命令是否符合预期形状。
    match (parts.next(), parts.next(), parts.next()) {
        // 第三段必须是 None，表示没有多余参数。
        (Some("add"), Some(name), None) => Some(Command::Add(name.to_string())),
        // parse 返回 Result；ok() 把成功值变 Some，失败变 None。
        (Some("remove"), Some(id), None) => id.parse().ok().map(Command::Remove),
        (Some("list"), None, None) => Some(Command::List),
        // 任何其它输入都解析失败。
        _ => None,
    }
}

pub fn if_expression_label(done: bool) -> &'static str {
    // Rust 的 if 是表达式，所以整个 if 可以直接返回字符串切片。
    if done {
        "done"
    } else {
        "open"
    }
}

pub fn shadow_then_measure(input: &str) -> usize {
    // shadowing：重新绑定同名变量，这里得到去掉首尾空白的 &str。
    let input = input.trim();
    // 再次 shadowing：这里从 &str 创建了新的 String。
    let input = input.to_uppercase();
    // len() 对字符串返回字节数，不是“人眼看到的字符数”。
    input.len()
}

pub fn unicode_facts(input: &str) -> (usize, usize, Vec<char>) {
    // 返回：UTF-8 字节数、char 数量、逐个 char 收集成的 Vec。
    (input.len(), input.chars().count(), input.chars().collect())
}

impl PointTuple {
    pub fn manhattan(self) -> f64 {
        // tuple struct 用位置访问字段：.0 是第一个字段，.1 是第二个字段。
        self.0.abs() + self.1.abs()
    }
}

impl PointStruct {
    pub fn manhattan(self) -> f64 {
        // 具名字段更容易看懂：x 和 y 的含义直接写在字段名里。
        self.x.abs() + self.y.abs()
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn converts_temperature_and_grades_all_ranges() {
        // 断言 32 华氏度转换为 0 摄氏度。
        assert_eq!(fahrenheit_to_celsius(32.0), 0.0);
        // 下面这些断言覆盖 grade 函数的每个 match 分支。
        assert_eq!(grade(95), Some('A'));
        assert_eq!(grade(85), Some('B'));
        assert_eq!(grade(75), Some('C'));
        assert_eq!(grade(65), Some('D'));
        assert_eq!(grade(10), Some('F'));
        assert_eq!(grade(101), None);
    }

    #[test]
    fn parses_exhaustive_commands() {
        // 合法 add 命令应解析成 Command::Add。
        assert_eq!(parse_command("add rust"), Some(Command::Add("rust".into())));
        // 合法 remove 命令应把字符串 id 解析成数字。
        assert_eq!(parse_command("remove 42"), Some(Command::Remove(42)));
        // list 命令没有参数。
        assert_eq!(parse_command("list"), Some(Command::List));
        // 非数字 id 解析失败。
        assert_eq!(parse_command("remove nope"), None);
        // 多余参数会让整体命令解析失败。
        assert_eq!(parse_command("add too many words"), None);
    }

    #[test]
    fn demonstrates_expressions_shadowing_unicode_and_coordinates() {
        // if 表达式两个分支都返回 &'static str。
        assert_eq!(if_expression_label(true), "done");
        assert_eq!(if_expression_label(false), "open");
        assert_eq!(shadow_then_measure(" rust "), 4);
        assert_eq!(unicode_facts("中a"), (4, 2, vec!['中', 'a']));
        assert_eq!(PointTuple(-3.0, 4.0).manhattan(), 7.0);
        assert_eq!(PointStruct { x: -3.0, y: 4.0 }.manhattan(), 7.0);
    }
}
