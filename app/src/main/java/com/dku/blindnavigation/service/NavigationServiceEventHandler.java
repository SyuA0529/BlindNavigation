package com.dku.blindnavigation.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.dku.blindnavigation.navigation.dto.Poi;

import java.lang.ref.WeakReference;

public class NavigationServiceEventHandler extends Handler {

    private final WeakReference<NavigationService> reference;

    public NavigationServiceEventHandler(NavigationService service) {
        super(Looper.getMainLooper());
        this.reference = new WeakReference<>(service);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        NavigationService service = reference.get();
        Bundle msgData = msg.getData();
        if (!msgData.getBoolean("status")) {
            return;
        }

        Poi gpsCurLocation = msgData.getParcelable("curLocationCoord");
        if (!service.checkReachNextLocation(gpsCurLocation)) {
            return;
        }

        service.updateLocation();

        Poi nextLocation = service.getNextLocation(gpsCurLocation);
        assert nextLocation != null;
        service.sendNextDirection(nextLocation);
    }

}
