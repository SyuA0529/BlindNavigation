package com.dku.blindnavigation.utils.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class LocationNameProvider {

    private static final String TAG = "LocationNameProvider";

    private final Consumer<String> mConsumer;

    public LocationNameProvider(Consumer<String> consumer) {
        mConsumer = consumer;
    }

    public void getDepartureInfo(Context context, double latitude, double longitude) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            try {
                String departureName = LocationUtils.getLocationNameByCoord(context, latitude, longitude);
                if (Objects.isNull(departureName)) {
                    return;
                }
                mConsumer.accept(departureName);
            } catch (IOException ignored) {
            }
        } else {
            LocationUtils.getLocationNameByCoord(context, latitude, longitude, new DepartureListener());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public class DepartureListener implements Geocoder.GeocodeListener {
        @Override
        public void onGeocode(@NonNull List<Address> addresses) {
            if (addresses.isEmpty()) {
                return;
            }

            String addressLine = null;
            for (Address location : addresses) {
                addressLine = location.getAddressLine(0);
                if (addressLine != null) {
                    break;
                }
            }
            if (Objects.nonNull(addressLine)) {
                mConsumer.accept(addressLine);
            }
        }

        @Override
        public void onError(@Nullable String errorMessage) {
            Geocoder.GeocodeListener.super.onError(errorMessage);
        }
    }

}
