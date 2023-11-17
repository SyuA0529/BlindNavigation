package com.dku.blindnavigation.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.dku.blindnavigation.R;
import com.dku.blindnavigation.repository.navigate.RemoteNavigateDataSource;
import com.dku.blindnavigation.repository.navigate.remote.dto.Poi;
import com.dku.blindnavigation.ui.fragment.navigate.DestinationArriveFragment;
import com.dku.blindnavigation.ui.fragment.navigate.DestinationSelectFragment;
import com.dku.blindnavigation.ui.fragment.navigate.NavigateFragment;
import com.dku.blindnavigation.utils.TTSHelper;
import com.dku.blindnavigation.utils.location.CurLocationCoordProvider;
import com.dku.blindnavigation.utils.location.LocationNameProvider;
import com.dku.blindnavigation.viewmodel.navigate.NavigateActivityViewModel;

import java.util.Objects;

public class NavigateActivity extends AppCompatActivity {

    private static final String TAG = "NavigateActivity";

    private TTSHelper ttsHelper;
    private FragmentManager fragmentManager;
    private CurLocationCoordProvider curLocationCoordProvider;
    private LocationNameProvider locationNameProvider;
    private NavigateActivityViewModel viewModel;
    private DestinationSelectFragment destinationSelectFragment =
            DestinationSelectFragment.newInstance();
    private DestinationArriveFragment destinationArriveFragment =
            DestinationArriveFragment.newInstance();
    private RemoteNavigateDataSource remoteNavigateDataSource;

    private final BroadcastReceiver arriveBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("message").equals("arrive")) {
                changeFragment(destinationArriveFragment);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        ttsHelper = new TTSHelper(this);
        fragmentManager = getSupportFragmentManager();
        remoteNavigateDataSource = RemoteNavigateDataSource.getInstance();
        changeFragment(destinationSelectFragment);

        viewModel = new ViewModelProvider(this).get(NavigateActivityViewModel.class);
        viewModel.getTitle().observe(this, this::setTitle);
        initCurLocationCoordProvider();
        initCurLocationNameProvider();
        initRouteProvider();
        initRouteObserver();

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(arriveBroadcastReceiver, new IntentFilter("navigation"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        curLocationCoordProvider.startRequestLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        curLocationCoordProvider.stopRequestLocation();
    }

    private void initRouteObserver() {
        viewModel.getRoute().observe(this, pois -> {
            if (Objects.isNull(pois)) {
                return;
            }
            changeFragment(NavigateFragment.newInstance(getDeparturePoi(),
                    viewModel.getRoute().getValue(), viewModel.getPhoneDegree().getValue()));
        });
    }

    private void initCurLocationCoordProvider() {
        curLocationCoordProvider = new CurLocationCoordProvider(this, viewModel::postCurLocation);
    }

    private void initCurLocationNameProvider() {
        locationNameProvider = new LocationNameProvider(viewModel::postCurLocationName);
        viewModel.getCurLocation()
                .observe(this, location -> {
                    if (Objects.isNull(location)) {
                        return;
                    }
                    locationNameProvider.getDepartureInfo(this,
                            location.getLatitude(), location.getLongitude());
                });
    }

    private void initRouteProvider() {
        viewModel.getDestinationPoi().observe(this, destinationPoi -> {
            if (!viewModel.isReadyToGetRoute()) {
                ttsHelper.speakString("경로 안내에 필요한 정보가 부족합니다");
                return;
            }

            remoteNavigateDataSource.getRoute(this, getDeparturePoi(), viewModel.getDestinationPoi().getValue(), viewModel::postRoute);
        });
    }

    @NonNull
    private Poi getDeparturePoi() {
        Location departureLocation = viewModel.getCurLocation().getValue();
        String departureName = viewModel.getCurLocationName().getValue();
        return new Poi(departureName, departureLocation.getLatitude(), departureLocation.getLongitude());
    }

    private void changeFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.routeFragmentLayout, fragment)
                .commit();
    }

    private void setTitle(String title) {
        this.<TextView>findViewById(R.id.routeTitleTV).setText(title);
    }

}