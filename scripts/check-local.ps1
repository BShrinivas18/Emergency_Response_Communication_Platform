$ErrorActionPreference = 'Stop'
$root = Split-Path $PSScriptRoot -Parent
$secrets = Get-Content (Join-Path $root '.local-run/secrets.json') -Raw | ConvertFrom-Json
function Check-Status([string]$url, [int]$expected, [hashtable]$headers = @{}) {
 $response = Invoke-WebRequest -Uri $url -Headers $headers -TimeoutSec 30 -SkipHttpErrorCheck
 if ([int]$response.StatusCode -ne $expected) { throw "$url returned $($response.StatusCode), expected $expected" }
 Write-Output "$url : $expected PASS"
}
Check-Status 'http://127.0.0.1:4200/' 200
Check-Status 'http://127.0.0.1:8888/' 200
Check-Status 'http://127.0.0.1:8888/incidents' 401
$body = @{username='local-admin';password=$secrets.localAdminPassword} | ConvertTo-Json
$loginResponse = Invoke-WebRequest -Method Post -Uri 'http://127.0.0.1:8888/login' -ContentType 'application/json' -Body $body -TimeoutSec 30 -SkipHttpErrorCheck
if ($loginResponse.StatusCode -ne 200) { throw "Local login failed: HTTP $($loginResponse.StatusCode). Existing databases need their existing credentials." }
$login = $loginResponse.Content | ConvertFrom-Json
if (!$login.jwtToken) { throw 'Login returned no token' }
Write-Output 'Local admin login : 200 PASS (credentials and token omitted)'
$headers = @{Authorization=('Bearer '+$login.jwtToken)}
foreach ($path in @('/incidents','/responders','/logs','/locations')) {
 Check-Status ('http://127.0.0.1:8888'+$path) 200 $headers
}
Write-Output 'Read-only checks complete; no SOS, dispatch, or notifications submitted.'
