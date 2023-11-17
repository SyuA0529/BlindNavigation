package com.dku.blindnavigation.ui.fragment.navigate;

import static android.content.Context.SENSOR_SERVICE;

import android.annotation.SuppressLint;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dku.blindnavigation.R;
import com.dku.blindnavigation.repository.navigate.RemoteNavigateDataSource;
import com.dku.blindnavigation.repository.navigate.remote.dto.Poi;
import com.dku.blindnavigation.utils.AudioRecordHelper;
import com.dku.blindnavigation.utils.TTSHelper;
import com.dku.blindnavigation.utils.direction.OrientationListener;
import com.dku.blindnavigation.viewmodel.navigate.NavigateActivityViewModel;
import com.dku.blindnavigation.viewmodel.navigate.RouteSelectFragmentViewModel;

import java.util.Objects;

public class DestinationSelectFragment extends Fragment {

    private static final String TAG = "DestinationSelectFragment";

    private TTSHelper ttsHelper;
    private NavigateActivityViewModel rootViewModel;
    private RouteSelectFragmentViewModel viewModel;
    private AudioRecordHelper audioRecordHelper;
    private OrientationListener mOrientationListener;
    private RemoteNavigateDataSource remoteNavigateDataSource;

    private boolean isFirstSelectBTClicked = true;
    private boolean isFirstNextBTClicked = true;
    private boolean isFirstFindBTClicked = true;
    private boolean isFirstToMainBTClicked = true;

    public static DestinationSelectFragment newInstance() {
        DestinationSelectFragment fragment = new DestinationSelectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initComponent();
        View rootView = inflater.inflate(R.layout.fragment_destination_select, container, false);
        rootViewModel.changeTitle(getString(R.string.destinationSelectTitle));
        initDestinationNameBT(rootView);
        initSelectDestinationBT(rootView);
        initNextDestinationBT(rootView);
        rootView.findViewById(R.id.selectDestToMainBT)
                .setOnClickListener(view -> {
                    if (isFirstToMainBTClicked) {
                        ttsHelper.speakString("메인 메뉴로 이동하는 버튼입니다");
                        isFirstToMainBTClicked = false;
                        return;
                    }
                    requireActivity().finish();
                });
        viewModel.getDestinationPois().observe(getViewLifecycleOwner(), pois -> {
            if (Objects.isNull(pois)) {
                return;
            }
            ttsHelper.speakString(viewModel.getNextDestinationPoi().getName());
        });
        return rootView;
    }

    private void initNextDestinationBT(View rootView) {
        rootView.findViewById(R.id.nextDestinationBT)
                .setOnClickListener(view -> {
                    Poi nextDestination = viewModel.getNextDestinationPoi();
                    if (Objects.isNull(nextDestination)) {
                        if (isFirstNextBTClicked) {
                            ttsHelper.speakString("다음 목적지를 알려주는 버튼입니다");
                            isFirstNextBTClicked = false;
                            return;
                        }
                        ttsHelper.speakString("일치하는 목적지가 없습니다");
                        return;
                    }
                    ttsHelper.speakString(nextDestination.getName());
                });
    }

    private void initSelectDestinationBT(View rootView) {
        rootView.findViewById(R.id.selectDestinationBT)
                .setOnClickListener(view -> {
                    if (isFirstSelectBTClicked) {
                        ttsHelper.speakString("목적지 선택 버튼입니다");
                        isFirstSelectBTClicked = false;
                        return;
                    }
                    if (Objects.isNull(viewModel.getCurDestinationPoi())) {
                        ttsHelper.speakString("목적지가 존재하지 않습니다");
                        return;
                    }
                    rootViewModel.postDestinationPoi(viewModel.getCurDestinationPoi());
                });
    }

    private void initDestinationNameBT(View rootView) {
        rootView.findViewById(R.id.findDestinationBT)
                .setOnClickListener(v -> {
                    if (isFirstFindBTClicked) {
                        ttsHelper.speakString("목적지 검색 버튼입니다");
                        isFirstFindBTClicked = false;
                        return;
                    }
                    audioRecordHelper.onRecord();
                    if (audioRecordHelper.hasRecordedData()) {
                        remoteNavigateDataSource.getDestinationName(requireActivity(), audioRecordHelper.getRecordedData(), viewModel::postDestinationName);
                    }
                });
        viewModel.getDestinationName()
                .observe(getViewLifecycleOwner(), destinationName -> {
                    if (Objects.isNull(destinationName)) {
                        return;
                    }
                    remoteNavigateDataSource.getDestinationPoi(requireActivity(), destinationName, viewModel::postDestinationPois);
                });
    }

    private void initComponent() {
        ttsHelper = new TTSHelper(requireActivity());
        audioRecordHelper = new AudioRecordHelper(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this).get(RouteSelectFragmentViewModel.class);
        rootViewModel = new ViewModelProvider(requireActivity()).get(NavigateActivityViewModel.class);
        remoteNavigateDataSource = RemoteNavigateDataSource.getInstance();
        mOrientationListener = new OrientationListener((SensorManager) getActivity().getSystemService(SENSOR_SERVICE), rootViewModel::postPhoneDegree);
    }

    @Override
    public void onResume() {
        super.onResume();
        mOrientationListener.registerSensorListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        mOrientationListener.unregisterSensorListeners();
    }

}