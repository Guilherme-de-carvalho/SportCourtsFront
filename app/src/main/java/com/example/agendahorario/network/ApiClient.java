package com.example.agendahorario.network;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Padrão para emulador Android Studio (acessa localhost do host)
    private static String BASE_URL = "http://192.168.15.2:80/sport-courts-api/public/";

    // Volatile para thread-safety
    private static volatile Retrofit retrofit = null;

    private ApiClient() { }

    /**
     * Permite sobrescrever a base URL em runtime (ex.: usar IP da máquina)
     */
    public static void setBaseUrl(@NonNull String baseUrl) {
        if (!baseUrl.endsWith("/")) baseUrl = baseUrl + "/";
        BASE_URL = baseUrl;
        // Forçar nova criação do retrofit na próxima chamada
        retrofit = null;
    }

    /**
     * Retorna uma instância configurada do Retrofit usando interceptores:
     * - logging interceptor (BODY)
     * - auth interceptor que adiciona Authorization Bearer token salvo em SharedPreferences
     */
    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            synchronized (ApiClient.class) {
                if (retrofit == null) {
                    // Logging (use BODY enquanto em dev)
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                    // Interceptor para adicionar Authorization e Content-Type
                    Interceptor authInterceptor = chain -> {
                        Request original = chain.request();

                        // use Application context para evitar leaks
                        SharedPreferences prefs = context.getApplicationContext()
                                .getSharedPreferences("agendahorario", Context.MODE_PRIVATE);
                        String token = prefs.getString("api_token", null);

                        Request.Builder builder = original.newBuilder()
                                .header("Accept", "application/json")
                                .header("Content-Type", "application/json");

                        if (token != null && !token.isEmpty()) {
                            builder.header("Authorization", "Bearer " + token);
                        }

                        Request request = builder.build();
                        return chain.proceed(request);
                    };

                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(logging)
                            .addInterceptor(authInterceptor)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true)
                            .build();

                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client)
                            .build();
                }
            }
        }
        return retrofit;
    }

    /**
     * Conveniência para obter o serviço API
     */
    public static ApiService getApiService(Context context) {
        return getClient(context).create(ApiService.class);
    }

    /**
     * Remove a instância atual para forçar reconstrução (útil ao trocar token/baseUrl manualmente)
     */
    public static void reset() {
        retrofit = null;
    }
}