package com.javastudy.functional;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class GroupingByDemoTest {

    private static List<GroupingByDemo.Student> sampleStudents() {
        return List.of(
                new GroupingByDemo.Student("Alice", "A", 90),
                new GroupingByDemo.Student("Bob", "A", 85),
                new GroupingByDemo.Student("Charlie", "B", 78),
                new GroupingByDemo.Student("Diana", "B", 92),
                new GroupingByDemo.Student("Eve", "A", 88)
        );
    }

    @Test
    void groupByGradeReturnsCorrectGroups() {
        var groups = GroupingByDemo.groupByGrade(sampleStudents());
        assertEquals(2, groups.size());
        assertEquals(3, groups.get("A").size());
        assertEquals(2, groups.get("B").size());
    }

    @Test
    void groupNamesByGradeReturnsOnlyNames() {
        var groups = GroupingByDemo.groupNamesByGrade(sampleStudents());
        assertTrue(groups.get("A").contains("Alice"));
        assertTrue(groups.get("A").contains("Bob"));
        assertTrue(groups.get("A").contains("Eve"));
        assertTrue(groups.get("B").contains("Charlie"));
        assertTrue(groups.get("B").contains("Diana"));
    }

    @Test
    void countByGradeReturnsCounts() {
        var counts = GroupingByDemo.countByGrade(sampleStudents());
        assertEquals(3L, counts.get("A"));
        assertEquals(2L, counts.get("B"));
    }

    @Test
    void averageScoreByGradeComputesAverages() {
        var averages = GroupingByDemo.averageScoreByGrade(sampleStudents());
        assertEquals((90.0 + 85.0 + 88.0) / 3, averages.get("A"), 0.01);
        assertEquals((78.0 + 92.0) / 2, averages.get("B"), 0.01);
    }

    @Test
    void topScoreByGradeFindsMax() {
        var tops = GroupingByDemo.topScoreByGrade(sampleStudents());
        assertEquals("Alice", tops.get("A").orElseThrow().name());
        assertEquals("Diana", tops.get("B").orElseThrow().name());
    }

    @Test
    void sumScoreByGradeComputesSums() {
        var sums = GroupingByDemo.sumScoreByGrade(sampleStudents());
        assertEquals(90 + 85 + 88, sums.get("A"));
        assertEquals(78 + 92, sums.get("B"));
    }

    @Test
    void groupByGradeSortedUsesTreeMap() {
        var groups = GroupingByDemo.groupByGradeSorted(sampleStudents());
        var keys = new java.util.ArrayList<>(groups.keySet());
        assertEquals(List.of("A", "B"), keys); // TreeMap 保证排序
    }

    @Test
    void partitionByPassingSplitsCorrectly() {
        var partitions = GroupingByDemo.partitionByPassing(sampleStudents(), 85);
        assertEquals(4, partitions.get(true).size());  // 90, 85, 92, 88
        assertEquals(1, partitions.get(false).size()); // 78
    }
}
