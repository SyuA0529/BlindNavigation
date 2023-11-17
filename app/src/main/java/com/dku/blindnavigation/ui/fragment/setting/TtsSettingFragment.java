package com.dku.blindnavigation.ui.fragment.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.dku.blindnavigation.R;
import com.dku.blindnavigation.viewmodel.setting.TtsSettingFragmentViewModel;

public class TtsSettingFragment extends SettingFragment {

    private TtsSettingFragmentViewModel mViewModel;

    private boolean isFirstSpeedUpBTClicked = true;
    private boolean isFirstSpeedDownBTClicked = true;
    private boolean isFirstSaveBTClicked = true;
    private boolean isFirstToMainBTClicked = true;

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
                        .setOnClickListener(view -> {
                            if (isFirstSpeedUpBTClicked) {
                                ttsHelper.speakString("TTS 속도를 빠르게 하는 버튼입니다");
                                isFirstSpeedUpBTClicked = false;
                                return;
                            }
                            mViewModel.increaseTtsSpeed();
                        });
        rootView.findViewById(R.id.ttsSpeedDown)
                        .setOnClickListener(view -> {
                            if (isFirstSpeedDownBTClicked) {
                                ttsHelper.speakString("TTS 속도를 느리게 하는 버튼입니다");
                                isFirstSpeedDownBTClicked = false;
                                return;
                            }
                            mViewModel.decreaseTtsSpeed();
                        });

        rootView.<Button>findViewById(R.id.testTTSBT)
                .setOnClickListener(view -> ttsHelper.testSpeakString(mViewModel.getTtsSpeed().getValue()));

        rootView.<Button>findViewById(R.id.saveTTSBT)
                .setOnClickListener(view -> {
                    if (isFirstSaveBTClicked) {
                        ttsHelper.speakString("TTS 속도를 저장하는 버튼입니다");
                        isFirstSaveBTClicked = false;
                        return;
                    }
                    mViewModel.saveTtsSpeed();
                    ttsHelper.changeTtsSpeed(mViewModel.getTtsSpeed().getValue());
                    ttsHelper.speakString("설정한 속도를 저장합니다.");
                });

        rootView.<Button>findViewById(R.id.ttsSpeedToMainBT)
                .setOnClickListener(view -> isFirstToMainBTClicked = toMainMenu(isFirstToMainBTClicked));
    }

}