package com.javastudy.basics;

import java.util.List;

/**
 * 知识点：控制流语句
 * if/else if/else, for循环, 增强for循环, while循环
 */
public class ControlFlowDemo {

    /**
     * if / else if / else 条件分支
     */
    public static String classifyScore(int score) {
        if (score >= 90) {
            return "A";
        } else if (score >= 80) {
            return "B";
        } else if (score >= 70) {
            return "C";
        } else if (score >= 60) {
            return "D";
        } else {
            return "F";
        }
    }

    /**
     * 标准 for 循环
     */
    public static int sumWithForLoop(int n) {
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i;
        }
        return sum;
    }

    /**
     * 增强 for 循环 (for-each)
     */
    public static int sumWithForEach(List<Integer> numbers) {
        int sum = 0;
        for (int num : numbers) {
            sum += num;
        }
        return sum;
    }

    /**
     * while 循环
     */
    public static int sumWithWhile(int n) {
        int sum = 0;
        int i = 1;
        while (i <= n) {
            sum += i;
            i++;
        }
        return sum;
    }

    /**
     * do-while 循环
     */
    public static int sumWithDoWhile(int n) {
        int sum = 0;
        int i = 1;
        do {
            sum += i;
            i++;
        } while (i <= n);
        return sum;
    }

    /**
     * break 和 continue
     */
    public static int sumUntilNegative(int[] numbers) {
        int sum = 0;
        for (int num : numbers) {
            if (num < 0) break;    // 遇到负数停止
            if (num % 2 != 0) continue; // 跳过奇数
            sum += num;
        }
        return sum;
    }
}
