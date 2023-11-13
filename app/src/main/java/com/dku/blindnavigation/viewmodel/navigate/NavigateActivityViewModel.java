package com.dku.blindnavigation.viewmodel.navigate;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dku.blindnavigation.repository.navigate.remote.dto.Poi;

import java.util.ArrayList;
import java.util.Objects;

public class NavigateActivityViewModel extends ViewModel {

    private static final String TAG = "NavigateActivityViewModel";

    private final MutableLiveData<String> mTitle;
    private final MutableLiveData<Poi> mDestinationPoi;
    private final MutableLiveData<Location> mCurLocation;
    private final MutableLiveData<String> mCurLocationName;
    private final MutableLiveData<Double> mPhoneDegree;
    private final MutableLiveData<ArrayList<Poi>> mRoute;

    public NavigateActivityViewModel() {
        mTitle = new MutableLiveData<>("Stub");
        mDestinationPoi = new MutableLiveData<>(null);
        mCurLocation = new MutableLiveData<>(null);
        mCurLocationName = new MutableLiveData<>(null);
        mPhoneDegree = new MutableLiveData<>(null);
        mRoute = new MutableLiveData<>(null);
    }

    public LiveData<String> getTitle() {
        return mTitle;
    }

    public void changeTitle(String title) {
        mTitle.setValue(title);
    }

    public LiveData<Poi> getDestinationPoi() {
        return mDestinationPoi;
    }

    public void postDestinationPoi(Poi curDestinationPoi) {
        mDestinationPoi.postValue(curDestinationPoi);
    }

    public LiveData<Location> getCurLocation() {
        return mCurLocation;
    }

    public void postCurLocation(Location location) {
        mCurLocation.postValue(location);
    }

    public LiveData<String> getCurLocationName() {
        return mCurLocationName;
    }

    public void postCurLocationName(String name) {
        mCurLocationName.postValue(name);
    }

    public boolean isReadyToGetRoute() {
        return Objects.nonNull(mDestinationPoi.getValue()) &&
                Objects.nonNull(mCurLocation.getValue()) &&
                Objects.nonNull(mCurLocationName.getValue()) &&
                Objects.nonNull(mPhoneDegree.getValue());
    }

    public LiveData<Double> getPhoneDegree() {
        return mPhoneDegree;
    }

    public void postPhoneDegree(double phoneDegree) {
        mPhoneDegree.postValue(phoneDegree);
    }

    public LiveData<ArrayList<Poi>> getRoute() {
        return mRoute;
    }

    public void postRoute(ArrayList<Poi> route) {
        mRoute.postValue(route);
    }

}
