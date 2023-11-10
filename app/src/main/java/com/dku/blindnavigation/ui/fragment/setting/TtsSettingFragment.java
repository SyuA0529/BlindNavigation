package com.dku.blindnavigation.ui.fragment.setting;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dku.blindnavigation.R;
import com.dku.blindnavigation.viewmodel.setting.TtsSettingFragmentViewModel;

public class TtsSettingFragment extends SettingFragment {

    private TtsSettingFragmentViewModel mViewModel;

    public static TtsSettingFragment newInstance() {
        return new TtsSettingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TtsSettingFragmentViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tts_setting, container, false);
        setTitle(getString(R.string.ttsSpeed));
        initButtons(rootView);
        return rootView;
    }

    /** @noinspection DataFlowIssue*/
    private void initButtons(View rootView) {
        rootView.findViewById(R.id.ttsSpeedUp)
                        .setOnClickListener(view -> mViewModel.increaseTtsSpeed());
        rootView.findViewById(R.id.ttsSpeedDown)
                        .setOnClickListener(view -> mViewModel.decreaseTtsSpeed());

        rootView.<Button>findViewById(R.id.testTTSBT)
                .setOnClickListener(view -> ttsHelper.testSpeakString(mViewModel.getTtsSpeed().getValue()));

        rootView.<Button>findViewById(R.id.saveTTSBT)
                .setOnClickListener(view -> {
                    mViewModel.saveTtsSpeed();
                    ttsHelper.changeTtsSpeed(mViewModel.getTtsSpeed().getValue());
                });

        rootView.<Button>findViewById(R.id.ttsSpeedToMainBT)
                .setOnClickListener(view -> toMainMenu());
    }

}