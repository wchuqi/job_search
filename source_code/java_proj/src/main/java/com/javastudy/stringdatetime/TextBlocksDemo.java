package com.javastudy.stringdatetime;

/**
 * Demonstrates text blocks (multi-line strings) introduced in Java 15.
 * <p>
 * Key points:
 * <ul>
 *   <li>Triple-quote """ opens a text block; closing """ determines indentation</li>
 *   <li>Leading whitespace common to all lines is stripped (re-indentation)</li>
 *   <li>Trailing whitespace on each line is removed (incidental whitespace)</li>
 *   <li>Escape sequences still work: \n, \t, \\, \"</li>
 *   <li>Use \ at end of line to prevent line terminator</li>
 *   <li>Great for SQL, JSON, HTML templates</li>
 * </ul>
 */
public class TextBlocksDemo {

    /**
     * Basic multi-line text block.
     */
    public static String multiLine() {
        return """
                Line 1
                Line 2
                Line 3
                """;
    }

    /**
     * JSON template using text block with string interpolation via formatted().
     */
    public static String jsonTemplate(String name, int age, String email) {
        return """
                {
                    "name": "%s",
                    "age": %d,
                    "email": "%s"
                }
                """.formatted(name, age, email);
    }

    /**
     * SQL template using text block.
     */
    public static String sqlTemplate(String table, String condition) {
        return """
                SELECT *
                FROM %s
                WHERE %s
                ORDER BY id
                """.formatted(table, condition);
    }

    /**
     * HTML template using text block.
     */
    public static String htmlTemplate(String title, String body) {
        return """
                <html>
                <head><title>%s</title></head>
                <body>%s</body>
                </html>
                """.formatted(title, body);
    }

    /**
     * Demonstrates that leading whitespace is stripped based on closing """ position.
     * The closing """ position determines the indentation that gets removed.
     */
    public static String indentationDemo() {
        return """
                    This line has 4 spaces of indentation relative to closing quotes.
                    This line also has 4 spaces.
                """;
    }

    /**
     * Demonstrates escape sequences in text blocks.
     * \ at end of line suppresses the newline (line continuation).
     */
    public static String lineContinuation() {
        return """
                This is a very long line that we \
                want to continue on the next source line \
                but appear as one line in the output.""";
    }

    /**
     * Demonstrates that you can include quotes inside text blocks
     * without escaping (single or double), only triple-quotes need escaping.
     */
    public static String quotesInside() {
        return """
                He said "hello" and 'goodbye'.
                No escaping needed for single or double quotes.
                """;
    }

    /**
     * Empty text block.
     */
    public static String emptyBlock() {
        return """
                """;
    }
}
