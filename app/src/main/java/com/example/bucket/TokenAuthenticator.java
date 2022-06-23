package com.example.bucket;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;


public class TokenAuthenticator implements Authenticator {

    //private final SharedPrefManager sharedPrefManager;
    String accessToken, newAccessToken, updatedAccessToken;

    //public TokenAuthenticator(SharedPrefManager sharedPrefManager) {
        //this.sharedPrefManager = sharedPrefManager;
    //}

    @Nullable
    @Override
    public Request authenticate(Route route, Response response) {
        if (!isRequestWithAccessToken(response) || accessToken == null) {
            return null;
        }
        synchronized (this) {
            // Access token is refreshed in another thread.
            if (!accessToken.equals(newAccessToken)) {
                return newRequestWithAccessToken(response.request(), newAccessToken);
            }

            // Need to refresh an access token
            return newRequestWithAccessToken(response.request(), updatedAccessToken);
        }
    }

    private boolean isRequestWithAccessToken(@NonNull Response response) {
        String header = response.request().header("Authorization");
        return header != null && header.startsWith("Bearer");
    }

    @NonNull
    private Request newRequestWithAccessToken(@NonNull Request request, @NonNull String accessToken) {
        return request.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();
    }
}