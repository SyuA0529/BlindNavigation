package com.dku.blindnavigation.ui.fragment.navigate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dku.blindnavigation.R;
import com.dku.blindnavigation.viewmodel.navigate.NavigateActivityViewModel;

public class DestinationArriveFragment extends Fragment {

    private static final String TAG = "DestinationArriveFragment";

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
        new ViewModelProvider(requireActivity()).get(NavigateActivityViewModel.class)
                .changeTitle("목적지 도착");
        rootView.findViewById(R.id.arriveDestToMainBT)
                .setOnClickListener(view -> requireActivity().finish());
        return rootView;
    }
}