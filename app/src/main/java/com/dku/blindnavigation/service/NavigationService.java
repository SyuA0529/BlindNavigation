package com.dku.blindnavigation.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.dku.blindnavigation.repository.navigate.remote.dto.Poi;
import com.dku.blindnavigation.ui.activity.NavigateActivity;
import com.dku.blindnavigation.ui.fragment.navigate.NavigateFragment;
import com.dku.blindnavigation.utils.bluetooth.BluetoothHelper;
import com.dku.blindnavigation.utils.direction.DirectionCalculator;
import com.dku.blindnavigation.utils.direction.DirectionType;
import com.dku.blindnavigation.utils.location.CurLocationCoordProvider;
import com.dku.blindnavigation.utils.location.LocationUtils;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class NavigationService extends Service {

    private static final String TAG = "NavigationService";
    public static final String CHANNEL_ID = "BlindNavigationChannel";
    private static final int REACH_DISTANCE = 20;

    private Poi prevLocation;
    private Poi curLocation;
    private Queue<Poi> routes;
    private CurLocationCoordProvider curLocationCoordProvider;
    private BluetoothHelper btHelper;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initForegroundService();
        initCurLocationProvider();

        curLocation = intent.getParcelableExtra(NavigateFragment.ARG_DEPARTURE_LOCATION);
        routes = new LinkedList<>(Objects.requireNonNull(intent.getParcelableArrayListExtra(NavigateFragment.ARG_ROUTE)));

        Poi nextLocation = getNextLocation(curLocation);
        if (Objects.isNull(nextLocation)) {
            arriveDestination();
        } else {
            sendDirection(DirectionCalculator.getFirstDirection(
                    curLocation, nextLocation, intent.getDoubleExtra(NavigateFragment.ARG_DEGREE, 0.0)));
        }


        return START_NOT_STICKY;
    }

    private void initCurLocationProvider() {
        curLocationCoordProvider = new CurLocationCoordProvider(this, this::onGetCurLocation);
        curLocationCoordProvider.startRequestLocation();
    }

    private void initForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        btHelper = new BluetoothHelper(this);
        if (!btHelper.connectBluetoothDevice()) {
            throw new RuntimeException();
        }
        startForeground(1, createNotification());
    }

    @Override
    public void onDestroy() {
        curLocationCoordProvider.stopRequestLocation();
        btHelper.disconnectDevice();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onGetCurLocation(Location location) {
        Poi gpsCurLocation = new Poi(location.getLatitude(), location.getLongitude());
        if (!checkReachNextLocation(gpsCurLocation)) {
            return;
        }
        updateLocation();
        Poi nextLocation = getNextLocation(gpsCurLocation);
        if (Objects.nonNull(nextLocation)) {
            sendNextDirection(nextLocation);
        }
    }

    protected void updateLocation() {
        prevLocation = curLocation;
        curLocation = routes.peek();
    }

    protected boolean checkReachNextLocation(Poi curLocation) {
        Poi nextLocationCoord = routes.peek();
        if (nextLocationCoord == null) {
            arriveDestination();
            return false;
        }

        double distance = LocationUtils.getDistance(curLocation.getFrontLat(), curLocation.getFrontLon(), nextLocationCoord.getFrontLat(), nextLocationCoord.getFrontLon());
        return distance <= REACH_DISTANCE;
    }

    protected void arriveDestination() {
        Toast.makeText(this, "도착", Toast.LENGTH_LONG).show();
        sendArriveIntent();
        stopForeground(true);
        stopSelf();
    }

    private void sendArriveIntent() {
        Intent intent = new Intent("navigation");
        intent.putExtra("message", "arrive");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Nullable
    private Poi getNextLocation(Poi curLocation) {
        while (checkReachNextLocation(curLocation)) {
            routes.poll();
        }
        return routes.peek();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Foreground Service Channel", NotificationManager.IMPORTANCE_HIGH);

        NotificationManager manager = getSystemService(NotificationManager.class);
        assert manager != null;
        manager.createNotificationChannel(serviceChannel);
    }

    private Notification createNotification() {
        Intent notificationIntent = new Intent(this, NavigateActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("시각 장애인용 네비게이션")
                .setContentText("경로 알림 중 입니다")
                .setContentIntent(pendingIntent)
                .build();
    }

    private void sendDirection(DirectionType curLocation) {
        btHelper.sendDirectionToDevice(curLocation);
        Toast.makeText(this, curLocation.toString(), Toast.LENGTH_LONG).show();
    }

    private void sendNextDirection(Poi nextLocation) {
        sendDirection(DirectionCalculator.getNextDirection(prevLocation, curLocation, nextLocation));
    }

}