//! Standard-library concurrency examples for threads, channels, locks, auto
//! traits, atomics, compare-exchange, and lock ordering.
//!
//! Test scenarios cover owned data moved into a thread, message passing,
//! shared mutable state guarded by Mutex/RwLock, Send/Sync compile checks,
//! relaxed atomic counters, compare-exchange success/failure, and deterministic
//! lock ordering to reduce deadlock risk.

use std::sync::atomic::{AtomicUsize, Ordering};
use std::sync::{mpsc, Arc, Mutex, RwLock};
use std::thread;

pub fn thread_sum(values: Vec<i32>) -> i32 {
    // move 闭包把 values 的所有权移动到新线程中。
    thread::spawn(move || values.into_iter().sum::<i32>())
        // join 等待线程结束；unwrap 简化教学示例中的错误处理。
        .join()
        .unwrap()
}

pub fn channel_collect(values: Vec<i32>) -> Vec<i32> {
    // mpsc 表示 multiple producer, single consumer。
    let (tx, rx) = mpsc::channel();
    let handle = thread::spawn(move || {
        for value in values {
            // send 会把值移动到接收端。
            tx.send(value * 2).unwrap();
        }
    });
    handle.join().unwrap();
    rx.iter().collect()
}

pub fn shared_counter(workers: usize, increments: usize) -> usize {
    // Arc 负责多线程共享所有权；Mutex 负责互斥修改。
    let counter = Arc::new(Mutex::new(0usize));
    let mut handles = Vec::new();
    for _ in 0..workers {
        // 每个线程拿到 Arc 的一个克隆句柄。
        let counter = Arc::clone(&counter);
        handles.push(thread::spawn(move || {
            for _ in 0..increments {
                // lock 返回 MutexGuard，离开这一行所在作用域后自动解锁。
                *counter.lock().unwrap() += 1;
            }
        }));
    }
    for handle in handles {
        handle.join().unwrap();
    }
    let final_value = *counter.lock().unwrap();
    final_value
}

pub fn rwlock_update() -> (usize, usize) {
    // RwLock 允许多个读者或一个写者。
    let value = RwLock::new(5usize);
    let first_read = *value.read().unwrap();
    *value.write().unwrap() += 1;
    let second_read = *value.read().unwrap();
    (first_read, second_read)
}

pub fn atomic_increment(times: usize) -> usize {
    // Relaxed 只保证原子读写，不提供额外同步顺序。
    let value = AtomicUsize::new(0);
    for _ in 0..times {
        value.fetch_add(1, Ordering::Relaxed);
    }
    value.load(Ordering::Relaxed)
}

pub fn compare_exchange_once(current: usize, expected: usize, new: usize) -> Result<usize, usize> {
    // compare_exchange 成功返回旧值 Ok，失败返回当前实际值 Err。
    let value = AtomicUsize::new(current);
    value.compare_exchange(expected, new, Ordering::AcqRel, Ordering::Acquire)
}

pub fn assert_send_sync<T: Send + Sync>() -> bool {
    // 如果 T 不满足 Send + Sync，这个函数在编译期就无法被调用。
    true
}

pub fn lock_order(left: u64, right: u64) -> (u64, u64) {
    // 固定加锁顺序是减少死锁风险的常见手段。
    if left <= right {
        (left, right)
    } else {
        (right, left)
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn covers_threads_channels_mutex_and_rwlock() {
        // 这个测试覆盖线程、channel、Mutex 和 RwLock。
        assert_eq!(thread_sum(vec![1, 2, 3]), 6);
        assert_eq!(channel_collect(vec![1, 2, 3]), vec![2, 4, 6]);
        assert_eq!(shared_counter(2, 3), 6);
        assert_eq!(rwlock_update(), (5, 6));
    }

    #[test]
    fn covers_send_sync_atomic_and_lock_ordering() {
        // Arc<Mutex<usize>> 应该满足 Send + Sync，可以跨线程共享。
        assert!(assert_send_sync::<Arc<Mutex<usize>>>());
        assert_eq!(atomic_increment(4), 4);
        assert_eq!(compare_exchange_once(1, 1, 2), Ok(1));
        assert_eq!(compare_exchange_once(1, 0, 2), Err(1));
        assert_eq!(lock_order(1, 2), (1, 2));
        assert_eq!(lock_order(9, 2), (2, 9));
    }
}
