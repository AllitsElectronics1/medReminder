# Test Device and Schedule Creation
$baseUrl = "http://localhost:8080"

Write-Host "=== Testing Device and Schedule Creation ==="
Write-Host ""

# Test with complete data including device and schedules
$testData = @{
    patientId = 29
    medicineName = "Test Medicine with Device and Schedule"
    dosage = "1 tablet"
    startDate = "2025-08-04"
    endDate = "2025-12-31"
    daysOfWeek = @("MONDAY", "WEDNESDAY", "FRIDAY")
    time = "09:00:00"
    reminderMinutesBefore = 30
    deviceId = "TEST_DEVICE_001"
} | ConvertTo-Json

Write-Host "Request Data:"
Write-Host $testData
Write-Host ""

Write-Host "Making request to create medicine with device and schedules..."
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/medicines" -Method POST -Body $testData -ContentType "application/json" -Headers @{
        "Authorization" = "Bearer YOUR_JWT_TOKEN_HERE"
        "Accept" = "application/json"
    }
    
    Write-Host "✅ Request successful!"
    Write-Host "Response: $($response | ConvertTo-Json -Depth 3)"
    
    Write-Host ""
    Write-Host "=== What should have been created: ==="
    Write-Host "1. Medicine: $($response.medicineName)"
    Write-Host "2. Device: TEST_DEVICE_001"
    Write-Host "3. Schedules: 3 schedules (Monday, Wednesday, Friday at 09:00)"
    Write-Host ""
    Write-Host "Check the application logs to see detailed creation messages."
    
} catch {
    Write-Host "❌ Request failed:"
    Write-Host "Status: $($_.Exception.Response.StatusCode)"
    Write-Host "Error: $($_.Exception.Message)"
    
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response Body: $responseBody"
    }
}

Write-Host ""
Write-Host "Note: Replace 'YOUR_JWT_TOKEN_HERE' with actual JWT token from login"
Write-Host "Check the Spring Boot application logs for detailed creation messages." 