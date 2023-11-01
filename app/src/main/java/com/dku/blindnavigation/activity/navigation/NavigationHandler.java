package com.dku.blindnavigation.activity.navigation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.dku.blindnavigation.navigation.location.gps.CurLocationCoordProvider;
import com.dku.blindnavigation.navigation.location.gps.LocationNameProvider;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class NavigationHandler extends Handler {

    private final WeakReference<NavigationActivity> reference;

    public NavigationHandler(NavigationActivity activity) {
        super(Looper.getMainLooper());
        this.reference = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        NavigationActivity activity = reference.get();
        Bundle msgData = msg.getData();
        int eventType = msgData.getInt("eventType");
        if (!msgData.getBoolean("status")) {
            return;
        }

        switch (eventType) {
            case LocationNameProvider.EVENT_TYPE:
                String curLocationName = Objects.requireNonNull(msgData.getString("departureName"));
                Log.d("NavigationActivity", curLocationName);
                activity.speakLocationName(curLocationName);
                break;
            case CurLocationCoordProvider.EVENT_TYPE:
                activity.changeCurLocationCoord(msgData.getParcelable("curLocationCoord"));
        }
    }

}
