package com.dku.blindnavigation.repository.navigate.remote.route;

import android.content.Context;

import androidx.annotation.NonNull;

import com.dku.blindnavigation.R;
import com.dku.blindnavigation.repository.navigate.remote.dto.Poi;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RouteClient {

    private static final RouteClient instance = new RouteClient();

    private final OkHttpClient okClient;

    public static RouteClient getInstance() {
        return instance;
    }

    private RouteClient() {
        this.okClient = new OkHttpClient();
    }

    public void requestRoute(Context context, Poi startLocation, Poi endLocation, Callback callback) {
        RequestBody body = generateRequestBody(startLocation, endLocation);
        Request request = new Request.Builder()
                .url(context.getString(R.string.sk_api_route_url))
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("appKey", context.getString(R.string.sk_api_key))
                .post(body)
                .build();

        okClient.newCall(request).enqueue(callback);
    }

    @NonNull
    private RequestBody generateRequestBody(Poi startLocation, Poi endLocation) {
        return new FormBody.Builder()
                .add("startX", String.valueOf(startLocation.getFrontLon()))
                .add("startY", String.valueOf(startLocation.getFrontLat()))
                .add("endX", String.valueOf(endLocation.getFrontLon()))
                .add("endY", String.valueOf(endLocation.getFrontLat()))
                .addEncoded("startName", startLocation.getName())
                .addEncoded("endName", endLocation.getName())
                .build();
    }

}
