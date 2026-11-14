$ErrorActionPreference = 'Stop'
$root = Split-Path $PSScriptRoot -Parent
$front = Join-Path $root 'Frontend-ERCP/emergency-response-platform'
$state = Join-Path $root '.local-run'
$node = (Get-Command node -ErrorAction Stop).Source
# Compile before starting Java services to keep memory use bounded.
Push-Location $front
try {
 & $node node_modules/@angular/cli/bin/ng.js build
 if ($LASTEXITCODE -ne 0) { throw 'Frontend build failed' }
} finally { Pop-Location }
& (Join-Path $PSScriptRoot 'setup-postgres.ps1')
& (Join-Path $PSScriptRoot 'start-services.ps1')
$process = Start-Process -FilePath $node -ArgumentList ('"'+(Join-Path $PSScriptRoot 'serve-frontend.cjs')+'"') -WorkingDirectory $root -WindowStyle Hidden -RedirectStandardOutput (Join-Path $state 'frontend.log') -RedirectStandardError (Join-Path $state 'frontend-error.log') -PassThru
$process.Id | Set-Content (Join-Path $state 'frontend.pid')
Write-Output 'Starting local stack. Open http://127.0.0.1:4200 after startup completes. Local admin credentials are in .local-run/secrets.json (localAdminPassword).'
