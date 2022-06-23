package com.example.bucket;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    String accessToken, newAccessToken, updatedAccessToken;


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = newRequestWithAccessToken(chain.request(), accessToken);
        Response response = chain.proceed(request);

        if(response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            synchronized (this) {

                if (!accessToken.equals(newAccessToken)) {
                    return chain.proceed(newRequestWithAccessToken(request, newAccessToken));
                }

                return chain.proceed(newRequestWithAccessToken(request, updatedAccessToken));
            }
        }
        return response;
    }

    @NonNull
    private Request newRequestWithAccessToken(@NonNull Request request, @NonNull String accessToken) {
        return request.newBuilder()
                .headers(getJsonHeader(accessToken))
                .build();
    }

    private static Headers getJsonHeader(String accessToken) {
        Headers.Builder builder = new Headers.Builder();
        builder.add("Content-Type", "application/json");
        builder.add("Accept", "application/json");

        return builder.build();
    }

}
