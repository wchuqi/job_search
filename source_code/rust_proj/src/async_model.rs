//! Dependency-free async models for Future, Poll, Waker, Pin, cancellation
//! safety, and backpressure.
//!
//! Test scenarios cover an async function, a manually implemented Future,
//! pinned self-referential prevention shape, dropped pending work, committed
//! work, and bounded queue pressure.

use std::future::Future;
use std::marker::PhantomPinned;
use std::pin::Pin;
use std::sync::{Arc, Mutex};
use std::task::{Context, Poll, RawWaker, RawWakerVTable, Waker};

pub async fn async_add(left: i32, right: i32) -> i32 {
    // async fn 返回 Future；真正执行发生在 Future 被 poll 时。
    left + right
}

pub fn block_on<F: Future>(future: F) -> F::Output {
    // 创建一个最小 waker，用于驱动教学用 Future。
    let waker = noop_waker();
    let mut context = Context::from_waker(&waker);
    // Box::pin 把 Future 固定在堆上，得到 Pin<Box<F>>。
    let mut future = Box::pin(future);
    loop {
        // poll 一次 Future；Ready 表示完成，Pending 表示稍后再试。
        match Future::poll(Pin::as_mut(&mut future), &mut context) {
            Poll::Ready(value) => return value,
            Poll::Pending => std::thread::yield_now(),
        }
    }
}

fn noop_waker() -> Waker {
    // This waker is enough for deterministic tests where the future wakes
    // itself and the executor immediately polls again.
    unsafe fn clone(_: *const ()) -> RawWaker {
        RawWaker::new(std::ptr::null(), &VTABLE)
    }
    unsafe fn wake(_: *const ()) {}
    unsafe fn wake_by_ref(_: *const ()) {}
    unsafe fn drop(_: *const ()) {}
    static VTABLE: RawWakerVTable = RawWakerVTable::new(clone, wake, wake_by_ref, drop);
    unsafe { Waker::from_raw(RawWaker::new(std::ptr::null(), &VTABLE)) }
}

pub struct ReadyAfterPolls {
    remaining: usize,
    output: i32,
}

impl ReadyAfterPolls {
    pub fn new(remaining: usize, output: i32) -> Self {
        // remaining 表示还要返回多少次 Pending 才 Ready。
        Self { remaining, output }
    }
}

impl Future for ReadyAfterPolls {
    type Output = i32;

    fn poll(mut self: Pin<&mut Self>, cx: &mut Context<'_>) -> Poll<Self::Output> {
        // remaining 为 0 时 Future 完成。
        if self.remaining == 0 {
            Poll::Ready(self.output)
        } else {
            // 否则减少计数，并通知 executor 可以再次 poll。
            self.remaining -= 1;
            cx.waker().wake_by_ref();
            Poll::Pending
        }
    }
}

pub struct PinnedRecord {
    value: String,
    _pin: PhantomPinned,
}

impl PinnedRecord {
    pub fn new(value: impl Into<String>) -> Pin<Box<Self>> {
        // PhantomPinned 让这个类型默认 !Unpin，必须被 Pin 保护。
        Box::pin(Self {
            value: value.into(),
            _pin: PhantomPinned,
        })
    }

    pub fn value(self: Pin<&Self>) -> &str {
        // 通过 Pin<&Self> 读取数据，不移动整个结构体。
        &self.get_ref().value
    }
}

#[derive(Debug, Default)]
pub struct CancelSafeCounter {
    committed: Arc<Mutex<i32>>,
}

impl CancelSafeCounter {
    pub fn new(value: i32) -> Self {
        // committed 是真正生效的值，放在 Arc<Mutex<_>> 中便于共享。
        Self {
            committed: Arc::new(Mutex::new(value)),
        }
    }

    pub fn begin_add(&self, delta: i32) -> PendingAdd {
        // begin_add 只创建待提交操作，不立刻修改 committed。
        PendingAdd {
            committed: Arc::clone(&self.committed),
            delta,
            committed_once: false,
        }
    }

    pub fn value(&self) -> i32 {
        // 读取当前已提交的值。
        *self.committed.lock().unwrap()
    }
}

pub struct PendingAdd {
    committed: Arc<Mutex<i32>>,
    delta: i32,
    committed_once: bool,
}

impl PendingAdd {
    pub fn commit(mut self) {
        // commit 消耗 self，防止同一个 PendingAdd 被提交多次。
        if !self.committed_once {
            *self.committed.lock().unwrap() += self.delta;
            self.committed_once = true;
        }
    }
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum QueueError {
    Full,
}

#[derive(Debug, Default)]
pub struct BoundedQueue<T> {
    capacity: usize,
    values: Vec<T>,
}

impl<T> BoundedQueue<T> {
    pub fn new(capacity: usize) -> Self {
        // capacity 是队列容量上限，用来表达背压。
        Self {
            capacity,
            values: Vec::new(),
        }
    }

    pub fn push(&mut self, value: T) -> Result<(), QueueError> {
        // 队列满了就返回错误，而不是无限增长内存。
        if self.values.len() == self.capacity {
            Err(QueueError::Full)
        } else {
            self.values.push(value);
            Ok(())
        }
    }

    pub fn pop(&mut self) -> Option<T> {
        // 空队列没有元素可取，返回 None。
        if self.values.is_empty() {
            None
        } else {
            Some(self.values.remove(0))
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn covers_async_fn_future_poll_contract_and_executor() {
        // block_on 会持续 poll Future，直到拿到 Ready。
        assert_eq!(block_on(async_add(2, 3)), 5);
        assert_eq!(block_on(ReadyAfterPolls::new(2, 9)), 9);
    }

    #[test]
    fn covers_pin_cancel_safety_and_backpressure() {
        // PinnedRecord 展示 Pin 保护下读取数据的形状。
        let record = PinnedRecord::new("fixed");
        assert_eq!(record.as_ref().value(), "fixed");

        let counter = CancelSafeCounter::new(10);
        let pending = counter.begin_add(5);
        drop(pending);
        assert_eq!(counter.value(), 10);
        counter.begin_add(7).commit();
        assert_eq!(counter.value(), 17);

        let mut queue = BoundedQueue::new(1);
        assert_eq!(queue.pop(), None);
        assert_eq!(queue.push("first"), Ok(()));
        assert_eq!(queue.push("second"), Err(QueueError::Full));
        assert_eq!(queue.pop(), Some("first"));
    }
}
