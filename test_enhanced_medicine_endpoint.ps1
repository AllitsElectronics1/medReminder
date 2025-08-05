# Test Enhanced Medicine Endpoint with Schedules and Devices
$baseUrl = "http://localhost:8080"

Write-Host "=== Testing Enhanced Medicine Endpoint ==="
Write-Host "This endpoint now creates medicine, schedules, and devices!"
Write-Host ""

# Test 1: Android App Style (with patientId, schedules, and device)
Write-Host "1. Testing Android App Style (complete request)..."
$androidStyleData = @{
    patientId = 29  # Specific patient ID
    medicineName = "Android Medicine"
    dosage = "1 tablet"
    startDate = "2025-08-04"
    endDate = "2025-12-31"
    daysOfWeek = @("MONDAY", "WEDNESDAY", "FRIDAY")
    time = "09:00:00"
    reminderMinutesBefore = 30
    deviceId = "ANDROID_DEVICE_001"
} | ConvertTo-Json

Write-Host "Android request: $androidStyleData"

try {
    $androidResponse = Invoke-RestMethod -Uri "$baseUrl/api/medicines" -Method POST -Body $androidStyleData -ContentType "application/json" -Headers @{
        "Authorization" = "Bearer YOUR_JWT_TOKEN_HERE"
        "Accept" = "application/json"
    }
    Write-Host "✅ Android style successful!"
    Write-Host "Response: $($androidResponse | ConvertTo-Json -Depth 2)"
} catch {
    Write-Host "❌ Android style failed: $($_.Exception.Message)"
}

Write-Host ""

# Test 2: Web App Style (without patientId - uses current user's first patient)
Write-Host "2. Testing Web App Style (without patientId)..."
$webStyleData = @{
    medicineName = "Web Medicine"
    dosage = "2 tablets"
    startDate = "2025-08-04"
    endDate = "2025-12-31"
    daysOfWeek = @("TUESDAY", "THURSDAY", "SATURDAY")
    time = "14:30:00"
    reminderMinutesBefore = 15
    deviceId = "WEB_DEVICE_001"
} | ConvertTo-Json

Write-Host "Web request: $webStyleData"

try {
    $webResponse = Invoke-RestMethod -Uri "$baseUrl/api/medicines" -Method POST -Body $webStyleData -ContentType "application/json" -Headers @{
        "Authorization" = "Bearer YOUR_JWT_TOKEN_HERE"
        "Accept" = "application/json"
    }
    Write-Host "✅ Web style successful!"
    Write-Host "Response: $($webResponse | ConvertTo-Json -Depth 2)"
} catch {
    Write-Host "❌ Web style failed: $($_.Exception.Message)"
}

Write-Host ""

# Test 3: Simple Medicine (without schedules or device)
Write-Host "3. Testing Simple Medicine (without schedules or device)..."
$simpleData = @{
    patientId = 29
    medicineName = "Simple Medicine"
    dosage = "1 capsule"
    startDate = "2025-08-04"
} | ConvertTo-Json

Write-Host "Simple request: $simpleData"

try {
    $simpleResponse = Invoke-RestMethod -Uri "$baseUrl/api/medicines" -Method POST -Body $simpleData -ContentType "application/json" -Headers @{
        "Authorization" = "Bearer YOUR_JWT_TOKEN_HERE"
        "Accept" = "application/json"
    }
    Write-Host "✅ Simple medicine successful!"
    Write-Host "Response: $($simpleResponse | ConvertTo-Json -Depth 2)"
} catch {
    Write-Host "❌ Simple medicine failed: $($_.Exception.Message)"
}

Write-Host ""
Write-Host "=== Summary ==="
Write-Host "✅ Enhanced endpoint now handles:"
Write-Host "   - Medicine creation"
Write-Host "   - Medicine schedules (optional)"
Write-Host "   - Device creation/update (optional)"
Write-Host "   - Both Android and Web app styles"
Write-Host ""
Write-Host "Note: Replace 'YOUR_JWT_TOKEN_HERE' with actual JWT token from login" 