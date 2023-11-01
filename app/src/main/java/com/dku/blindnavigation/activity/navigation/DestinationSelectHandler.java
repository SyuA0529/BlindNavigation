package com.dku.blindnavigation.activity.navigation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.dku.blindnavigation.navigation.direction.OrientationListener;
import com.dku.blindnavigation.navigation.location.destination.DestinationListener;
import com.dku.blindnavigation.navigation.location.gps.CurLocationCoordProvider;
import com.dku.blindnavigation.navigation.location.gps.LocationNameProvider;
import com.dku.blindnavigation.navigation.route.RouteListener;

import java.lang.ref.WeakReference;

public class DestinationSelectHandler extends Handler {

    private final WeakReference<DestinationSelectActivity> reference;

    public DestinationSelectHandler(DestinationSelectActivity activity) {
        super(Looper.getMainLooper());
        this.reference = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        DestinationSelectActivity activity = reference.get();
        Bundle msgData = msg.getData();
        int eventType = msgData.getInt("eventType");

        if (eventType == OrientationListener.EVENT_TYPE) {
            activity.onGetPhoneDegree(msgData.getDouble("degree"));
            return;
        }

        boolean status = msgData.getBoolean("status");
        switch (eventType) {
            case DestinationListener.EVENT_TYPE:
                handleDestinationPois(activity, msgData, status);

            case CurLocationCoordProvider.EVENT_TYPE: //Departure Coordinate
                handleDepartureCoord(activity, msgData, status);
                break;

            case LocationNameProvider.EVENT_TYPE: //Departure Name
                handleDepartureName(activity, msgData, status);
                break;

            case RouteListener.EVENT_TYPE: //Route
                handleRoute(activity, msgData, status);
                break;
        }
    }

    private static void handleDestinationPois(DestinationSelectActivity activity, Bundle msgData, boolean status) {
        if (status) {
            activity.onSuccessGetDestinationPois(msgData.getParcelableArrayList("pois"));
            return;
        }
        activity.onFailure("목적지를 검색할 수 없습니다");
    }

    private static void handleDepartureCoord(DestinationSelectActivity activity, Bundle msgData, boolean status) {
        if (status) {
            activity.onSuccessGetDepartureCoord(msgData.getParcelable("curLocationCoord"));
            return;
        }
        activity.onFailure("현재 위치 청보를 가져올 수 없습니다");
    }

    private static void handleDepartureName(DestinationSelectActivity activity, Bundle msgData, boolean status) {
        if (status) {
            activity.onSuccessGetDepartureName(msgData.getString("departureName"));
            return;
        }
        activity.onFailure("현재 주변의 건물의 이름을 가져올 수 없습니다");
    }

    private static void handleRoute(DestinationSelectActivity activity, Bundle msgData, boolean status) {
        if (status) {
            activity.onSuccessGetRoute(msgData.getParcelableArrayList("route"));
            return;
        }
        activity.onFailure("목적지 까지의 경로를 가져올 수 없습니다");
    }

}
