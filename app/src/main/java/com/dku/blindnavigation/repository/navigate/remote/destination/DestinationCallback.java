package com.dku.blindnavigation.repository.navigate.remote.destination;

import androidx.annotation.NonNull;

import com.dku.blindnavigation.repository.navigate.remote.dto.DestinationResponse;
import com.dku.blindnavigation.repository.navigate.remote.dto.Poi;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DestinationCallback implements Callback {

    private final Consumer<List<Poi>> mPois;

    public DestinationCallback(Consumer<List<Poi>> pois) {
        this.mPois = pois;
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String body = Objects.requireNonNull(response.body()).string();
        DestinationResponse locationResponse = mapper.readValue(body, DestinationResponse.class);
        if (response.code() != 200) {
            onFailure(call, new IOException());
            return;
        }
        mPois.accept(locationResponse.getPois());
    }

}
