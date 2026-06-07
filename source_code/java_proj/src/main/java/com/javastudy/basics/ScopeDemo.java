package com.javastudy.basics;

/**
 * 知识点：变量作用域和生命周期
 * 局部变量在代码块内声明，块外不可见
 */
public class ScopeDemo {

    /**
     * for循环变量作用域：i 只在循环内可见
     */
    public static int countTo(int n) {
        int result = 0;
        for (int i = 1; i <= n; i++) {
            result += i;
            // i 在此作用域内可见
        }
        // i 在此处不可见
        return result;
    }

    /**
     * 块作用域：变量在if块内声明
     */
    public static String blockScopeExample(boolean condition) {
        if (condition) {
            String message = "inside if";
            return message;
        }
        // message 在此处不可见
        return "outside";
    }

    /**
     * 局部变量遮蔽 (shadowing)：局部变量遮蔽字段
     */
    private int value = 100;

    public int shadowExample(int value) {
        // 参数 value 遮蔽了字段 value
        return value; // 返回参数值，不是字段值
    }

    public int getFieldValue() {
        return this.value; // 使用 this 访问被遮蔽的字段
    }
}
