# Test Medicine Creation Endpoint
$baseUrl = "http://localhost:8080"

Write-Host "Testing Medicine Creation Endpoint..."
Write-Host ""

# First, let's get a list of patients to use a valid patient ID
Write-Host "Getting list of patients..."
try {
    $patientsResponse = Invoke-RestMethod -Uri "$baseUrl/api/patients" -Method GET -Headers @{
        "Authorization" = "Bearer YOUR_JWT_TOKEN_HERE"
        "Accept" = "application/json"
    }
    
    if ($patientsResponse -and $patientsResponse.Count -gt 0) {
        $patientId = $patientsResponse[0].id
        Write-Host "Using patient ID: $patientId"
        
        # Test medicine creation
        $medicineData = @{
            patientId = $patientId
            medicineName = "Test Medicine"
            dosage = "1 tablet"
            startDate = "2025-08-04"
            endDate = "2025-12-31"
        } | ConvertTo-Json
        
        Write-Host "Creating medicine with data: $medicineData"
        
        try {
            $medicineResponse = Invoke-RestMethod -Uri "$baseUrl/api/medicines" -Method POST -Body $medicineData -ContentType "application/json" -Headers @{
                "Authorization" = "Bearer YOUR_JWT_TOKEN_HERE"
                "Accept" = "application/json"
            }
            
            Write-Host "✅ Medicine creation successful!"
            Write-Host "Response: $($medicineResponse | ConvertTo-Json -Depth 2)"
        } catch {
            Write-Host "❌ Medicine creation failed:"
            Write-Host "Status: $($_.Exception.Response.StatusCode)"
            if ($_.Exception.Response) {
                $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
                $responseBody = $reader.ReadToEnd()
                Write-Host "Response: $responseBody"
            }
        }
    } else {
        Write-Host "No patients found. Please create a patient first."
    }
} catch {
    Write-Host "❌ Failed to get patients: $($_.Exception.Message)"
}

Write-Host ""
Write-Host "Note: Replace 'YOUR_JWT_TOKEN_HERE' with an actual JWT token from login" 