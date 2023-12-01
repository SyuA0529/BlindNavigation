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
import com.dku.blindnavigation.utils.VibrationUtil;

public class UsageFragment1 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.usage_frame_1, container, false);
        TTSHelper ttsHelper = new TTSHelper(requireActivity());
        VibrationUtil vibrationUtil = new VibrationUtil(requireActivity());
        rootView.setOnClickListener(view -> {
            vibrationUtil.vibrate(300);
            ttsHelper.speakString("네비게이션\n을 연결하는 법" +
                    "1. 블루투스를 활성화시킨다.\n" +
                    "2. 블루투스 장치검색 버튼을 누른다.\n" +
                    "3. 네비게이션 기기 이름이 나오면  '예'  버튼을 누른다. \n" +
                    "4. 연결이 되었으면 이용이 가능하다.");
        });
        return rootView;
    }

}
