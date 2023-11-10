package com.dku.blindnavigation.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.dku.blindnavigation.R;
import com.dku.blindnavigation.ui.fragment.setting.InquiryFragment;
import com.dku.blindnavigation.ui.fragment.setting.SettingMainFragment;
import com.dku.blindnavigation.ui.fragment.setting.TtsSettingFragment;
import com.dku.blindnavigation.ui.fragment.setting.UsageFragment;
import com.dku.blindnavigation.viewmodel.setting.SettingActivityViewModel;

public class SettingActivity extends AppCompatActivity {

    private static final String TAG = "SettingActivity";

    private final Fragment[] fragments = new Fragment[]{
            SettingMainFragment.newInstance(),
            TtsSettingFragment.newInstance(),
            UsageFragment.newInstance(),
            InquiryFragment.newInstance()
    };

    private FragmentManager fragmentManager;
    private TextView titleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        fragmentManager = getSupportFragmentManager();
        SettingActivityViewModel viewModel = new ViewModelProvider(this).get(SettingActivityViewModel.class);

        viewModel.getFragmentNum().observe(this, this::changeFragment);

        titleTV = findViewById(R.id.settingTitleTV);
        viewModel.changeTitle(getString(R.string.setting));
        viewModel.getTitle().observe(this, this::changeTitle);
    }

    private void changeFragment(int fragmentNum) {
        fragmentManager.beginTransaction()
                .replace(R.id.settingFragmentLayout, fragments[fragmentNum])
                .commit();
    }

    private void changeTitle(String title) {
        titleTV.setText(title);
    }

}