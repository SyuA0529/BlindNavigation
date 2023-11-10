package com.dku.blindnavigation.repository.navigate.remote.route;

import androidx.annotation.NonNull;

import com.dku.blindnavigation.repository.navigate.remote.dto.Poi;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RouteCallback implements Callback {

    private final Consumer<ArrayList<Poi>> mConsumer;

    public RouteCallback(Consumer<ArrayList<Poi>> consumer) {
        mConsumer = consumer;
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {

    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        ArrayList<Poi> coordinates = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        String responseString = Objects.requireNonNull(response.body()).string();
        JsonNode featuresNode = mapper.readTree(responseString).path("features");

        for (JsonNode featureNode : featuresNode) {
            JsonNode geometryNode = featureNode.path("geometry");
            String type = geometryNode.path("type").asText();

            JsonNode coordinatesNode = geometryNode.path("coordinates");
            addPoisToList(coordinates, type, coordinatesNode);
        }

        mConsumer.accept(coordinates);
    }

    private static void addPoisToList(ArrayList<Poi> coordinates, String type, JsonNode coordinatesNode) {
        if (type.equals("Point")) {
            coordinates.add(new Poi(coordinatesNode.get(1).asDouble(), coordinatesNode.get(0).asDouble()));
        } else if (type.equals("LineString")) {
            for (JsonNode coordinateNode : coordinatesNode)
                coordinates.add(new Poi(coordinateNode.get(1).asDouble(), coordinateNode.get(0).asDouble()));
        }
    }

}
