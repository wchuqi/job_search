package com.javastudy.basics;

/**
 * 知识点：数组（一维和二维）
 * 声明、初始化、访问、length属性
 */
public class ArrayDemo {

    /**
     * 创建并初始化一维数组
     */
    public static int[] createArray(int size) {
        return new int[size]; // 默认初始化为0
    }

    /**
     * 数组字面量初始化
     */
    public static int[] createWithValues(int... values) {
        return values;
    }

    /**
     * 数组长度
     */
    public static int arrayLength(int[] arr) {
        return arr.length;
    }

    /**
     * 数组求和
     */
    public static int sum(int[] arr) {
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        return sum;
    }

    /**
     * 二维数组创建
     */
    public static int[][] create2DArray(int rows, int cols) {
        return new int[rows][cols];
    }

    /**
     * 二维数组求和
     */
    public static int sum2D(int[][] matrix) {
        int sum = 0;
        for (int[] row : matrix) {
            for (int val : row) {
                sum += val;
            }
        }
        return sum;
    }

    /**
     * 数组拷贝
     */
    public static int[] copyArray(int[] source) {
        int[] dest = new int[source.length];
        System.arraycopy(source, 0, dest, 0, source.length);
        return dest;
    }
}
