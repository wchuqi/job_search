package com.javastudy.functional;

import org.junit.jupiter.api.Test;
import java.util.IntSummaryStatistics;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MapToIntDemoTest {

    @Test
    void sumLengthsComputesTotalLength() {
        assertEquals(8, MapToIntDemo.sumLengths(List.of("ab", "cd", "efgh")));
    }

    @Test
    void sumLengthsHandlesEmptyList() {
        assertEquals(0, MapToIntDemo.sumLengths(List.of()));
    }

    @Test
    void sumIntegersComputesSum() {
        assertEquals(10, MapToIntDemo.sumIntegers(List.of(1, 2, 3, 4)));
    }

    @Test
    void maxOfFindsMaximum() {
        assertEquals(5, MapToIntDemo.maxOf(List.of(1, 5, 3, 2)));
    }

    @Test
    void maxOfEmptyReturnsZero() {
        assertEquals(0, MapToIntDemo.maxOf(List.of()));
    }

    @Test
    void minOfFindsMinimum() {
        assertEquals(1, MapToIntDemo.minOf(List.of(5, 1, 3, 2)));
    }

    @Test
    void minOfEmptyReturnsZero() {
        assertEquals(0, MapToIntDemo.minOf(List.of()));
    }

    @Test
    void averageOfComputesAverage() {
        assertEquals(2.5, MapToIntDemo.averageOf(List.of(1, 2, 3, 4)), 0.01);
    }

    @Test
    void averageOfEmptyReturnsZero() {
        assertEquals(0.0, MapToIntDemo.averageOf(List.of()));
    }

    @Test
    void statisticsReturnsCorrectSummary() {
        IntSummaryStatistics stats = MapToIntDemo.statistics(List.of(1, 2, 3, 4, 5));
        assertEquals(1, stats.getMin());
        assertEquals(5, stats.getMax());
        assertEquals(15, stats.getSum());
        assertEquals(5, stats.getCount());
        assertEquals(3.0, stats.getAverage(), 0.01);
    }

    @Test
    void sumRangeComputes1ToN() {
        assertEquals(15, MapToIntDemo.sumRange(5));
        assertEquals(1, MapToIntDemo.sumRange(1));
        assertEquals(0, MapToIntDemo.sumRange(0));
    }

    @Test
    void evenNumbersReturnsEvenInRange() {
        var result = MapToIntDemo.evenNumbers(1, 10);
        assertEquals(List.of(2, 4, 6, 8), result);
    }

    @Test
    void sumPositiveIgnoresNegatives() {
        assertEquals(6, MapToIntDemo.sumPositive(List.of(-1, 2, -3, 4)));
    }

    @Test
    void sumPositiveHandlesAllNegative() {
        assertEquals(0, MapToIntDemo.sumPositive(List.of(-1, -2, -3)));
    }

    @Test
    void sumLengthsAboveFiltersByThreshold() {
        assertEquals(7, MapToIntDemo.sumLengthsAbove(List.of("ab", "cde", "fghi"), 2));
    }
}
