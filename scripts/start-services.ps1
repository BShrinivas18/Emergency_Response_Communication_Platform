param(
 [string[]]$Services = @('Eureka-Server','Api-Gateway-Server','Incident-Service','Responder-Service','Victim-Service','Location-Service','Log-Service','User-Service'),
 [switch]$SkipBuild,
 [ValidatePattern('^ercp(_[a-z0-9]{1,24})?$')][string]$DatabasePrefix = 'ercp'
)
$ErrorActionPreference = 'Stop'
$root = Split-Path $PSScriptRoot -Parent
$state = Join-Path $root '.local-run'
$secrets = Get-Content (Join-Path $state 'secrets.json') -Raw | ConvertFrom-Json
if (!$env:JAVA_HOME) { throw 'Set JAVA_HOME to Java 21 before starting.' }
if (!$secrets.localAdminPassword) {
 $bytes = New-Object byte[] 24
 [System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
 $secrets | Add-Member localAdminPassword ([Convert]::ToBase64String($bytes))
 $secrets | ConvertTo-Json | Set-Content (Join-Path $state 'secrets.json')
}
$env:SPRING_PROFILES_ACTIVE = 'local'
$env:ERCP_LOCAL_ADMIN_USERNAME = 'local-admin'
$env:ERCP_LOCAL_ADMIN_PASSWORD = $secrets.localAdminPassword
$env:JWT_SECRET = $secrets.jwtSecret
$env:RDS_PASSWORD = $secrets.databasePassword
$env:SPRING_DATASOURCE_USERNAME = 'ercp_dev'
$env:SPRING_DATASOURCE_PASSWORD = $secrets.databasePassword
$env:CORS_ORIGINS = 'http://localhost:4200,http://127.0.0.1:4200'
$env:SPRING_JPA_SHOW_SQL = 'false'
$env:EUREKA_CLIENT_SERVICEURL_DEFAULTZONE = 'http://127.0.0.1:8761/eureka/'
$definitions = @{
 'Eureka-Server'=@(8761,''); 'Api-Gateway-Server'=@(8888,'gateway');
 'Incident-Service'=@(8080,'incident'); 'Responder-Service'=@(8081,'responder');
 'Victim-Service'=@(8082,'victim'); 'Location-Service'=@(8000,'location');
 'Log-Service'=@(8084,'log'); 'User-Service'=@(8083,'user')
}
foreach ($service in $Services) {
 $config = $definitions[$service]
 if (!$config) { throw 'Unknown service' }
 if (Get-NetTCPConnection -State Listen -LocalPort $config[0] -ErrorAction SilentlyContinue) {
   throw "$service port $($config[0]) is in use. Stop the existing stack first."
 }
 $project = Join-Path $root "Backend-ERCP/$service"
 if (!$SkipBuild) {
   & mvn -B -o -f (Join-Path $project 'pom.xml') -DskipTests package *> (Join-Path $state "$service-build.log")
   if ($LASTEXITCODE -ne 0) { throw "$service BUILD FAILED; inspect .local-run/$service-build.log" }
 }
 $jar = Get-ChildItem (Join-Path $project 'target') -Filter '*.jar' | Select-Object -First 1
 if (!$jar) { throw "Package missing for $service; run without -SkipBuild" }
 $env:SERVER_PORT = [string]$config[0]
 $env:SPRING_DATASOURCE_URL = "jdbc:postgresql://127.0.0.1:55432/${DatabasePrefix}_$($config[1])"
 $process = Start-Process -FilePath (Join-Path $env:JAVA_HOME 'bin/java.exe') -ArgumentList @('-Xms32m','-Xmx160m','-XX:MaxMetaspaceSize=160m','-XX:+UseSerialGC','-jar',('"'+$jar.FullName+'"')) -WorkingDirectory $project -WindowStyle Hidden -RedirectStandardOutput (Join-Path $state "$service.log") -RedirectStandardError (Join-Path $state "$service-error.log") -PassThru
 $process.Id | Set-Content (Join-Path $state "$service.pid")
 Write-Output "$service launched: PID $($process.Id), port $($config[0])"
}
