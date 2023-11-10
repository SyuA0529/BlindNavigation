package com.dku.blindnavigation;

import android.app.Application;

import com.dku.blindnavigation.repository.TtsDataStore;
import com.dku.blindnavigation.utils.TTSHelper;

public class BlindNavigationApplication extends Application {

    private TtsDataStore mTtsDataStore;

    public TtsDataStore getTtsDataStore() {
        return mTtsDataStore;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTtsDataStore = new TtsDataStore(this);
        TTSHelper.initDataStore(mTtsDataStore);
    }

}