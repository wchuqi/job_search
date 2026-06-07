//! Struct, enum, trait, generic, dispatch, and repository examples.
//!
//! Test scenarios cover state transitions, static dispatch, dynamic dispatch,
//! trait objects, associated types, and blanket implementations.

use std::collections::HashMap;
use std::fmt::Display;

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum TaskStatus {
    // 任务刚创建时的状态。
    Todo,
    // 任务已经开始处理。
    Doing,
    // 任务已经完成。
    Done,
}

#[derive(Debug, Clone, PartialEq, Eq)]
pub struct Task {
    pub id: u64,
    pub title: String,
    pub status: TaskStatus,
}

impl Task {
    pub fn new(id: u64, title: impl Into<String>) -> Self {
        // 构造函数把“如何初始化一个合法 Task”的规则集中在一起。
        Self {
            id,
            // Into<String> 允许 title 传 &str 或 String，这里统一转成 String。
            title: title.into(),
            // 新任务默认是 Todo。
            status: TaskStatus::Todo,
        }
    }

    pub fn start(&mut self) {
        // &mut self 表示这个方法会修改当前 Task。
        self.status = TaskStatus::Doing;
    }

    pub fn complete(&mut self) {
        // 状态迁移通过 enum 赋值完成，编译器保证只能赋合法状态。
        self.status = TaskStatus::Done;
    }

    pub fn is_closed(&self) -> bool {
        // matches! 用模式判断 enum 是否为 Done。
        matches!(self.status, TaskStatus::Done)
    }
}

pub trait Render {
    fn render(&self) -> String;
}

impl Render for Task {
    fn render(&self) -> String {
        // format! 创建一个新的 String，用于展示 Task。
        format!(
            "#{} [{}] {}",
            self.id,
            status_label(self.status),
            self.title
        )
    }
}

pub fn status_label(status: TaskStatus) -> &'static str {
    // match 必须覆盖 TaskStatus 的所有变体。
    match status {
        TaskStatus::Todo => "todo",
        TaskStatus::Doing => "doing",
        TaskStatus::Done => "done",
    }
}

pub fn render_static<T: Render>(value: &T) -> String {
    // 泛型 + trait bound：编译期知道具体类型，属于静态分发。
    value.render()
}

pub fn render_dynamic(value: &dyn Render) -> String {
    // dyn Trait：运行时通过 vtable 调用，属于动态分发。
    value.render()
}

pub trait Repository {
    type Id;
    type Item;

    fn insert(&mut self, item: Self::Item);
    fn get(&self, id: Self::Id) -> Option<&Self::Item>;
}

#[derive(Debug, Default)]
pub struct InMemoryTasks {
    tasks: HashMap<u64, Task>,
}

impl Repository for InMemoryTasks {
    type Id = u64;
    type Item = Task;

    fn insert(&mut self, item: Self::Item) {
        // 使用 Task 的 id 作为 HashMap key。
        self.tasks.insert(item.id, item);
    }

    fn get(&self, id: Self::Id) -> Option<&Self::Item> {
        // get 返回 Option<&Task>，找不到时是 None。
        self.tasks.get(&id)
    }
}

pub trait IntoLabel {
    fn label(&self) -> String;
}

impl<T: Display> IntoLabel for T {
    fn label(&self) -> String {
        // blanket impl：所有实现 Display 的类型都会获得 label 方法。
        format!("label:{self}")
    }
}

pub fn object_safe_batch(items: &[Box<dyn Render>]) -> Vec<String> {
    // Box<dyn Render> 可以把不同具体类型统一当成 Render 使用。
    items.iter().map(|item| item.render()).collect()
}

pub fn choose_dispatch(use_dynamic: bool, task: &Task) -> String {
    // 这里故意保留两个分支，测试可以覆盖静态和动态分发。
    if use_dynamic {
        render_dynamic(task)
    } else {
        render_static(task)
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn models_struct_enum_impl_and_status_state() {
        // 从构造新任务开始验证状态迁移。
        let mut task = Task::new(1, "learn");
        assert_eq!(status_label(TaskStatus::Todo), "todo");
        assert_eq!(status_label(TaskStatus::Doing), "doing");
        assert_eq!(status_label(TaskStatus::Done), "done");
        assert!(!task.is_closed());
        task.start();
        assert_eq!(task.status, TaskStatus::Doing);
        task.complete();
        assert!(task.is_closed());
    }

    #[test]
    fn covers_static_dynamic_dispatch_and_trait_objects() {
        // 同一个 Task 走静态分发和动态分发，结果应该相同。
        let task = Task::new(7, "dispatch");
        assert_eq!(render_static(&task), "#7 [todo] dispatch");
        assert_eq!(render_dynamic(&task), "#7 [todo] dispatch");
        assert_eq!(choose_dispatch(true, &task), "#7 [todo] dispatch");
        assert_eq!(choose_dispatch(false, &task), "#7 [todo] dispatch");
        let items: Vec<Box<dyn Render>> = vec![Box::new(task)];
        assert_eq!(
            object_safe_batch(&items),
            vec!["#7 [todo] dispatch".to_string()]
        );
    }

    #[test]
    fn covers_associated_types_repository_and_blanket_impl() {
        // InMemoryTasks 实现了 Repository，关联类型确定 Id=u64、Item=Task。
        let mut repo = InMemoryTasks::default();
        repo.insert(Task::new(9, "store"));
        assert_eq!(repo.get(9).map(|task| task.title.as_str()), Some("store"));
        assert_eq!(repo.get(10), None);
        assert_eq!(42.label(), "label:42");
    }
}
