package com.dku.blindnavigation.activity.setting;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.dku.blindnavigation.BlindNavigationApplication;
import com.dku.blindnavigation.R;
import com.dku.blindnavigation.activity.utils.IntentUtils;
import com.dku.blindnavigation.tts.TTSHelper;

public class TTSSettingActivity extends AppCompatActivity {

    private TTSHelper ttsHelper;
    private float ttsSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttssetting);

        initTtsHelper();
        initSeekBar();

        this.<Button>findViewById(R.id.testTTSBT).setOnClickListener(v -> ttsHelper.testSpeakString(ttsSpeed));

        Button button = findViewById(R.id.saveTTSBT);
        button.setOnClickListener(v -> {
            ((BlindNavigationApplication) getApplication()).updateTtsSpeed(ttsSpeed);
            ttsHelper.changeTtsSpeed(ttsSpeed);
        });

        this.<Button>findViewById(R.id.ttsSpeedToMainBT).setOnClickListener(v ->
                startActivity(IntentUtils.createStartMainActivityIntent(this)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ttsHelper = new TTSHelper(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ttsHelper.stopUsing();
    }

    private void initSeekBar() {
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setProgress((int) (ttsSpeed * 10));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ttsSpeed = (float) seekBar.getProgress() / 10;
            }
        });
    }

    private void initTtsHelper() {
        ttsSpeed = getTtsSpeed();
        ttsHelper = new TTSHelper(this);
    }

    private float getTtsSpeed() {
        return (((BlindNavigationApplication) getApplication()).getTtsSpeed());
    }

}