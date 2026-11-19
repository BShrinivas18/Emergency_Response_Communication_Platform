param([ValidatePattern('^ercp(_[a-z0-9]{1,24})?$')][string]$DatabasePrefix = 'ercp')

$ErrorActionPreference = 'Stop'
$root = Split-Path $PSScriptRoot -Parent
$pg = if ($env:PG_BIN) { $env:PG_BIN } else { 'C:\Program Files\PostgreSQL\18\bin' }
$state = Join-Path $root '.local-run'
New-Item -ItemType Directory -Path $state -Force | Out-Null
$secretsPath = Join-Path $state 'secrets.json'
if (!(Test-Path $secretsPath)) {
  $bytes = New-Object byte[] 48
  [System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
  $password = [Convert]::ToBase64String($bytes)
  [System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
  @{ databasePassword=$password; jwtSecret=[Convert]::ToBase64String($bytes) } | ConvertTo-Json | Set-Content $secretsPath
}
$secrets = Get-Content $secretsPath -Raw | ConvertFrom-Json
$env:PGPASSWORD = $secrets.databasePassword
$data = Join-Path $state 'postgres'
if (!(Test-Path (Join-Path $data 'PG_VERSION'))) {
  $passwordFile = Join-Path $state 'init-password.txt'
  [System.IO.File]::WriteAllText($passwordFile,$secrets.databasePassword)
  & "$pg\initdb.exe" -D $data -U ercp_dev --auth-host=scram-sha-256 --auth-local=scram-sha-256 --pwfile=$passwordFile --encoding=UTF8 --locale=C *> (Join-Path $state 'postgres-init.log')
  if ($LASTEXITCODE -ne 0) { throw 'Isolated database initialization failed; inspect postgres-init.log' }
  Remove-Item -LiteralPath $passwordFile
}
& "$pg\pg_ctl.exe" -D $data status *> $null
if ($LASTEXITCODE -ne 0) {
  & "$pg\pg_ctl.exe" -D $data -l (Join-Path $state 'postgres.log') -o '-h 127.0.0.1 -p 55432' -w start
  if ($LASTEXITCODE -ne 0) { throw 'Isolated database startup failed' }
}
foreach ($service in @('gateway','incident','responder','location','victim','log','user')) {
  $database = "${DatabasePrefix}_$service"
  $exists = & "$pg\psql.exe" -h 127.0.0.1 -p 55432 -U ercp_dev -d postgres -tAc "SELECT 1 FROM pg_database WHERE datname='$database'"
  if ($exists -ne '1') {
    & "$pg\createdb.exe" -h 127.0.0.1 -p 55432 -U ercp_dev $database
    if ($LASTEXITCODE -ne 0) { throw "Could not create $database" }
  }
}
Write-Output "Isolated PostgreSQL ready on 127.0.0.1:55432 with database prefix $DatabasePrefix."
