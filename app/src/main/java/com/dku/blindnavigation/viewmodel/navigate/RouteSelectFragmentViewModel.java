package com.dku.blindnavigation.viewmodel.navigate;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dku.blindnavigation.repository.navigate.remote.dto.Poi;

import java.util.List;
import java.util.Objects;

public class RouteSelectFragmentViewModel extends AndroidViewModel {

    private static final String TAG = "RouteSelectFragmentViewModel";

    private final MutableLiveData<String> mDestinationName;
    private final MutableLiveData<List<Poi>> mDestinationPois;


    public RouteSelectFragmentViewModel(@NonNull Application application) {
        super(application);
        mDestinationName = new MutableLiveData<>(null);
        mDestinationPois = new MutableLiveData<>(null);
    }

    public LiveData<String> getDestinationName() {
        return mDestinationName;
    }

    public void postDestinationName(String destinationName) {
        mDestinationName.postValue(destinationName);
    }

    public Poi getNextDestinationPoi() {
        List<Poi> value = mDestinationPois.getValue();
        if (Objects.isNull(value) || value.isEmpty()) {
            return null;
        }
        Poi nextDestination = value.get(0);
        value.remove(0);
        return nextDestination;
    }

    public Poi getCurDestinationPoi() {
        List<Poi> value = mDestinationPois.getValue();
        if (Objects.isNull(value) || value.isEmpty()) {
            return null;
        }
        return value.get(0);
    }

    public void postDestinationPois(List<Poi> pois) {
        mDestinationPois.postValue(pois);
    }

    public LiveData<List<Poi>> getDestinationPois() {
        return mDestinationPois;
    }

}
