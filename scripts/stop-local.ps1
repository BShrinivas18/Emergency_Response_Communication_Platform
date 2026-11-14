$ErrorActionPreference='Stop'
$root=Split-Path $PSScriptRoot -Parent
$state = Join-Path $root '.local-run'
foreach ($file in Get-ChildItem $state -Filter '*.pid') {
 $processId=[int](Get-Content $file.FullName)
 $process=Get-CimInstance Win32_Process -Filter "ProcessId=$processId" -ErrorAction SilentlyContinue
 if ($process -and $process.CommandLine -and $process.CommandLine.Contains($root) -and $process.Name -in @('java.exe','node.exe')) {
   Stop-Process -Id $processId
   Write-Output ('Stopped '+$file.BaseName)
 }
}
$pg = if ($env:PG_BIN) { $env:PG_BIN } else { 'C:\Program Files\PostgreSQL\18\bin' }
& (Join-Path $pg 'pg_ctl.exe') -D (Join-Path $state 'postgres') -m fast -w stop
