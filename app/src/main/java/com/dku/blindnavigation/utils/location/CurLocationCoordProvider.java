package com.dku.blindnavigation.utils.location;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.function.Consumer;

public class CurLocationCoordProvider {

    private static final String TAG = "CurLocationCoordProvider";

    private final Consumer<Location> mConsumer;
    private final Context mContext;
    private final FusedLocationProviderClient fusedLocationClient;


    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            Location curLocation = locationResult.getLastLocation();
            if (curLocation == null) {
                return;
            }
            mConsumer.accept(curLocation);
        }
    };

    public CurLocationCoordProvider(Context context, Consumer<Location> consumer) {
        mContext = context;
        mConsumer = consumer;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void startRequestLocation() {
        LocationRequest locationRequest = new LocationRequest
                .Builder(Priority.PRIORITY_HIGH_ACCURACY, 2 * 1000)
                .build();

        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    public void stopRequestLocation() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

}
