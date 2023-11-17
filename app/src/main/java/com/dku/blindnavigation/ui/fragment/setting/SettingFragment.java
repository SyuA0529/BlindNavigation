package com.dku.blindnavigation.ui.fragment.setting;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dku.blindnavigation.utils.TTSHelper;
import com.dku.blindnavigation.viewmodel.setting.SettingActivityViewModel;

public class SettingFragment extends Fragment {

    private static final String TAG = "SettingFragment";

    protected TTSHelper ttsHelper;
    private OnBackPressedCallback callback;
    private SettingActivityViewModel rootViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootViewModel = new ViewModelProvider(requireActivity()).get(SettingActivityViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        throw new RuntimeException("Stub!");
    }

    protected SettingActivityViewModel getRootView() {
        return rootViewModel;
    }

    protected void setTitle(String title) {
        rootViewModel.changeTitle(title);
    }

    protected boolean toMainMenu(boolean isFirstToMainBTClicked) {
        if (isFirstToMainBTClicked) {
            ttsHelper.speakString("메인 메뉴로 이동하는 버튼입니다");
        } else {
            requireActivity().finish();
        }
        return false;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ttsHelper = new TTSHelper(context);

        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (rootViewModel.getFragmentNum().getValue() != 0) {
                    rootViewModel.changeFragmentNum(0);
                    return;
                }
                requireActivity().finish();
            }
        };

        requireActivity().getOnBackPressedDispatcher()
                .addCallback(callback);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ttsHelper.stopUsing();
        callback.remove();
    }

}