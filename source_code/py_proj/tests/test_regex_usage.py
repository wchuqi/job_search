"""Tests for regex_usage module (18-third-party regex package)."""

import pytest

from py_proj.regex_usage import (
    FuzzyResult,
    compile_user_pattern,
    fuzzy_word_match,
    grapheme_clusters,
    has_balanced_parentheses,
    overlapped_substrings,
    parse_phone_numbers,
    regex_feature_summary,
    repeated_named_captures,
    unicode_words,
    variable_length_lookbehind,
)


class TestUnicodeAndOverlappedMatches:
    def test_unicode_words(self):
        assert unicode_words("Python 正则 cafe 123") == ["Python", "正则", "cafe"]

    def test_overlapped_substrings(self):
        assert overlapped_substrings("ababa", "aba") == ["aba", "aba"]


class TestStructuredExtraction:
    def test_parse_phone_numbers(self):
        result = parse_phone_numbers("+86 010-12345678; +1 415-5551234")
        assert result == [
            {"country": "+86", "area": "010", "number": "12345678"},
            {"country": "+1", "area": "415", "number": "5551234"},
        ]

    def test_repeated_named_captures(self):
        assert repeated_named_captures("red, green, blue") == ["red", "green", "blue"]

    def test_repeated_named_captures_invalid_line(self):
        assert repeated_named_captures("red, , blue") == []


class TestAdvancedPatterns:
    def test_variable_length_lookbehind(self):
        text = "ID-7 ORDER-20250608 AB-9 ignored lower-3"
        assert variable_length_lookbehind(text) == ["7", "20250608", "9"]

    def test_balanced_parentheses(self):
        assert has_balanced_parentheses("(a(b)c)") is True
        assert has_balanced_parentheses("(a(b)") is False
        assert has_balanced_parentheses("plain text") is False


class TestFuzzyAndGraphemes:
    def test_fuzzy_word_match(self):
        assert fuzzy_word_match("colour", "color", max_errors=1) == FuzzyResult(
            matched=True,
            insertions=1,
        )

    def test_fuzzy_word_match_no_match(self):
        assert fuzzy_word_match("calendar", "color", max_errors=1) == FuzzyResult(
            matched=False
        )

    def test_fuzzy_word_match_rejects_negative_errors(self):
        with pytest.raises(ValueError, match="non-negative"):
            fuzzy_word_match("color", "color", max_errors=-1)

    def test_grapheme_clusters(self):
        assert grapheme_clusters("e\u0301👍") == ["e\u0301", "👍"]


class TestPatternCompilation:
    def test_compile_user_pattern(self):
        pattern = compile_user_pattern(r"\d+")
        assert pattern.findall("a1 b22") == ["1", "22"]

    def test_compile_user_pattern_rejects_invalid_pattern(self):
        with pytest.raises(ValueError, match="invalid regex pattern"):
            compile_user_pattern("(")


class TestSummary:
    def test_regex_feature_summary(self):
        result = regex_feature_summary()
        assert result["unicode_words"] == ["Python", "正则", "cafe"]
        assert result["overlapped"] == ["aba", "aba"]
        assert result["phones"][0]["country"] == "+86"
        assert result["captures"] == ["red", "green", "blue"]
        assert result["lookbehind"] == ["7", "20250608", "9"]
        assert result["recursive"] is True
        assert result["fuzzy"].matched is True
        assert result["clusters"] == ["e\u0301", "👍"]
