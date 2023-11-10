package com.dku.blindnavigation.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.dku.blindnavigation.repository.TtsDataStore;

import java.util.Locale;

public class TTSHelper {

    private static final String testString = "TTS 테스트";
    private static TtsDataStore mDataStore;

    private final TextToSpeech tts;
    private float ttsSpeed;

    public static void initDataStore(TtsDataStore dataStore) {
        mDataStore = dataStore;
    }

    public TTSHelper(Context context) {
        this.ttsSpeed = mDataStore.getTtsSpeed();
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS)
                    tts.setLanguage(Locale.KOREAN);
            }
        });
    }

    private void speakString(String string, float ttsSpeed) {
        tts.setPitch(1.0f);
        tts.setSpeechRate(ttsSpeed);
        tts.speak(string, TextToSpeech.QUEUE_FLUSH, null, string);
    }

    public void speakString(String string) {
        speakString(string, this.ttsSpeed);
    }

    public void testSpeakString(float ttsSpeed) {
        speakString(testString, ttsSpeed);
    }

    public void changeTtsSpeed(float ttsSpeed) {
        this.ttsSpeed = ttsSpeed;
    }

    public void stopUsing() {
        tts.stop();
        tts.shutdown();
    }

    @Override
    protected void finalize() throws Throwable {
        stopUsing();
        super.finalize();
    }

}
