Write-Host "Testing if application is running..."
Start-Sleep -Seconds 5

try {
    $healthResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/health" -Method GET -TimeoutSec 10
    Write-Host "✅ Application is running! Health check: $healthResponse"
    
    Write-Host ""
    Write-Host "Testing login endpoint..."
    
    $loginData = @{
        email = "test@example.com"
        password = "password123"
    } | ConvertTo-Json
    
    try {
        $loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -Body $loginData -ContentType "application/json" -TimeoutSec 10
        Write-Host "✅ Login endpoint is working!"
        Write-Host "Response: $($loginResponse | ConvertTo-Json -Depth 2)"
    } catch {
        Write-Host "⚠️ Login endpoint responded (expected for invalid credentials):"
        Write-Host "Status: $($_.Exception.Response.StatusCode)"
        if ($_.Exception.Response) {
            $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
            $responseBody = $reader.ReadToEnd()
            Write-Host "Response: $responseBody"
        }
    }
    
} catch {
    Write-Host "❌ Application is not responding: $($_.Exception.Message)"
} 