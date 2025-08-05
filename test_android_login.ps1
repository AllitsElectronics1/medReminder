# Test Android Login Endpoint
$baseUrl = "http://localhost:8080"
$loginUrl = "$baseUrl/api/auth/login"

# Test data - using email field (not username)
$loginData = @{
    email = "test@example.com"
    password = "password123"
} | ConvertTo-Json

Write-Host "Testing Android Login Endpoint..."
Write-Host "URL: $loginUrl"
Write-Host "Request Body: $loginData"
Write-Host ""

try {
    $response = Invoke-RestMethod -Uri $loginUrl -Method POST -Body $loginData -ContentType "application/json" -Headers @{
        "Accept" = "application/json"
    }
    
    Write-Host "✅ SUCCESS: Login endpoint is working!"
    Write-Host "Response: $($response | ConvertTo-Json -Depth 3)"
} catch {
    Write-Host "❌ ERROR: Login failed"
    Write-Host "Status Code: $($_.Exception.Response.StatusCode)"
    Write-Host "Error Message: $($_.Exception.Message)"
    
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response Body: $responseBody"
    }
}

Write-Host ""
Write-Host "Testing Health Check..."
try {
    $healthResponse = Invoke-RestMethod -Uri "$baseUrl/api/auth/health" -Method GET
    Write-Host "✅ Health Check: $healthResponse"
} catch {
    Write-Host "❌ Health Check Failed: $($_.Exception.Message)"
} 