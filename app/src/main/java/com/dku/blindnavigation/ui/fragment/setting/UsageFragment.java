package com.dku.blindnavigation.ui.fragment.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.dku.blindnavigation.R;
import com.dku.blindnavigation.ui.adapter.UsageFragmentAdapter;

import me.relex.circleindicator.CircleIndicator3;

public class UsageFragment extends SettingFragment {

    private static final String TAG = "UsageFragment";
    private static final int FRAGMENT_COUNT = 4;

    private ViewPager2 pager;
    private CircleIndicator3 indicator;
    private boolean isFirstToMainBTClicked = true;

    public static UsageFragment newInstance() {
        return new UsageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_usage, container, false);
        setTitle(getString(R.string.usage));
        initViewPager(rootView);
        rootView.<Button>findViewById(R.id.usageToMainBT)
                .setOnClickListener(view -> isFirstToMainBTClicked = toMainMenu(isFirstToMainBTClicked));
        return rootView;
    }

    private void initViewPager(View rootView) {
        FragmentStateAdapter pagerAdapter = new UsageFragmentAdapter(getChildFragmentManager(),
                getViewLifecycleOwner().getLifecycle(), FRAGMENT_COUNT);

        pager = rootView.findViewById(R.id.viewpager);
        pager.setAdapter(pagerAdapter);
        pager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        pager.setCurrentItem(1000); //시작 지점
        pager.setOffscreenPageLimit(4); //최대 이미지 수

        indicator = rootView.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        indicator.createIndicators(FRAGMENT_COUNT, 0);

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    pager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                indicator.animatePageSelected(position % FRAGMENT_COUNT);
            }
        });
    }

}