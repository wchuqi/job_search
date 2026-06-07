$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
Set-Location $projectRoot

$sources = Get-ChildItem -Recurse -Filter *.java src\main\java,src\test\java | ForEach-Object { $_.FullName }

Remove-Item -Recurse -Force out\test -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force -Path out\test | Out-Null

javac --release 21 -encoding UTF-8 -d out\test $sources
java -cp out\test com.example.algo.TestRunner
