from __future__ import annotations

import pytest

import regex_proj
from regex_proj import assertions, dialects, engine, groups, operations, patterns, quantifiers, redos, review


def test_package_exports_modules() -> None:
    assert "patterns" in regex_proj.__all__
    assert regex_proj.patterns is patterns


def test_patterns_character_escaping_and_classes() -> None:
    assert patterns.escape_literal("a.b*") == "a\\.b\\*"
    assert patterns.find_date_shapes("x 2026-06-06 y 2026-99-99") == ["2026-06-06", "2026-99-99"]
    assert patterns.is_date_shape("2026-06-06")
    assert not patterns.is_date_shape("x2026-06-06")
    assert patterns.is_six_digit_code("123456")
    assert not patterns.is_six_digit_code("abc123456xyz")
    assert patterns.is_hex_color("#12AF90")
    assert not patterns.is_hex_color("#12AG90")
    assert patterns.is_identifier("_name1")
    assert not patterns.is_identifier("1name")
    assert patterns.is_windows_path_shape(r"C:\Users\alice")
    assert not patterns.is_windows_path_shape(r"C:/Users/alice")
    assert patterns.dot_matches("a-b")
    assert not patterns.dot_matches("a\nb")
    assert patterns.dot_matches("a\nb", dotall=True)
    assert patterns.char_class_examples("a")["in_abc"]
    assert patterns.char_class_examples("F")["hex_digit"]
    assert patterns.char_class_examples("x")["not_digit"]
    assert patterns.char_class_examples("-")["hyphen_literal"]
    assert patterns.char_class_examples("]")["right_bracket_literal"]
    assert patterns.predefined_class_report("5")["digit"]
    assert patterns.predefined_class_report("A")["not_digit"]
    assert patterns.predefined_class_report("你")["word_unicode"]
    assert not patterns.predefined_class_report("你")["word_ascii"]
    assert patterns.predefined_class_report(" ")["space"]
    assert patterns.predefined_class_report("x")["not_space"]
    assert patterns.literal_special_char_matches(".")
    assert not patterns.literal_special_char_matches("a")


def test_quantifiers_greedy_lazy_possessive_and_scope() -> None:
    assert all(quantifiers.basic_quantifier_matches().values())
    text = '"a" and "b"'
    assert quantifiers.greedy_quoted_span(text) == '"a" and "b"'
    assert quantifiers.greedy_quoted_span("no quotes") is None
    assert quantifiers.lazy_quoted_spans(text) == ['"a"', '"b"']
    assert quantifiers.bounded_quoted_spans(text) == ['"a"', '"b"']
    escaped = r'"a\"b" "c"'
    assert quantifiers.escaped_string_literals(escaped) == [r'"a\"b"', '"c"']
    assert all(quantifiers.repeat_scope_results().values())
    assert quantifiers.comma_fields("a,b,,c") == ["a", "b", "c"]
    possessive = quantifiers.possessive_quantifier_demo("123")
    assert possessive == {
        "greedy_can_backtrack": True,
        "possessive_will_not_backtrack": False,
        "atomic_will_not_backtrack": False,
    }


def test_groups_capture_named_non_capture_alternation_and_reference() -> None:
    assert groups.parse_date_numbered("2026-06-06") == ("2026", "06", "06")
    assert groups.parse_date_numbered("bad") is None
    assert groups.parse_date_named("2026-06-06") == {"year": "2026", "month": "06", "day": "06"}
    assert groups.parse_date_named("bad") is None
    assert groups.supports_http_or_https("https://example.com")
    assert not groups.supports_http_or_https("ftp://example.com")
    assert groups.parse_url("https://example.com/a/b") == {
        "scheme": "https",
        "host": "example.com",
        "path": "/a/b",
    }
    assert groups.parse_url("bad") is None
    assert groups.parse_level_date_message("INFO 2026-06-06 start job") == {
        "level": "INFO",
        "date": "2026-06-06",
        "message": "start job",
    }
    assert groups.parse_level_date_message("DEBUG 2026-06-06 start") is None
    assert groups.alternation_order("catalog") == "cat"
    assert groups.alternation_order("catalog", r"catalog|cat") == "catalog"
    assert groups.alternation_order("dog") is None
    assert groups.grey_or_gray("gray")
    assert groups.grey_or_gray("grey")
    assert groups.repeated_word("hello hello") == "hello"
    assert groups.repeated_word("hello world") is None
    assert groups.matching_html_pair("<h1>title</h1>") == "h1"
    assert groups.matching_html_pair("<h1>title</h2>") is None
    assert groups.replace_date_to_us("date=2026-06-06") == "date=06/06/2026"


def test_assertions_anchors_boundaries_flags_and_lookaround() -> None:
    assert assertions.contains_date("abc2026-06-06xyz")
    assert not assertions.full_date("abc2026-06-06xyz")
    assert assertions.full_date("2026-06-06")
    assert assertions.absolute_whole_string_abc("abc")
    assert not assertions.absolute_whole_string_abc("abc\n")
    assert assertions.dollar_allows_final_newline("abc\n")
    log_text = "INFO ok\nerror: bad\nERROR failed"
    assert assertions.find_error_lines(log_text) == ["error: bad", "ERROR failed"]
    assert assertions.dotall_block("<body>a\nb</body>") == "<body>a\nb</body>"
    assert assertions.dotall_block("<body>a") is None
    assert assertions.verbose_log_parse("2026-06-06 WARN disk high") == {
        "date": "2026-06-06",
        "level": "WARN",
        "message": "disk high",
    }
    assert assertions.verbose_log_parse("bad") is None
    assert assertions.whole_word_cat("cat catalog cat") == ["cat", "cat"]
    assert assertions.numbers_before_unit("128yuan 50% 3yuan") == ["128", "3"]
    assert assertions.numbers_before_unit("50% 10yuan", "%") == ["50"]
    assert assertions.foo_not_followed_by_bar("foo foobar fooz") == ["foo", "foo"]
    assert assertions.numbers_after_currency("USD128 CNY50 USD3") == ["128", "3"]
    assert assertions.numbers_not_after_minus("-12 34 5") == ["34", "5"]
    assert assertions.identifiers_not_test_prefix(["test_a", "_ok", "1bad", "good2"]) == ["_ok", "good2"]
    assert assertions.valid_password("abc12345")
    assert not assertions.valid_password("abcdefgh")
    assert not assertions.valid_password("abc123")
    assert assertions.filename_not_tmp("report.txt")
    assert not assertions.filename_not_tmp("cache.tmp")
    assert assertions.parenthesized_content("a(b)c(de)") == ["b", "de"]


def test_engine_execution_trace_and_complexity_helpers() -> None:
    trace = engine.trace_a_dot_star_b("axxbxxb")
    assert [step["step"] for step in trace] == [1, 2, 3, 4, 5]
    assert trace[-1]["pos"] == 6
    assert engine.unanchored_start_attempts("abc", "xxabc") == [0, 1, 2]
    assert engine.unanchored_start_attempts("abc", "xxxxx") == [0, 1, 2, 3, 4]
    assert engine.first_branch_match(r"cat|catalog", "catalog") == "cat"
    assert engine.first_branch_match(r"catalog|cat", "catalog") == "catalog"
    assert engine.first_branch_match(r"dog", "catalog") is None
    assert engine.capture_after_backtracking("abc") == "ab"
    assert engine.capture_after_backtracking("ac") == "a"
    assert engine.capture_after_backtracking("abbc") is None
    assert set(engine.nested_quantifier_partitions(3)) == {(3,), (1, 2), (2, 1), (1, 1, 1)}
    assert engine.nested_quantifier_path_count(4) == 8
    comparisons = engine.engine_comparisons()
    assert comparisons[0].backtracking
    assert not comparisons[1].supports_backreference


def test_redos_audit_attack_rewrite_and_conditions() -> None:
    audit = redos.audit_regex(r"^(a+)+$", user_controlled=True, max_length=None, has_timeout=False)
    assert audit.risk == "exponential"
    assert "nested_quantifier" in audit.findings
    assert "limit input length" in audit.mitigations
    overlap = redos.audit_regex(r"^(a|aa)+$", user_controlled=True, max_length=100, has_timeout=True)
    assert overlap.risk == "review"
    assert "overlapping_branch" in overlap.findings
    backref = redos.audit_regex(r"^([a-z]+)\1$", user_controlled=False, max_length=20, has_timeout=True)
    assert "backreference" in backref.findings
    quadratic = redos.audit_regex(r"^.*foo.*bar$", user_controlled=False, max_length=200, has_timeout=True)
    assert quadratic.risk == "quadratic"
    low = redos.audit_regex(r"^[A-Za-z_][A-Za-z0-9_]{2,31}$", False, 32, True)
    assert low.risk == "low"
    assert redos.has_nested_quantifier(r"^(a+)+$")
    assert redos.has_overlapping_a_branch(r"^(aa|a)+$")
    assert redos.has_multiple_dot_star(r"^.*a.*$")
    assert redos.has_late_failure_shape(r"^a+$")
    assert redos.attack_sample(r"^(a+)+$", 4) == "aaaa!"
    assert redos.attack_sample(r"^(\w+\s*)+$", 2) == "wordword!"
    assert redos.attack_sample(r"^x+$", 3) == "xxx!"
    assert redos.rewrite(r"^(a+)+$") == r"^a+$"
    assert redos.rewrite(r"^(a|aa)+$") == r"^a+$"
    assert redos.rewrite(r"^(.+)+@example\.com$") == r"^[^@\s]+@example\.com$"
    assert redos.rewrite(r"^(a?){30}a{30}$") == r"^a{30,60}$"
    assert redos.rewrite(r"^.*foo.*bar$") == "use text.find('foo') before text.find('bar')"
    assert redos.rewrite(r"unknown") == "add explicit bounds, length limits, and performance tests"
    assert redos.risk_conditions(True, True, True)
    assert not redos.risk_conditions(True, False, True)


def test_operations_extract_replace_split_clean_and_project_parser() -> None:
    assert operations.parse_simple_log("2026-06-06 10:30:20 INFO user=alice action=login") == {
        "date": "2026-06-06",
        "time": "10:30:20",
        "level": "INFO",
        "user": "alice",
        "action": "login",
    }
    assert operations.parse_simple_log("bad line") is None
    assert operations.replace_date_to_us("date=2026-06-06") == "date=06/06/2026"
    assert operations.split_on_comma_space("a, b ,c") == ["a", "b", "c"]
    assert operations.split_words("  hello   regex world  ") == ["hello", "regex", "world"]
    assert operations.collapse_whitespace(" a\t b\nc ") == "a b c"
    assert operations.strip_trailing_horizontal_space("a  \nb\t\nc") == "a\nb\nc"
    assert operations.parse_metric_line("ip=10.0.0.1 status=200 cost=32ms") == {
        "ip": "10.0.0.1",
        "status": 200,
        "cost": 32,
    }
    assert operations.parse_metric_line("bad") is None
    assert operations.mask_phone("13812345678") == "138****5678"
    assert operations.mask_phone("bad") == "bad"
    assert operations.mask_email_user("carol@example.com") == "c***@example.com"
    assert operations.mask_email_user("bad") == "bad"
    nginx = '10.0.0.1 - - [06/Jun/2026:21:10:00 +0800] "GET /api/jobs HTTP/1.1" 200 532'
    assert operations.parse_nginx_log(nginx)["bytes"] == 532
    nginx_empty_bytes = '10.0.0.1 - - [06/Jun/2026:21:10:00 +0800] "GET /api/jobs HTTP/1.1" 204 -'
    assert operations.parse_nginx_log(nginx_empty_bytes)["bytes"] is None
    assert operations.parse_nginx_log("bad") is None
    project_line = "2026-06-06 21:32:10 ERROR ip=10.0.0.3 user=carol@example.com action=pay cost=900ms"
    parsed = operations.parse_project_line(project_line)
    assert parsed is not None
    assert parsed["cost"] == 900
    assert parsed["user"] == "c***@example.com"
    assert operations.parse_project_line("bad line") is None
    result = operations.parse_project_lines(
        [
            "2026-06-06 21:30:01 INFO ip=10.0.0.1 user=alice action=login cost=32ms",
            "bad line",
            "2026-06-06 21:31:05 WARN ip=10.0.0.2 user=bob action=query cost=120ms extra",
        ]
    )
    assert len(result.records) == 1
    assert result.invalid_lines == ((2, "bad line"), (3, "2026-06-06 21:31:05 WARN ip=10.0.0.2 user=bob action=query cost=120ms extra"))
    assert operations.extract_url_parts("https://example.com/a") == {
        "scheme": "https",
        "host": "example.com",
        "path": "/a",
    }
    assert operations.extract_url_parts("not-url") is None


@pytest.mark.parametrize(
    ("target", "expected"),
    [
        ("java", r'"\\d+"'),
        ("javascript_literal", r"/\d+/"),
        ("javascript_constructor", r'new RegExp("\\d+")'),
        ("python", r'r"\d+"'),
        ("json", r'"\\d+"'),
        ("raw", r"\d+"),
    ],
)
def test_dialects_source_literals(target: str, expected: str) -> None:
    assert dialects.source_literal(r"\d+", target) == expected


def test_dialects_features_api_unicode_replacement_and_migration() -> None:
    assert dialects.feature_supported("named_group", "java")
    assert not dialects.feature_supported("backreference", "re2")
    assert not dialects.feature_supported("lookahead", "re2")
    assert not dialects.feature_supported("lookbehind", "re2")
    assert dialects.feature_supported("possessive_quantifier", "python_re")
    assert not dialects.feature_supported("atomic_group", "javascript")
    assert dialects.replacement_syntax("javascript") == ("$1", "$<name>")
    assert dialects.replacement_syntax("java") == ("$1", "${name}")
    assert dialects.replacement_syntax("python") == (r"\1 or \g<1>", r"\g<name>")
    assert dialects.replacement_syntax("dotnet") == ("$1", "${name}")
    api = dialects.python_api_results(r"\d+", "abc123")
    assert api["match"] is None
    assert api["search"] == "123"
    assert api["fullmatch"] is None
    assert api["findall_no_group"] == ["123"]
    assert api["findall_group"] == [("abc", "123")]
    assert dialects.python_api_results(r"\d+", "123")["fullmatch"] == "123"
    js_re = dialects.JavaScriptGlobalDigit()
    assert js_re.test("1")
    assert not js_re.test("1")
    assert js_re.test("2")
    unicode_report = dialects.unicode_word_report("你好")
    assert unicode_report["python_unicode_word"]
    assert not unicode_report["python_ascii_word"]
    assert unicode_report["word_boundary_finds"]
    checklist = dialects.migration_checklist(r"(?<year>\d{4})-\1", "javascript", "java")
    assert "check named group syntax" in checklist
    assert "check backreference support" in checklist
    assert "add target runtime tests" in checklist
    simple = dialects.migration_checklist(r"\d+", "python", "java")
    assert "check named group syntax" not in simple


def test_review_docs_case_studies_templates_and_findings() -> None:
    doc = review.build_phone_mask_doc()
    assert doc.name == "phone_mask"
    assert doc.user_controlled
    assert doc.capture_fields == ("prefix", "suffix")
    assert review.email_shape("a@example.com")
    assert not review.email_shape("a example.com")
    assert review.url_precheck("https://example.com/path")
    assert not review.url_precheck("ftp://example.com")
    parsed = review.parse_bounded_kv_log("level=INFO user=alice cost=20ms path=/api/jobs")
    assert parsed == {"level": "INFO", "user": "alice", "cost": 20, "path": "/api/jobs"}
    assert review.parse_bounded_kv_log("level=DEBUG user=alice cost=20ms path=/api/jobs") is None
    assert review.valid_password_policy("abc12345")
    assert not review.valid_password_policy("abcdefgh")
    assert review.path_allowed_after_normalization("/admin/jobs")
    assert review.path_allowed_after_normalization("/%61dmin/jobs")
    assert not review.path_allowed_after_normalization("/admin/../public")
    assert review.should_use_parser("HTML")
    assert not review.should_use_parser("log")
    template = review.sample_template()
    assert "long late failure" in template["performance"]
    findings = review.review_findings(r"^(\w+\.)*\w+@\w+(\.\w+)+$", "search", True, None, True)
    assert "character class may be too broad" in findings["correctness"]
    assert "nested quantifier" in findings["performance"]
    assert "missing input length limit" in findings["performance"]
    assert "user controlled input" in findings["security"]
    assert "do not rely on regex alone for security" in findings["security"]
    assert "consider named or noncapturing groups" in findings["maintainability"]
    loose = review.review_findings(r"foo.*bar.*baz", "search", False, 200, False)
    assert "not clearly whole-input checked" in loose["correctness"]
    assert "multiple dot-star" in loose["performance"]
    compat = review.review_findings(r"(?<=USD)\d++\1", "fullmatch", False, 20, False)
    assert "dialect-specific feature" in compat["compatibility"]
    clear = review.review_findings(r"^[A-Z]{2}$", "fullmatch", False, 2, False)
    assert clear == {
        "correctness": (),
        "performance": (),
        "security": (),
        "maintainability": (),
        "compatibility": (),
    }
