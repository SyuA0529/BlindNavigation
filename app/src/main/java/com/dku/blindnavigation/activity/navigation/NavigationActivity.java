package com.dku.blindnavigation.activity.navigation;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.dku.blindnavigation.BlindNavigationApplication;
import com.dku.blindnavigation.R;
import com.dku.blindnavigation.activity.utils.IntentUtils;
import com.dku.blindnavigation.navigation.dto.Poi;
import com.dku.blindnavigation.navigation.location.gps.CurLocationCoordProvider;
import com.dku.blindnavigation.navigation.location.gps.LocationNameProvider;
import com.dku.blindnavigation.service.NavigationService;
import com.dku.blindnavigation.tts.TTSHelper;

import java.util.Objects;

public class NavigationActivity extends AppCompatActivity {

    private final Handler handler = new NavigationHandler(this);
    private TTSHelper ttsHelper;
    private CurLocationCoordProvider curLocationCoordProvider;
    private LocationNameProvider locationNameProvider;
    private Poi curLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        this.ttsHelper = new TTSHelper(this);

        curLocationCoordProvider = new CurLocationCoordProvider(this, handler);
        locationNameProvider = new LocationNameProvider(handler);

        this.<Button>findViewById(R.id.nearLocationNameBT).setOnClickListener(v ->
                locationNameProvider.getDepartureInfo(this, curLocation.getFrontLat(), curLocation.getFrontLon()));

        this.<Button>findViewById(R.id.stopGuideBT).setOnClickListener(v -> stopNavigate());

        startNavigate();
    }

    private void startNavigate() {
        Intent intent = new Intent(this, NavigationService.class);
        intent.putExtras(Objects.requireNonNull(getIntent().getExtras()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    private void stopNavigate() {
        stopService(new Intent(this, NavigationService.class));
        startActivity(IntentUtils.createStartMainActivityIntent(this));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        curLocationCoordProvider.startRequestLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        curLocationCoordProvider.stopRequestLocation();
    }

    @Override
    public void onBackPressed() {
        stopNavigate();
    }

    public void speakLocationName(String locationName) {
        ttsHelper.speakString(locationName);
    }

    public void changeCurLocationCoord(Poi curLocation) {
        this.curLocation = curLocation;
    }

}