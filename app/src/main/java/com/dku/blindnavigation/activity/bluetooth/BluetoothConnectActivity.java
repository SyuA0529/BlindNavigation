package com.dku.blindnavigation.activity.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.dku.blindnavigation.BlindNavigationApplication;
import com.dku.blindnavigation.PermissionUtils;
import com.dku.blindnavigation.R;
import com.dku.blindnavigation.activity.utils.IntentUtils;
import com.dku.blindnavigation.bluetooth.BluetoothHelper;
import com.dku.blindnavigation.tts.TTSHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class BluetoothConnectActivity extends AppCompatActivity {

    private TTSHelper ttsHelper;
    private BluetoothHelper btHelper;
    private final Set<BluetoothDevice> btDeviceSet = new HashSet<>();
    private List<BluetoothDevice> btDevices;
    private final Handler handler = new Handler();
    private boolean scanFinish = false;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connect);

        PermissionUtils.checkBluetoothPermissions(this);

        btHelper = new BluetoothHelper(this);

        this.<Button>findViewById(R.id.searchBTDeviceBT).setOnClickListener(v -> {
            if (!PermissionUtils.checkBluetoothPermissions(this)) {
                ttsHelper.speakString("권한이 존재하지 않습니다");
                return;
            }
            ttsHelper.speakString("장치 검색을 시작합니다.");
            btHelper.startDiscovery();
            handler.postDelayed(this::finishBTScan, 15000);
        });

        this.<Button>findViewById(R.id.btDeviceYesBT).setOnClickListener(v -> {
            if (checkDevicesNotInitialize()) return;

            setBluetoothMacAddr(btDevices.get(0));
            startActivity(IntentUtils.createStartMainActivityIntent(this));
        });

        this.<Button>findViewById(R.id.btDeviceNoBT).setOnClickListener(v -> {
            if (checkDevicesNotInitialize()) {
                return;
            }
            btDevices.remove(0);
            if(!btDevices.isEmpty()) {
                ttsHelper.speakString(btDevices.get(0).getName());
            }
        });

        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    @SuppressLint("MissingPermission")
    private void finishBTScan() {
        btDevices = new ArrayList<>(btDeviceSet);
        scanFinish = true;
        ttsHelper.speakString("장치 검색이 끝났습니다");

        for (BluetoothDevice bluetoothDevice : btDeviceSet) {
            Log.d("BluetoothConnectActivity", bluetoothDevice.getName());
        }
        if (!btDevices.isEmpty()) {
            ttsHelper.speakString(btDevices.get(0).getName());
        }
        Log.d("BluetoothConnectActivity", "finishBTScan called");
    }

    @SuppressLint("MissingPermission")
    private void setBluetoothMacAddr(BluetoothDevice bluetoothDevice) {
        SharedPreferences.Editor edit = getSharedPreferences("setting", MODE_PRIVATE).edit();
        edit.putString("MAC_ADDR", bluetoothDevice.getAddress());
        edit.apply();
    }

    private boolean checkDevicesNotInitialize() {
        if (!scanFinish) {
            ttsHelper.speakString("장치 검색 중입니다");
            return true;
        }

        if (btDevices == null || btDevices.isEmpty()) {
            ttsHelper.speakString("검색된 장치가 없습니다");
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ttsHelper = new TTSHelper(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @SuppressLint("MissingPermission")
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (Objects.nonNull(device.getName())) {
                    btDeviceSet.add(device);
                }
            }
        }
    };

}