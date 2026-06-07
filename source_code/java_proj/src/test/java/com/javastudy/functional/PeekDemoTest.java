package com.javastudy.functional;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PeekDemoTest {

    @Test
    void peekToLogRecordsBeforeAndAfter() {
        var log = new ArrayList<String>();
        var result = PeekDemo.peekToLog(List.of("a", "b"), log);
        assertEquals(List.of("A", "B"), result);
        assertEquals(4, log.size());
        assertEquals("before: a", log.get(0));
        assertEquals("after: A", log.get(1));
        assertEquals("before: b", log.get(2));
        assertEquals("after: B", log.get(3));
    }

    @Test
    void peekFilterRecordsInputAndPassed() {
        var log = new ArrayList<String>();
        var result = PeekDemo.peekFilter(List.of(-1, 1, -2, 2), log);
        assertEquals(List.of(1, 2), result);
        assertTrue(log.contains("input: -1"));
        assertTrue(log.contains("input: 1"));
        assertTrue(log.contains("passed: 1"));
        assertTrue(log.contains("passed: 2"));
    }

    @Test
    void peekWithoutTerminalDoesNotExecute() {
        assertEquals(0, PeekDemo.peekWithoutTerminal(List.of("a", "b", "c")));
    }

    @Test
    void peekWithTerminalExecutesForEachElement() {
        assertEquals(3, PeekDemo.peekWithTerminal(List.of("a", "b", "c")));
    }

    @Test
    void peekWithTerminalEmptyList() {
        assertEquals(0, PeekDemo.peekWithTerminal(List.of()));
    }

    @Test
    void debugChainRecordsAllStages() {
        var debugLog = new ArrayList<String>();
        var result = PeekDemo.debugChain(List.of("  HELLO ", "", "  world  "), debugLog);
        assertEquals(List.of("hello", "world"), result);
        // 验证日志包含各阶段
        assertTrue(debugLog.stream().anyMatch(s -> s.contains("0-原始")));
        assertTrue(debugLog.stream().anyMatch(s -> s.contains("1-过滤空串")));
        assertTrue(debugLog.stream().anyMatch(s -> s.contains("2-trim")));
        assertTrue(debugLog.stream().anyMatch(s -> s.contains("3-小写")));
    }

    @Test
    void countAtEachStageRecordsCounts() {
        var counts = PeekDemo.countAtEachStage(List.of("a", "bb", "ccc", "dddd"));
        assertEquals(4, counts[0]); // 原始: 4 个
        assertEquals(2, counts[1]); // 长度 > 2: "ccc", "dddd"
        assertEquals(2, counts[2]); // map 后: 2 个
    }

    @Test
    void countAtEachStageAllFilteredOut() {
        var counts = PeekDemo.countAtEachStage(List.of("a", "b"));
        assertEquals(2, counts[0]); // 原始: 2 个
        assertEquals(0, counts[1]); // 长度 > 2: 0 个
        assertEquals(0, counts[2]); // map 后: 0 个
    }
}
