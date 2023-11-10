package com.dku.blindnavigation.utils;

import android.content.Context;
import android.content.Intent;

import com.dku.blindnavigation.ui.activity.MainMenuActivity;


public class IntentUtils {

    private IntentUtils() {
    }

    public static Intent createStartMainActivityIntent(Context context) {
        Intent intent = new Intent(context, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

}
