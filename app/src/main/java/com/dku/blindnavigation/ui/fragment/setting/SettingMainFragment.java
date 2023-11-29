package com.dku.blindnavigation.ui.fragment.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dku.blindnavigation.R;
import com.dku.blindnavigation.utils.VibrationUtil;

public class SettingMainFragment extends SettingFragment {

    private static final String TAG = "SettingMainFragment";

    private boolean isFirstToMainBTClicked = true;
    private VibrationUtil vibrationUtil;

    public static SettingMainFragment newInstance() {
        return new SettingMainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vibrationUtil = new VibrationUtil(getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting_main, container, false);
        setTitle(getString(R.string.setting));
        initButtons(rootView);
        return rootView;
    }

    private void initButtons(View rootView) {
        rootView.<Button>findViewById(R.id.toTtsSpeedBT)
                .setOnClickListener(view -> {
                    ttsHelper.speakString("TTS 속도 설정 메뉴");
                    vibrationUtil.vibrate(300);
                    getRootView().changeFragmentNum(1);
                });

        rootView.<Button>findViewById(R.id.toUsageBT)
                .setOnClickListener(view -> {
                    ttsHelper.speakString("사용법");
                    vibrationUtil.vibrate(300);
                    getRootView().changeFragmentNum(2);
                });

        rootView.<Button>findViewById(R.id.toInquiryBT)
                .setOnClickListener(view -> {
                    ttsHelper.speakString("문의사항");
                    vibrationUtil.vibrate(300);
                    getRootView().changeFragmentNum(3);
                });

        rootView.findViewById(R.id.settingToMainBT)
                .setOnClickListener(view -> {
                    vibrationUtil.vibrate(300);
                    isFirstToMainBTClicked = toMainMenu(isFirstToMainBTClicked);
                });
    }

}