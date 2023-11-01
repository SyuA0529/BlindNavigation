package com.dku.blindnavigation.tts;

import android.app.Activity;
import android.speech.tts.TextToSpeech;

import com.dku.blindnavigation.BlindNavigationApplication;

import java.util.Locale;

public class TTSHelper {

    private static final String testString = "TTS 테스트";

    private final TextToSpeech tts;
    private float ttsSpeed;
    private final Activity context;

    public TTSHelper(Activity context) {
        this.context = context;
        this.ttsSpeed = ((BlindNavigationApplication) context.getApplicationContext()).getTtsSpeed();
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
