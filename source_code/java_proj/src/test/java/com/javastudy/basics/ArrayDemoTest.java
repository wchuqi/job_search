package com.javastudy.basics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArrayDemoTest {

    @Test
    void testCreateArray() {
        int[] arr = ArrayDemo.createArray(5);
        assertEquals(5, arr.length);
        assertEquals(0, arr[0]); // 默认值
    }

    @Test
    void testCreateWithValues() {
        int[] arr = ArrayDemo.createWithValues(1, 2, 3);
        assertArrayEquals(new int[]{1, 2, 3}, arr);
    }

    @Test
    void testArrayLength() {
        assertEquals(3, ArrayDemo.arrayLength(new int[]{1, 2, 3}));
        assertEquals(0, ArrayDemo.arrayLength(new int[]{}));
    }

    @Test
    void testSum() {
        assertEquals(15, ArrayDemo.sum(new int[]{1, 2, 3, 4, 5}));
        assertEquals(0, ArrayDemo.sum(new int[]{}));
    }

    @Test
    void testCreate2DArray() {
        int[][] matrix = ArrayDemo.create2DArray(3, 4);
        assertEquals(3, matrix.length);
        assertEquals(4, matrix[0].length);
    }

    @Test
    void testSum2D() {
        int[][] matrix = {{1, 2}, {3, 4}};
        assertEquals(10, ArrayDemo.sum2D(matrix));
    }

    @Test
    void testCopyArray() {
        int[] source = {1, 2, 3};
        int[] copy = ArrayDemo.copyArray(source);
        assertArrayEquals(source, copy);
        assertNotSame(source, copy); // 不是同一个对象
    }
}
