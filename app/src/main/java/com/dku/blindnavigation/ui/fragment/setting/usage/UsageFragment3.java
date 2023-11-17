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

public class UsageFragment3 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.usage_frame_3, container, false);
        TTSHelper ttsHelper = new TTSHelper(requireActivity());
        rootView.setOnClickListener(view -> ttsHelper.speakString("TTS 조절 기능\n" +
                "1. TTS의 속도 조절은 방향키로 진행한다.\n" +
                "2. 사용자의 설정에 맞게 0.1배속에서 2배속까지 조절이 가능하다.\n" +
                "3. 조절을 완료한 후 저장을 누르면 해당 속도가 적용된다.\n" +
                "4. 같은 방식으로 속도 변경 후 적용하면 된다."));
        return rootView;
    }

}
