package com.dku.blindnavigation.ui.fragment.setting.usage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dku.blindnavigation.R;
import com.dku.blindnavigation.utils.TTSHelper;

public class UsageFragment2 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.usage_frame_2, container, false);
        TTSHelper ttsHelper = new TTSHelper(requireActivity());
        rootView.setOnClickListener(view -> {
//            ttsHelper.speakString();
        });
        return rootView;
    }

}
