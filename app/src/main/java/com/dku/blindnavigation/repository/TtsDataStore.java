package com.dku.blindnavigation.repository;

import android.app.Application;
import android.content.Context;

public class TtsDataStore {

    private static final String TTS_SPEED = "ttsSpeed";
    private static final String STORAGE_NAME = "setting";

    private final Application mApplication;

    public TtsDataStore(Application mApplication) {
        this.mApplication = mApplication;
    }

    public float getTtsSpeed() {
        return mApplication.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)
                .getFloat(TTS_SPEED, 1.0f);
    }

    public void saveTtsSpeed(float ttsSpeed) {
        mApplication.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putFloat(TTS_SPEED, ttsSpeed)
                .apply();
    }

}
