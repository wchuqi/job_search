//! Performance-oriented models for layout, alignment, niche optimization,
//! iterator equivalence, allocation strategy, bottleneck diagnosis, and release
//! profiles.
//!
//! Test scenarios cover layout summaries, zero-cost iterator equivalence,
//! preallocation, every bottleneck classification branch, and size-sensitive vs
//! throughput-oriented release profiles.

use std::mem::{align_of, size_of};
use std::num::NonZeroUsize;

#[repr(C)]
pub struct WideLayout {
    pub flag: bool,
    pub count: u64,
}

pub fn layout_summary<T>() -> (usize, usize) {
    // size_of 是类型占用字节数；align_of 是内存对齐要求。
    (size_of::<T>(), align_of::<T>())
}

pub fn option_nonzero_is_pointer_sized() -> bool {
    // NonZeroUsize 不可能为 0，因此 Option 可以用 0 表示 None。
    size_of::<Option<NonZeroUsize>>() == size_of::<usize>()
}

pub fn iterator_sum(values: &[u64]) -> u64 {
    // 迭代器写法通常能被优化到接近手写循环。
    values.iter().copied().sum()
}

pub fn loop_sum(values: &[u64]) -> u64 {
    // 手写循环用于和 iterator_sum 对比语义等价。
    let mut total = 0;
    for value in values {
        total += *value;
    }
    total
}

pub fn preallocated_push(count: usize) -> (usize, usize) {
    // 提前分配容量，避免 push 过程中多次扩容。
    let mut values = Vec::with_capacity(count);
    for value in 0..count {
        values.push(value);
    }
    (values.len(), values.capacity())
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum Bottleneck {
    Algorithm,
    Allocation,
    LockContention,
    Io,
    Logging,
}

pub fn diagnose_bottleneck(
    cpu_hot: bool,
    allocations_high: bool,
    lock_wait_high: bool,
    io_wait_high: bool,
) -> Bottleneck {
    // 先看锁等待和 IO 等高影响信号，再看分配和 CPU。
    if lock_wait_high {
        Bottleneck::LockContention
    } else if io_wait_high {
        Bottleneck::Io
    } else if allocations_high {
        Bottleneck::Allocation
    } else if cpu_hot {
        Bottleneck::Algorithm
    } else {
        Bottleneck::Logging
    }
}

#[derive(Debug, Clone, PartialEq, Eq)]
pub struct ReleaseProfile {
    pub lto: bool,
    pub codegen_units: u8,
    pub strip_symbols: bool,
}

pub fn release_profile(size_sensitive: bool) -> ReleaseProfile {
    // 体积敏感时开启 LTO、减少 codegen units、去掉符号。
    if size_sensitive {
        ReleaseProfile {
            lto: true,
            codegen_units: 1,
            strip_symbols: true,
        }
    } else {
        ReleaseProfile {
            lto: false,
            codegen_units: 16,
            strip_symbols: false,
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn covers_layout_niche_preallocation_and_zero_cost_iteration() {
        // u8 的大小和对齐都是 1 字节。
        assert_eq!(layout_summary::<u8>(), (1, 1));
        assert!(layout_summary::<WideLayout>().0 >= 16);
        assert!(option_nonzero_is_pointer_sized());
        assert_eq!(iterator_sum(&[1, 2, 3]), loop_sum(&[1, 2, 3]));
        assert_eq!(preallocated_push(3), (3, 3));
    }

    #[test]
    fn covers_profiling_decision_rules_and_release_profile() {
        // 下面依次覆盖每个瓶颈分类分支。
        assert_eq!(
            diagnose_bottleneck(false, false, true, true),
            Bottleneck::LockContention
        );
        assert_eq!(
            diagnose_bottleneck(false, false, false, true),
            Bottleneck::Io
        );
        assert_eq!(
            diagnose_bottleneck(false, true, false, false),
            Bottleneck::Allocation
        );
        assert_eq!(
            diagnose_bottleneck(true, false, false, false),
            Bottleneck::Algorithm
        );
        assert_eq!(
            diagnose_bottleneck(false, false, false, false),
            Bottleneck::Logging
        );
        assert_eq!(
            release_profile(true),
            ReleaseProfile {
                lto: true,
                codegen_units: 1,
                strip_symbols: true
            }
        );
        assert_eq!(
            release_profile(false),
            ReleaseProfile {
                lto: false,
                codegen_units: 16,
                strip_symbols: false
            }
        );
    }
}
