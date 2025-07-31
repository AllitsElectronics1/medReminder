package com.example.webapp;

import android.util.Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthManager {
    private static final String TAG = "AuthManager";
    private ApiService apiService;
    
    public AuthManager() {
        this.apiService = RetrofitClient.getInstance();
    }
    
    public void login(String email, String password, AuthCallback callback) {
        Log.d(TAG, "Attempting login for: " + email);
        
        AuthRequest request = new AuthRequest(email, password);
        
        Call<AuthResponse> call = apiService.login(request);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.d(TAG, "Login response code: " + response.code());
                Log.d(TAG, "Login response body: " + response.body());
                
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    if (authResponse.getToken() != null) {
                        Log.d(TAG, "Login successful for: " + email);
                        callback.onSuccess(authResponse);
                    } else {
                        Log.w(TAG, "Login failed - no token received");
                        callback.onError("Login failed: " + authResponse.getMessage());
                    }
                } else {
                    String errorBody = "";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        errorBody = "Unknown error";
                    }
                    
                    Log.e(TAG, "Login failed - HTTP " + response.code() + ": " + errorBody);
                    callback.onError("Login failed: HTTP " + response.code() + " - " + errorBody);
                }
            }
            
            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "Login network error: " + t.getMessage(), t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    
    public void register(String name, String email, String password, String role, AuthCallback callback) {
        Log.d(TAG, "Attempting registration for: " + email);
        
        RegisterRequest request = new RegisterRequest(name, email, password, role);
        
        Call<AuthResponse> call = apiService.register(request);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.d(TAG, "Registration response code: " + response.code());
                Log.d(TAG, "Registration response body: " + response.body());
                
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    if (authResponse.getToken() != null) {
                        Log.d(TAG, "Registration successful for: " + email);
                        callback.onSuccess(authResponse);
                    } else {
                        Log.w(TAG, "Registration failed - no token received");
                        callback.onError("Registration failed: " + authResponse.getMessage());
                    }
                } else {
                    String errorBody = "";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        errorBody = "Unknown error";
                    }
                    
                    Log.e(TAG, "Registration failed - HTTP " + response.code() + ": " + errorBody);
                    callback.onError("Registration failed: HTTP " + response.code() + " - " + errorBody);
                }
            }
            
            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "Registration network error: " + t.getMessage(), t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    
    public void testConnection(AuthCallback callback) {
        Log.d(TAG, "Testing connection to server");
        
        Call<String> call = apiService.healthCheck();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "Health check response code: " + response.code());
                Log.d(TAG, "Health check response body: " + response.body());
                
                if (response.isSuccessful()) {
                    Log.d(TAG, "Connection test successful");
                    callback.onSuccess(null); // No auth response for health check
                } else {
                    Log.e(TAG, "Connection test failed - HTTP " + response.code());
                    callback.onError("Connection test failed: HTTP " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Connection test network error: " + t.getMessage(), t);
                callback.onError("Connection test failed: " + t.getMessage());
            }
        });
    }
    
    // Callback interface
    public interface AuthCallback {
        void onSuccess(AuthResponse response);
        void onError(String error);
    }
} 