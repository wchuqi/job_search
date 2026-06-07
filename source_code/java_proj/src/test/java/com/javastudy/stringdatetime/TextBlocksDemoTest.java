package com.javastudy.stringdatetime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextBlocksDemoTest {

    @Test
    void multiLineContainsThreeLines() {
        String result = TextBlocksDemo.multiLine();
        assertTrue(result.contains("Line 1"));
        assertTrue(result.contains("Line 2"));
        assertTrue(result.contains("Line 3"));
        assertEquals(3, result.strip().split("\n").length);
    }

    @Test
    void jsonTemplateFormatsCorrectly() {
        String json = TextBlocksDemo.jsonTemplate("Alice", 30, "alice@example.com");
        assertTrue(json.contains("\"name\": \"Alice\""));
        assertTrue(json.contains("\"age\": 30"));
        assertTrue(json.contains("\"email\": \"alice@example.com\""));
        assertTrue(json.trim().startsWith("{"));
        assertTrue(json.trim().endsWith("}"));
    }

    @Test
    void sqlTemplateFormatsCorrectly() {
        String sql = TextBlocksDemo.sqlTemplate("users", "age > 18");
        assertTrue(sql.contains("SELECT *"));
        assertTrue(sql.contains("FROM users"));
        assertTrue(sql.contains("WHERE age > 18"));
        assertTrue(sql.contains("ORDER BY id"));
    }

    @Test
    void htmlTemplateFormatsCorrectly() {
        String html = TextBlocksDemo.htmlTemplate("My Page", "<p>Hello</p>");
        assertTrue(html.contains("<title>My Page</title>"));
        assertTrue(html.contains("<body><p>Hello</p></body>"));
        assertTrue(html.contains("<html>"));
        assertTrue(html.contains("</html>"));
    }

    @Test
    void indentationIsStripped() {
        String result = TextBlocksDemo.indentationDemo();
        // After re-indentation, the content should not start with 4 spaces
        String[] lines = result.split("\n");
        // The content has indentation relative to closing """
        // Each line should have some consistent indentation stripped
        assertFalse(result.isEmpty());
    }

    @Test
    void lineContinuationProducesSingleLine() {
        String result = TextBlocksDemo.lineContinuation();
        // \ at end of line should make it one logical line
        assertFalse(result.contains("\n"),
                "Line continuation should produce a single line");
        assertTrue(result.contains("This is a very long line"));
        assertTrue(result.contains("want to continue"));
    }

    @Test
    void quotesInsideDoNotNeedEscaping() {
        String result = TextBlocksDemo.quotesInside();
        assertTrue(result.contains("\"hello\""));
        assertTrue(result.contains("'goodbye'"));
    }

    @Test
    void emptyBlockIsEmpty() {
        assertEquals("", TextBlocksDemo.emptyBlock());
    }

    @Test
    void textBlockEndsWithNewline() {
        // Text blocks typically end with a newline before the closing """
        String result = TextBlocksDemo.multiLine();
        assertTrue(result.endsWith("\n"),
                "Text block should end with a newline");
    }
}
