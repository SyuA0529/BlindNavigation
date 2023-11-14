package com.dku.blindnavigation.ui.fragment.navigate;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dku.blindnavigation.R;
import com.dku.blindnavigation.repository.navigate.remote.dto.Poi;
import com.dku.blindnavigation.service.NavigationService;
import com.dku.blindnavigation.utils.TTSHelper;
import com.dku.blindnavigation.viewmodel.navigate.NavigateActivityViewModel;

import java.util.ArrayList;

public class NavigateFragment extends Fragment {

    public static final String ARG_DEPARTURE_LOCATION = "DEPARTURE_LOCATION";
    public static final String ARG_ROUTE = "ROUTE";
    public static final String ARG_DEGREE = "DEGREE";

    private NavigateActivityViewModel navigateActivityViewModel;
    private TTSHelper ttsHelper;

    public static NavigateFragment newInstance(Poi departurePoi, ArrayList<Poi> route, double degree) {
        NavigateFragment fragment = new NavigateFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_DEPARTURE_LOCATION, departurePoi);
        bundle.putParcelableArrayList(ARG_ROUTE, route);
        bundle.putDouble(ARG_DEGREE, degree);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_navigate, container, false);
        ttsHelper = new TTSHelper(requireActivity());
        navigateActivityViewModel = new ViewModelProvider(requireActivity()).get(NavigateActivityViewModel.class);

        requireActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), getBackPressedCallback());

        startNavigate();

        rootView.findViewById(R.id.nearLocationNameBT)
                .setOnClickListener(view -> {
                    ttsHelper.speakString(navigateActivityViewModel.getCurLocationName().getValue());
                });
        rootView.findViewById(R.id.stopGuideBT)
                .setOnClickListener(view -> {
                    ttsHelper.speakString("안내를 종료합니다.");
                    stopNavigate();
                });
        return rootView;
    }

    @NonNull
    private OnBackPressedCallback getBackPressedCallback() {
        return new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                stopNavigate();
            }
        };
    }


    public void startNavigate() {
        Activity activity = requireActivity();
        Intent intent = new Intent(activity, NavigationService.class);
        intent.putExtras(getArguments());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.startForegroundService(intent);
            return;
        }
        activity.startService(intent);
    }

    public void stopNavigate() {
        Activity activity = requireActivity();
        activity.stopService(new Intent(activity, NavigationService.class));
        activity.finish();
    }

}