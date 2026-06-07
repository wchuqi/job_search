$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
Set-Location $projectRoot

$env:PYTHONPATH = Join-Path $projectRoot "src"
python -m unittest discover -s tests -p "test_*.py" -v
