package com.dku.blindnavigation.viewmodel.setting;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.dku.blindnavigation.BlindNavigationApplication;
import com.dku.blindnavigation.repository.TtsDataStore;

public class TtsSettingFragmentViewModel extends AndroidViewModel {

    private final TtsDataStore dataStore;
    private final MutableLiveData<Float> mTtsSpeed;

    public TtsSettingFragmentViewModel(@NonNull Application application) {
        super(application);
        dataStore = ((BlindNavigationApplication) application).getTtsDataStore();
        mTtsSpeed = new MutableLiveData<>(dataStore.getTtsSpeed());
    }

    public MutableLiveData<Float> getTtsSpeed() {
        return mTtsSpeed;
    }

    public void saveTtsSpeed() {
        dataStore.saveTtsSpeed(mTtsSpeed.getValue());
    }

    public void increaseTtsSpeed() {
        if (mTtsSpeed.getValue() >= 2.0f) {
            return;
        }
        mTtsSpeed.setValue(mTtsSpeed.getValue() + 0.1f);
    }

    public void decreaseTtsSpeed() {
        if (mTtsSpeed.getValue() <= 0.1f) {
            return;
        }
        mTtsSpeed.setValue(mTtsSpeed.getValue() - 0.1f);
    }

}