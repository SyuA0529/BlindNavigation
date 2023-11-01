package com.dku.blindnavigation.activity.setting;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.dku.blindnavigation.R;
import com.dku.blindnavigation.activity.utils.IntentUtils;

public class InquiryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        this.<Button>findViewById(R.id.toMainBT).setOnClickListener(v ->
                startActivity(IntentUtils.createStartMainActivityIntent(this)));
    }
}