package com.dku.blindnavigation.ui.fragment.navigate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dku.blindnavigation.R;
import com.dku.blindnavigation.utils.TTSHelper;
import com.dku.blindnavigation.viewmodel.navigate.NavigateActivityViewModel;

public class DestinationArriveFragment extends Fragment {

    private static final String TAG = "DestinationArriveFragment";
    private TTSHelper ttsHelper;

    public static DestinationArriveFragment newInstance() {
        DestinationArriveFragment fragment = new DestinationArriveFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_destination_arrive, container, false);
        ttsHelper = new TTSHelper(requireActivity());
        new ViewModelProvider(requireActivity()).get(NavigateActivityViewModel.class)
                .changeTitle("목적지 도착");
        rootView.findViewById(R.id.arriveDestToMainBT)
                .setOnClickListener(view -> {
                    ttsHelper.speakString("목적지에 도착하였습니다.");
                    requireActivity().finish();
                });
        return rootView;
    }
}