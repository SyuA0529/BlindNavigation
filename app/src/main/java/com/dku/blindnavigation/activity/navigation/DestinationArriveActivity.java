package com.dku.blindnavigation.activity.navigation;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.dku.blindnavigation.R;
import com.dku.blindnavigation.activity.utils.IntentUtils;

public class DestinationArriveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_arrive);

        this.<Button>findViewById(R.id.arriveDestToMainBT).setOnClickListener(v ->
                startActivity(IntentUtils.createStartMainActivityIntent(this)));
    }

}