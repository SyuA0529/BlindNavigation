package com.dku.blindnavigation.activity.setting;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.dku.blindnavigation.R;
import com.dku.blindnavigation.activity.utils.IntentUtils;

import me.relex.circleindicator.CircleIndicator3;

public class UsageActivity extends AppCompatActivity {

    private static final int FRAGMENT_COUNT = 4;

    private ViewPager2 pager;
    private CircleIndicator3 indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage);
        initViewPager();

        this.<Button>findViewById(R.id.usageToMainBT).setOnClickListener(v ->
                startActivity(IntentUtils.createStartMainActivityIntent(this)));
    }

    private void initViewPager() {
        FragmentStateAdapter pagerAdapter = new UsageFragmentAdapter(this, FRAGMENT_COUNT);

        pager = findViewById(R.id.viewpager);
        pager.setAdapter(pagerAdapter);
        pager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        pager.setCurrentItem(1000); //시작 지점
        pager.setOffscreenPageLimit(4); //최대 이미지 수

        indicator = findViewById(R.id.indicator);
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