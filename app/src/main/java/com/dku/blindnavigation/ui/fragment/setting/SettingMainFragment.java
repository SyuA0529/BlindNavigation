package com.dku.blindnavigation.ui.fragment.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dku.blindnavigation.R;

public class SettingMainFragment extends SettingFragment {

    private static final String TAG = "SettingMainFragment";

    public static SettingMainFragment newInstance() {
        return new SettingMainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting_main, container, false);
        setTitle(getString(R.string.setting));
        initButtons(rootView);
        return rootView;
    }

    private void initButtons(View rootView) {
        rootView.<Button>findViewById(R.id.toTtsSpeedBT)
                .setOnClickListener(view -> getRootView().changeFragmentNum(1));

        rootView.<Button>findViewById(R.id.toUsageBT)
                .setOnClickListener(view -> getRootView().changeFragmentNum(2));

        rootView.<Button>findViewById(R.id.toInquiryBT)
                .setOnClickListener(view -> getRootView().changeFragmentNum(3));

        rootView.findViewById(R.id.settingToMainBT)
                .setOnClickListener(view -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
    }

}