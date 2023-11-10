package com.dku.blindnavigation.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dku.blindnavigation.ui.fragment.setting.usage.UsageFragment1;
import com.dku.blindnavigation.ui.fragment.setting.usage.UsageFragment2;
import com.dku.blindnavigation.ui.fragment.setting.usage.UsageFragment3;
import com.dku.blindnavigation.ui.fragment.setting.usage.UsageFragment4;

public class UsageFragmentAdapter extends FragmentStateAdapter {

    private final int fragmentCount;



    public UsageFragmentAdapter(@NonNull Fragment fragment, int fragmentCount) {
        super(fragment);
        this.fragmentCount = fragmentCount;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        position = getRealPosition(position);
        switch (position) {
            case 0:
                return new UsageFragment1();
            case 1:
                return new UsageFragment2();
            case 2:
                return new UsageFragment3();
            default:
                return new UsageFragment4();
        }
    }

    @Override
    public int getItemCount() {
        return 2000;
    }

    private int getRealPosition(int position) {
        return position % fragmentCount;
    }

}
