//! Unsafe and FFI boundary examples.
//!
//! Test scenarios cover raw pointer reads, the safe wrapper around unsafe code,
//! `repr(C)` layout intent, `extern "C"` functions, MaybeUninit initialization,
//! panic containment, and an unsafe boundary audit checklist.

use std::mem::MaybeUninit;
use std::panic::{catch_unwind, AssertUnwindSafe};
use std::slice;

/// Sums a raw pointer range.
///
/// # Safety
///
/// When `len > 0`, `ptr` must be non-null, properly aligned, and valid for
/// reading `len` contiguous `i32` values. The memory must not be mutated while
/// this function reads it.
pub unsafe fn raw_slice_sum(ptr: *const i32, len: usize) -> i32 {
    // len 为 0 时不读取 ptr，因此允许传入空指针。
    if len == 0 {
        0
    } else {
        // from_raw_parts 是 unsafe：调用者必须保证指针和长度有效。
        slice::from_raw_parts(ptr, len).iter().sum()
    }
}

pub fn sum_slice_via_raw(values: &[i32]) -> i32 {
    // 安全 API 内部调用 unsafe，并由切片保证指针和长度合法。
    unsafe { raw_slice_sum(values.as_ptr(), values.len()) }
}

#[repr(C)]
#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub struct FfiPoint {
    pub x: i32,
    pub y: i32,
}

pub extern "C" fn ffi_add(left: i32, right: i32) -> i32 {
    // extern "C" 使用 C ABI，便于跨语言调用。
    left + right
}

pub fn call_ffi_add(left: i32, right: i32) -> i32 {
    // 对外暴露普通安全 Rust 函数，隐藏 ABI 细节。
    ffi_add(left, right)
}

pub fn init_array_3(values: [i32; 3]) -> [i32; 3] {
    // MaybeUninit 表示“这块内存可能还没初始化”。
    let mut out: [MaybeUninit<i32>; 3] = [MaybeUninit::uninit(); 3];
    for (index, value) in values.into_iter().enumerate() {
        // write 初始化指定位置，不会先读取旧值。
        out[index].write(value);
    }
    // The loop above initializes every slot exactly once before conversion.
    unsafe { std::mem::transmute_copy::<[MaybeUninit<i32>; 3], [i32; 3]>(&out) }
}

pub fn catch_panic_boundary<F>(f: F) -> Result<(), &'static str>
where
    F: FnOnce(),
{
    // FFI 边界不应让 panic 穿透，这里用 catch_unwind 捕获。
    catch_unwind(AssertUnwindSafe(f)).map_err(|_| "panic caught at boundary")
}

#[derive(Debug, Clone, PartialEq, Eq)]
pub struct UnsafeAudit {
    pub has_safety_doc: bool,
    pub validates_pointer: bool,
    pub defines_ownership: bool,
    pub panic_crosses_ffi: bool,
}

pub fn audit_unsafe_boundary(audit: &UnsafeAudit) -> bool {
    // unsafe 封装要说明 Safety、校验指针、定义所有权，并阻止 panic 跨 FFI。
    audit.has_safety_doc
        && audit.validates_pointer
        && audit.defines_ownership
        && !audit.panic_crosses_ffi
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn covers_raw_pointers_repr_c_ffi_and_maybe_uninit() {
        // 空切片场景验证 len=0 时不会读取空指针。
        assert_eq!(unsafe { raw_slice_sum(std::ptr::null(), 0) }, 0);
        assert_eq!(sum_slice_via_raw(&[1, 2, 3]), 6);
        assert_eq!(FfiPoint { x: 1, y: 2 }, FfiPoint { x: 1, y: 2 });
        assert_eq!(call_ffi_add(2, 5), 7);
        assert_eq!(init_array_3([1, 2, 3]), [1, 2, 3]);
    }

    #[test]
    fn covers_panic_boundary_and_unsafe_audit_rules() {
        // 正常闭包不会产生边界错误。
        assert_eq!(catch_panic_boundary(|| {}), Ok(()));
        assert_eq!(
            catch_panic_boundary(|| panic!("boom")),
            Err("panic caught at boundary")
        );
        assert!(audit_unsafe_boundary(&UnsafeAudit {
            has_safety_doc: true,
            validates_pointer: true,
            defines_ownership: true,
            panic_crosses_ffi: false,
        }));
        assert!(!audit_unsafe_boundary(&UnsafeAudit {
            has_safety_doc: false,
            validates_pointer: true,
            defines_ownership: true,
            panic_crosses_ffi: false,
        }));
    }
}
