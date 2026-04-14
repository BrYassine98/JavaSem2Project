$ErrorActionPreference = 'Stop'

Set-Location $PSScriptRoot

if (Test-Path 'bin') {
    Remove-Item -Recurse -Force 'bin'
}

New-Item -ItemType Directory -Path 'bin' | Out-Null

$sources = Get-ChildItem -Recurse -Filter *.java 'src' | ForEach-Object { $_.FullName }
javac -d bin $sources
java -cp "bin;lib/*" Main