package com.dku.blindnavigation.viewmodel.setting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingActivityViewModel extends ViewModel {

    private final MutableLiveData<Integer> mFragmentNum;
    private final MutableLiveData<String> mTitle;

    public SettingActivityViewModel() {
        mFragmentNum = new MutableLiveData<>(0);
        mTitle = new MutableLiveData<>();
    }

    public LiveData<Integer> getFragmentNum() {
        return mFragmentNum;
    }
    public LiveData<String> getTitle() {
        return mTitle;
    }

    public void changeFragmentNum(int fragmentNum) {
        mFragmentNum.setValue(fragmentNum);
    }
    public void changeTitle(String title) {
        mTitle.setValue(title);
    }

}
