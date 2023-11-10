package com.dku.blindnavigation.repository.navigate.remote;

import android.content.Context;

import androidx.annotation.NonNull;

import com.dku.blindnavigation.R;
import com.dku.blindnavigation.utils.location.CSRResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.function.Consumer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CSRClient {

    private static class CSRCallback implements Callback {

        private static final String TAG = "CSRCallback";

        private final Consumer<String> mConsumer;

        public CSRCallback(Consumer<String> consumer) {
            this.mConsumer = consumer;
        }

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {

        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            String message = new ObjectMapper().readValue(response.body().string(), CSRResponse.class).getText();
            mConsumer.accept(message);
        }
    }

    private static final CSRClient instance = new CSRClient();
    private final OkHttpClient okClient;

    public static CSRClient getInstance() {
        return instance;
    }

    private CSRClient() {
        this.okClient = new OkHttpClient();
    }

    public void sendRequest(Context context, byte[] data, Consumer<String> consumer) {
        Request request = new Request.Builder()
                .url(context.getString(R.string.ncloud_csr_uri))
                .post(generateRequestBody(data))
                .addHeader("X-NCP-APIGW-API-KEY-ID", context.getString(R.string.ncloud_csr_client_id))
                .addHeader("X-NCP-APIGW-API-KEY", context.getString(R.string.ncloud_csr_client_secret))
                .addHeader("Content-Type", "application/octet-stream")
                .build();

        okClient.newCall(request)
                .enqueue(new CSRCallback(consumer));
    }

    private RequestBody generateRequestBody(byte[] data) {
        return RequestBody.create(data);
    }

}
