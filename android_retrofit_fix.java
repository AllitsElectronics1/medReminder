package com.example.webapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import android.util.Log;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    private static final String TAG = "RetrofitClient";
    private static final String BASE_URL = "http://192.168.162.56:8080/";
    private static Retrofit retrofit = null;
    private static Retrofit authenticatedRetrofit = null;

    public static ApiService getInstance() {
        if (retrofit == null) {
            Log.d(TAG, "Initializing Retrofit with BASE_URL: " + BASE_URL);
            
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            
            // Add headers interceptor
            Interceptor headersInterceptor = chain -> {
                Request original = chain.request();
                Log.d(TAG, "Making request to: " + original.url());
                
                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            };
            
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(headersInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();
            
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            
            Log.d(TAG, "Retrofit initialized successfully");
        }
        return retrofit.create(ApiService.class);
    }

    public static ApiService getAuthenticatedInstance(String token) {
        if (authenticatedRetrofit == null || token == null) {
            Log.d(TAG, "Initializing Authenticated Retrofit with BASE_URL: " + BASE_URL);
            
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            
            // Add authorization interceptor
            Interceptor authInterceptor = chain -> {
                Request original = chain.request();
                Log.d(TAG, "Making authenticated request to: " + original.url());
                
                Request request = original.newBuilder()
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            };
            
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(authInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .followRedirects(true)
                    .followSslRedirects(true)
                    .build();
            
            authenticatedRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            
            Log.d(TAG, "Authenticated Retrofit initialized successfully");
        }
        return authenticatedRetrofit.create(ApiService.class);
    }
} 