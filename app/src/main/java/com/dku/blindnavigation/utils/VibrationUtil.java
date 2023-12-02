package com.dku.blindnavigation.utils;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class VibrationUtil{

    private final Vibrator vibrator;

    public VibrationUtil(Context context) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void vibrate(long milliseconds) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(
                    milliseconds,
                    VibrationEffect.DEFAULT_AMPLITUDE
            ));
        } else {
            // deprecated in API 26
            vibrator.vibrate(milliseconds);
        }
    }
}
