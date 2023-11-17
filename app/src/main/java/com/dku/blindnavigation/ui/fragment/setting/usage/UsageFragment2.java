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
        rootView.setOnClickListener(view -> ttsHelper.speakString("네비게이션 장갑 사용법\n" +
                "1. 서쪽은 검지, 북쪽은 중지, 동쪽은 약지, 남쪽은 소지이다.\n" +
                "2. 한 손가락에 진동이 느껴지면 한 가지 방향을 향한다.\n" +
                "3. 두 손가락에 진동이 느껴지면 대각선 방향을 향한다."));
        return rootView;
    }

}
