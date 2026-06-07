"""pytest test package for py_proj.

测试触发规则来自项目根目录的 `pyproject.toml`：
pytest 会扫描 `tests/` 目录下的 `test_*.py` 文件，执行 `Test*` 类中的
`test_*` 方法，并通过 pytest-cov 统计 `src/py_proj` 的源码覆盖率。
"""
