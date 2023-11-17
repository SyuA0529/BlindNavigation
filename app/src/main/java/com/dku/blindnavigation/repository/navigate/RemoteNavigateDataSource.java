package com.dku.blindnavigation.repository.navigate;

import android.content.Context;

import com.dku.blindnavigation.repository.navigate.remote.CSRClient;
import com.dku.blindnavigation.repository.navigate.remote.destination.DestinationCallback;
import com.dku.blindnavigation.repository.navigate.remote.destination.DestinationClient;
import com.dku.blindnavigation.repository.navigate.remote.dto.Poi;
import com.dku.blindnavigation.repository.navigate.remote.route.RouteCallback;
import com.dku.blindnavigation.repository.navigate.remote.route.RouteClient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class RemoteNavigateDataSource {

    private static final RemoteNavigateDataSource instance = new RemoteNavigateDataSource();
    private final CSRClient csrClient;
    private final DestinationClient destinationClient;
    private final RouteClient routeClient;

    public static RemoteNavigateDataSource getInstance() {
        return instance;
    }

    private RemoteNavigateDataSource() {
        csrClient = CSRClient.getInstance();
        destinationClient = DestinationClient.getInstance();
        routeClient = RouteClient.getInstance();
    }


    public void getDestinationName(Context context, byte[] audio, Consumer<String> consumer) {
        csrClient.sendRequest(context, audio, consumer);
    }

    public void getDestinationPoi(Context context, String destinationName, Consumer<List<Poi>> consumer) {
        destinationClient.requestDestination(context, destinationName, new DestinationCallback(consumer));
    }

    public void getRoute(Context context, Poi startLocation, Poi endLocation, Consumer<ArrayList<Poi>> consumer) {
        routeClient.requestRoute(context, startLocation, endLocation, new RouteCallback(consumer));
    }

}
