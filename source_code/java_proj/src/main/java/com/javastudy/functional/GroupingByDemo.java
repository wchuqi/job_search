package com.javastudy.functional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 知识点：Collectors.groupingBy 分组与 counting 计数
 *
 * groupingBy(classifier)                  按 classifier 分组 -> Map<K, List<T>>
 * groupingBy(classifier, downstream)      按 classifier 分组 + 下游收集器
 * groupingBy(classifier, mapFactory, ...)  自定义 Map 实现
 * counting()                              统计元素个数
 */
public class GroupingByDemo {

    // 辅助数据类
    public record Student(String name, String grade, int score) {}

    // ── 基本 groupingBy ──────────────────────────────────────────────

    /** 按年级分组 */
    public static Map<String, List<Student>> groupByGrade(List<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(Student::grade));
    }

    /** 按年级分组，只保留姓名 */
    public static Map<String, List<String>> groupNamesByGrade(List<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(
                        Student::grade,
                        Collectors.mapping(Student::name, Collectors.toList())
                ));
    }

    // ── groupingBy + counting ────────────────────────────────────────

    /** 每个年级的人数 */
    public static Map<String, Long> countByGrade(List<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(Student::grade, Collectors.counting()));
    }

    // ── groupingBy + 下游收集器 ──────────────────────────────────────

    /** 每个年级的平均分 */
    public static Map<String, Double> averageScoreByGrade(List<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(
                        Student::grade,
                        Collectors.averagingInt(Student::score)
                ));
    }

    /** 每个年级的最高分 */
    public static Map<String, Optional<Student>> topScoreByGrade(List<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(
                        Student::grade,
                        Collectors.maxBy(Comparator.comparingInt(Student::score))
                ));
    }

    /** 每个年级的总分 */
    public static Map<String, Integer> sumScoreByGrade(List<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(
                        Student::grade,
                        Collectors.summingInt(Student::score)
                ));
    }

    // ── 自定义 Map 类型 ──────────────────────────────────────────────

    /** 使用 TreeMap 保证 key 排序 */
    public static Map<String, List<Student>> groupByGradeSorted(List<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(
                        Student::grade,
                        TreeMap::new,
                        Collectors.toList()
                ));
    }

    // ── 分区（partitioningBy） ────────────────────────────────────────

    /** 按是否及格分区 */
    public static Map<Boolean, List<Student>> partitionByPassing(List<Student> students, int passScore) {
        return students.stream()
                .collect(Collectors.partitioningBy(s -> s.score() >= passScore));
    }
}
