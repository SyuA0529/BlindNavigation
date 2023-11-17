package com.dku.blindnavigation.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dku.blindnavigation.R;
import com.dku.blindnavigation.utils.PermissionUtils;
import com.dku.blindnavigation.utils.TTSHelper;

import java.util.Arrays;

public class MainMenuActivity extends AppCompatActivity {

    private static final String TAG = "MainMenuActivity";
    private static final int PERMISSION_CODE = 101;

    private TTSHelper ttsHelper;
    private boolean locationPermGranted = false;
    private boolean backgroundPermGranted = false;
    private boolean audioPermGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        locationPermGranted = PermissionUtils.checkLocationPermissions(this);
        if (locationPermGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                backgroundPermGranted = PermissionUtils.checkBackgroundLocationPermissions(this);
                audioPermGranted = PermissionUtils.checkAudioPermissions(this);
            }
        }

        initButtons();
    }

    private void initButtons() {
        this.<Button>findViewById(R.id.routeGuideBT).setOnClickListener(v -> {
            ttsHelper.speakString("경로 안내 메뉴입니다.");
            if (locationPermGranted && backgroundPermGranted && audioPermGranted) {
                startActivity(new Intent(this, NavigateActivity.class));
                return;
            }
            ttsHelper.speakString("위치 권한이 허용되지 않았습니다");
        });

        this.<Button>findViewById(R.id.btConnectBT).setOnClickListener(v ->{
            ttsHelper.speakString("블루투스 연결 메뉴입니다.");
            startActivity(new Intent(this, BluetoothConnectActivity.class));
        });

        this.<Button>findViewById(R.id.settingBT).setOnClickListener(v -> {
            ttsHelper.speakString("환경설정 메뉴입니다.");
            startActivity(new Intent(this, SettingActivity.class));
        });

        this.<Button>findViewById(R.id.exitBT).setOnClickListener(v ->
                System.exit(0));
    }

    @Override
    protected void onResume() {
        ttsHelper = new TTSHelper(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ttsHelper.stopUsing();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != PERMISSION_CODE) {
            return;
        }
        Log.d(TAG, "onRequestPermissionsResult: " + Arrays.toString(permissions));

        for (int index = 0; index < permissions.length; index++) {
            if (grantResults[index] != PackageManager.PERMISSION_DENIED) {
                if (permissions[index].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    locationPermGranted = true;
                    backgroundPermGranted = PermissionUtils.checkBluetoothPermissions(this);
                } else if (permissions[index].equals(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    backgroundPermGranted = true;
                    audioPermGranted = PermissionUtils.checkAudioPermissions(this);
                } else if (permissions[index].equals(Manifest.permission.RECORD_AUDIO)) {
                    audioPermGranted = true;
                }
            }
        }
    }

}