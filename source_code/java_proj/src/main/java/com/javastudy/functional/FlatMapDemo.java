package com.javastudy.functional;

import java.util.Arrays;
import java.util.List;

/**
 * 知识点：map vs flatMap
 *
 * map:   一对一把换，每个元素变成一个新元素     Stream<T> -> Stream<R>
 * flatMap: 一对多 + 展平，每个元素变成一个 Stream，再合并为一个 Stream
 *          Stream<T> -> Stream<R>（内部多个 Stream 合并）
 *
 * 典型场景：按空格拆分句子为单词
 */
public class FlatMapDemo {

    // ── map：一对一转换 ───────────────────────────────────────────────

    /** 每个句子映射为其长度（一对一） */
    public static List<Integer> mapToLength(List<String> sentences) {
        return sentences.stream()
                .map(String::length)
                .toList();
    }

    /** 每个句子映射为拆分后的数组（得到 List<String[]>，不是想要的结果） */
    public static List<String[]> mapToWordArray(List<String> sentences) {
        return sentences.stream()
                .map(s -> s.split("\\s+"))
                .toList();
    }

    // ── flatMap：一对多 + 展平 ────────────────────────────────────────

    /** 每个句子拆分为单词，展平为一个单词流 */
    public static List<String> flatMapToWords(List<String> sentences) {
        return sentences.stream()
                .flatMap(s -> Arrays.stream(s.split("\\s+")))
                .toList();
    }

    /** 与 map + flat 的区别：map 返回嵌套结构，flatMap 展平 */
    public static long countTotalWords(List<String> sentences) {
        return sentences.stream()
                .flatMap(s -> Arrays.stream(s.split("\\s+")))
                .count();
    }

    /** 去重单词 */
    public static List<String> uniqueWords(List<String> sentences) {
        return sentences.stream()
                .flatMap(s -> Arrays.stream(s.split("\\s+")))
                .map(String::toLowerCase)
                .distinct()
                .sorted()
                .toList();
    }

    // ── 更多 flatMap 场景 ─────────────────────────────────────────────

    /** 展平嵌套列表 */
    public static List<Integer> flattenLists(List<List<Integer>> nested) {
        return nested.stream()
                .flatMap(List::stream)
                .toList();
    }

    /** 过滤 + 展平：只保留长度 > 3 的单词 */
    public static List<String> longWords(List<String> sentences) {
        return sentences.stream()
                .flatMap(s -> Arrays.stream(s.split("\\s+")))
                .filter(w -> w.length() > 3)
                .toList();
    }
}
