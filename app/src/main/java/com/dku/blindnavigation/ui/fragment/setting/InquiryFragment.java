package com.dku.blindnavigation.ui.fragment.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.dku.blindnavigation.R;

public class InquiryFragment extends SettingFragment {

    public static InquiryFragment newInstance() {
        return new InquiryFragment();
    }

    private boolean isFirstToMainBTClicked = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setTitle(getString(R.string.usage));
        View rootView = inflater.inflate(R.layout.fragment_inquiry, container, false);
        rootView.findViewById(R.id.inquiryToMainBT)
                .setOnClickListener(view -> isFirstToMainBTClicked = toMainMenu(isFirstToMainBTClicked));
        return rootView;
    }

}