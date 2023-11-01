package com.dku.blindnavigation.activity.navigation;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.dku.blindnavigation.BlindNavigationApplication;
import com.dku.blindnavigation.R;
import com.dku.blindnavigation.navigation.direction.OrientationListener;
import com.dku.blindnavigation.navigation.dto.Poi;
import com.dku.blindnavigation.navigation.location.LocationUtils;
import com.dku.blindnavigation.navigation.location.destination.DestinationCallback;
import com.dku.blindnavigation.navigation.location.destination.DestinationListener;
import com.dku.blindnavigation.navigation.location.gps.CurLocationCoordProvider;
import com.dku.blindnavigation.navigation.location.gps.LocationNameProvider;
import com.dku.blindnavigation.navigation.route.RouteCallback;
import com.dku.blindnavigation.navigation.route.RouteListener;
import com.dku.blindnavigation.tts.TTSHelper;

import java.util.ArrayList;
import java.util.List;

public class DestinationSelectActivity extends AppCompatActivity {

    private final Handler handler = new DestinationSelectHandler(this);
    private final DestinationCallback destinationCallback = new DestinationCallback();
    private final RouteCallback routeCallback = new RouteCallback();
    private final Bundle bundle = new Bundle();

    private EditText inputDestinationEditTV;
    private TTSHelper ttsHelper;
    private OrientationListener orientationListener;
    private CurLocationCoordProvider curLocationCoordProvider;
    private LocationNameProvider locationNameProvider;
    private Poi departureLocation;
    private List<Poi> destinationLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_select);
        this.inputDestinationEditTV = findViewById(R.id.inputDestination);

        initListeners();
        initButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.ttsHelper = new TTSHelper(this);
        this.orientationListener.registerSensorListeners();
        this.curLocationCoordProvider.startRequestLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.ttsHelper.stopUsing();
        this.orientationListener.unregisterSensorListeners();
        this.curLocationCoordProvider.stopRequestLocation();
    }

    private void initListeners() {
        this.destinationCallback.addListener(new DestinationListener(this.handler));
        this.orientationListener = new OrientationListener((SensorManager) getSystemService(SENSOR_SERVICE), this.handler);
        this.curLocationCoordProvider = new CurLocationCoordProvider(this, this.handler);
        this.locationNameProvider = new LocationNameProvider(this.handler);
        this.routeCallback.addListener(new RouteListener(this.handler));
    }

    private void initButtons() {
        this.<Button>findViewById(R.id.findDestinationBT).setOnClickListener(v ->
                LocationUtils.getLocationInfoByName(this.inputDestinationEditTV.getText().toString(),
                        this, this.destinationCallback)
        );

        this.<Button>findViewById(R.id.selectDestinationBT).setOnClickListener(v -> {
            if (this.departureLocation == null) {
                this.ttsHelper.speakString("출발지 정보를 가져오지 못했습니다");
                return;
            }
            Poi destinationLocation = this.destinationLocations.get(0);
            LocationUtils.getRoute(this.departureLocation, destinationLocation, this, this.routeCallback);
        });

        this.<Button>findViewById(R.id.nextDestinationBT).setOnClickListener(v -> {
            if (this.destinationLocations == null || this.destinationLocations.isEmpty()) {
                this.ttsHelper.speakString("일치하는 목적지가 없습니다");
                return;
            }
            this.destinationLocations.remove(0);
            this.ttsHelper.speakString(this.destinationLocations.get(0).getName());
        });

        this.<Button>findViewById(R.id.selectDestToMainBT).setOnClickListener(v -> finish());
    }

    protected void onGetPhoneDegree(double degree) {
        this.bundle.putDouble("degree", degree);
    }

    protected void onSuccessGetDestinationPois(List<Poi> pois) {
        this.destinationLocations = pois;
        this.ttsHelper.speakString(this.destinationLocations.get(0).getName());
    }

    protected void onSuccessGetDepartureCoord(Poi departureLocation) {
        if (departureLocation == null) {
            return;
        }
        this.departureLocation = departureLocation;
        this.bundle.putParcelable("departureLocation", departureLocation);
        this.curLocationCoordProvider.stopRequestLocation();
        this.locationNameProvider.getDepartureInfo(
                this, this.departureLocation.getFrontLat(), this.departureLocation.getFrontLon());
    }

    protected void onSuccessGetDepartureName(String departureName) {
        this.departureLocation.setName(departureName);
    }

    protected void onSuccessGetRoute(ArrayList<Poi> route) {
        this.bundle.putParcelableArrayList("route", route);
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtras(this.bundle);
        startActivity(intent);
    }

    protected void onFailure(String speakString) {
        ttsHelper.speakString(speakString);
    }

}