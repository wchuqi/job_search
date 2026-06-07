package com.javastudy.stringdatetime;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Demonstrates regular expressions in Java using Pattern and Matcher.
 * <p>
 * Key points:
 * <ul>
 *   <li>Pattern.compile() - compiles a regex into a reusable Pattern object</li>
 *   <li>Pattern.matches() - one-shot match of entire string</li>
 *   <li>Matcher.find() - find next match (not necessarily full string)</li>
 *   <li>Matcher.group() - retrieve the matched text</li>
 *   <li>Matcher.group(n) - retrieve captured group n</li>
 *   <li>Escaping: use \\ in Java strings for regex \ (e.g., \\. for literal dot)</li>
 *   <li>Precompile patterns that are used repeatedly for performance</li>
 * </ul>
 */
public class RegexDemo {

    // Precompiled patterns for reuse
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("[\\w.+-]+@[\\w-]+\\.[\\w.]+");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("(\\d{3})[- ]?(\\d{3})[- ]?(\\d{4})");

    private static final Pattern DATE_PATTERN =
            Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");

    /**
     * Pattern.matches() checks if the entire string matches the regex.
     */
    public static boolean matchesEntireString(String input, String regex) {
        return Pattern.matches(regex, input);
    }

    /**
     * Pattern.compile() + Matcher.find() to find all matches in a string.
     */
    public static List<String> findAllMatches(String input, String regex) {
        List<String> matches = new ArrayList<>();
        Matcher matcher = Pattern.compile(regex).matcher(input);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    /**
     * Using Matcher.group(n) to extract captured groups.
     * Returns groups as a list: [fullMatch, group1, group2, ...]
     */
    public static List<String> extractGroups(String input, String regex) {
        List<String> groups = new ArrayList<>();
        Matcher matcher = Pattern.compile(regex).matcher(input);
        if (matcher.find()) {
            for (int i = 0; i <= matcher.groupCount(); i++) {
                groups.add(matcher.group(i));
            }
        }
        return groups;
    }

    /**
     * Validate an email address using a precompiled pattern.
     */
    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Extract phone number parts using captured groups.
     * Returns [areaCode, prefix, lineNumber] or empty list if no match.
     */
    public static List<String> parsePhoneNumber(String phone) {
        Matcher matcher = PHONE_PATTERN.matcher(phone);
        if (matcher.find()) {
            return List.of(matcher.group(1), matcher.group(2), matcher.group(3));
        }
        return List.of();
    }

    /**
     * Replace all matches using Matcher.replaceAll().
     */
    public static String replaceMatches(String input, String regex, String replacement) {
        return Pattern.compile(regex).matcher(input).replaceAll(replacement);
    }

    /**
     * Demonstrates escaping special regex characters.
     * In regex, . * + ? ^ $ { } [ ] ( ) | \ are special and need \\ to be literal.
     */
    public static boolean matchLiteralDot(String input) {
        // Match a literal dot: must escape with \\
        return Pattern.matches(".*\\..*", input);
    }

    /**
     * Demonstrates Pattern.quote() for automatic escaping of literal strings.
     */
    public static boolean matchLiteralString(String input, String literal) {
        // Pattern.quote() wraps the string in \Q...\E to treat it as literal
        return Pattern.compile(Pattern.quote(literal)).matcher(input).find();
    }

    /**
     * Split a string using regex.
     */
    public static String[] splitByRegex(String input, String regex) {
        return input.split(regex);
    }

    /**
     * Case-insensitive matching using Pattern.compile flags.
     */
    public static boolean caseInsensitiveMatch(String input, String regex) {
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(input).matches();
    }
}
