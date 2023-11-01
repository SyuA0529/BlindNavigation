package com.dku.blindnavigation.activity.utils;

import android.content.Context;
import android.content.Intent;

import com.dku.blindnavigation.activity.MainMenuActivity;

public class IntentUtils {

    private IntentUtils() {
    }

    public static Intent createStartMainActivityIntent(Context context) {
        Intent intent = new Intent(context, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

}
