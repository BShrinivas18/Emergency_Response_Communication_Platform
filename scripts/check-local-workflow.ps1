param([Parameter(Mandatory)][switch]$CreateSyntheticFixture)

$ErrorActionPreference = 'Stop'
if (!$CreateSyntheticFixture) { throw 'This check writes a synthetic incident and responder to an empty local workspace database.' }
$root = Split-Path $PSScriptRoot -Parent
$state = Join-Path $root '.local-run'
$secrets = Get-Content (Join-Path $state 'secrets.json') -Raw | ConvertFrom-Json
$gateway = 'http://127.0.0.1:8888'

function Request-Local($method, $path, $headers = @{}, $body = $null, $status = 200) {
  $request = @{
    Uri = $gateway + $path
    Method = $method
    Headers = $headers
    TimeoutSec = 45
    SkipHttpErrorCheck = $true
  }
  if ($null -ne $body) {
    $request.Body = $body | ConvertTo-Json -Depth 8
    $request.ContentType = 'application/json'
  }
  $response = Invoke-WebRequest @request
  if ($response.StatusCode -ne $status) {
    throw "$method $path returned HTTP $($response.StatusCode), expected $status"
  }
  if ($response.Content) { return ($response.Content | ConvertFrom-Json -NoEnumerate) }
}

$admin = Request-Local POST '/login' -body @{username='local-admin'; password=$secrets.localAdminPassword}
$administrator = @{Authorization='Bearer ' + $admin.jwtToken}
if (!$admin.jwtToken -or $admin.userId -le 0) { throw 'Local admin sign-in did not return a usable identity' }
Write-Output 'Local admin login passed'

$incidents = Request-Local GET '/incidents' $administrator
$responders = Request-Local GET '/responders' $administrator
if ($incidents.Count -ne 0 -or $responders.Count -ne 0) {
  throw 'Synthetic workflow requires empty incident and responder databases to avoid assigning any existing responder.'
}

$name = 'local-test-responder'
$password = [Convert]::ToBase64String([System.Security.Cryptography.RandomNumberGenerator]::GetBytes(24))
$profile = Request-Local POST '/responders/create' $administrator @{
  name=$name; stationLocation='Synthetic validation station'; type='FIREFIGHTER'
}
$id = $profile.responderId
if ($id -le 0) { throw 'Responder profile missing' }

$created = Request-Local POST '/register' $administrator @{username=$name; password=$password; role='RESPONDER'}
if ($created.role -ne 'RESPONDER' -or $created.PSObject.Properties.Name -contains 'password') { throw 'Unexpected account response' }
@{username=$name; password=$password} | ConvertTo-Json | Set-Content (Join-Path $state 'responder-fixture.json')
$staff = Request-Local POST '/login' -body @{username=$name; password=$password}
if (!$staff.jwtToken -or $staff.responderId -ne $id) { throw 'Responder sign-in did not match the provisioned profile' }
$responder = @{Authorization='Bearer ' + $staff.jwtToken}
Write-Output 'Responder provisioning and login passed'

$receipt = Request-Local POST '/incidents/create' -body @{
  type='FIRE'; victimContact='SYNTHETIC-CALLBACK'
  incidentLocation=@{latitude=0; longitude=20.001; address='Synthetic incident fixture'}
} -status 201
$incidentId = $receipt.incidentId
if ($incidentId -le 0 -or $receipt.status -ne 'NEW') { throw 'Incident receipt missing' }
Write-Output 'Incident receipt and editable location passed'

$current = Request-Local GET "/responders/$id" $responder
if ($current.status -ne 'ASSIGNED' -or $current.incidentId -ne $incidentId) { throw 'Responder was not assigned' }
$incident = Request-Local GET "/incidents/$incidentId" $responder
if ($incident.incidentLocation.address -ne 'Synthetic incident fixture' -or $incident.victimContact -ne 'SYNTHETIC-CALLBACK') {
  throw 'Incident details were not persisted'
}
Write-Output 'Responder assignment and incident detail retrieval passed'

foreach ($nextStatus in 'ON_ROUTE','ON_SCENE','AVAILABLE','OFF_DUTY') {
  $changed = Request-Local PATCH "/responders/$id/$nextStatus" $responder @{}
  if ($changed.status -ne $nextStatus) { throw "Status $nextStatus not confirmed" }
  Write-Output "Responder status $nextStatus passed"
}
$history = Request-Local GET "/responders/$id/history" $responder
if (@($history | Where-Object {$_.eventType -eq 'STATUS_CHANGED' -and $_.actor -eq $name}).Count -lt 4) {
  throw 'Responder history did not record the authenticated actor for the four transitions'
}
if (@($history | Where-Object {$_.eventType -eq 'ASSIGNED' -and $_.incidentId -eq $incidentId}).Count -ne 1) {
  throw 'Responder assignment missing from history'
}
$incident = Request-Local GET "/incidents/$incidentId" $administrator
if ($incident.status -ne 'IN_PROGRESS') { throw 'Incident progress was not retained after release and off-duty' }
Write-Output 'Responder history and incident progress passed'
Write-Output 'Synthetic fixture retained in the isolated database. Fixture credentials are in ignored .local-run/responder-fixture.json.'
