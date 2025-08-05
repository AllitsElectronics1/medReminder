# Test Unified Medicine Endpoint for Both Android and Web Apps
$baseUrl = "http://localhost:8080"

Write-Host "=== Testing Unified Medicine Endpoint ==="
Write-Host "This endpoint now works for both Android and Web apps!"
Write-Host ""

# Test 1: Android App Style (with patientId)
Write-Host "1. Testing Android App Style (with patientId)..."
$androidStyleData = @{
    patientId = 29  # Specific patient ID
    medicineName = "Android Medicine"
    dosage = "1 tablet"
    startDate = "2025-08-04"
    endDate = "2025-12-31"
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
Write-Host "=== Summary ==="
Write-Host "✅ Single endpoint handles both use cases:"
Write-Host "   - Android: Send patientId for specific patient"
Write-Host "   - Web: Omit patientId to use current user's first patient"
Write-Host ""
Write-Host "Note: Replace 'YOUR_JWT_TOKEN_HERE' with actual JWT token from login" 