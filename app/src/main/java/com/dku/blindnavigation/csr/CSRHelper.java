package com.dku.blindnavigation.csr;

import android.content.Context;

import com.dku.blindnavigation.R;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CSRHelper {
    private static final CSRHelper instance = new CSRHelper();
    private final OkHttpClient okClient;

    private static CSRHelper getInstance() {
        return instance;
    }

    private CSRHelper() {
        this.okClient = new OkHttpClient();
    }

    public void requestCSR(Context context, Callback callback) {
        Request request = new Request.Builder()
                .url(context.getString(R.string.ncloud_csr_uri))
                .addHeader("X-NCP-APIGW-API-KEY-ID", context.getString(R.string.ncloud_csr_client_id))
                .addHeader("X-NCP-APIGW-API-KEY", context.getString(R.string.ncloud_csr_client_secret))
                .addHeader("Content-Type", "application/octet-stream")
//                .post()
                .build();
        okClient.newCall(request).enqueue(callback);
    }

    private RequestBody generateRequestBody() {
        return null;
    }
}
