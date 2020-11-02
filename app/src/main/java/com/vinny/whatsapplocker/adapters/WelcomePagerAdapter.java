package com.vinny.whatsapplocker.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.vinny.whatsapplocker.fragments.ChooserFragment;
import com.vinny.whatsapplocker.fragments.WelcomeFragment;

/**
 * Created by DENNOH on 10/2/2015.
 */
public class WelcomePagerAdapter extends FragmentPagerAdapter {

    public WelcomePagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return WelcomeFragment.getInstance();
            case 1:
                return ChooserFragment.getInstance();
        }
        return WelcomeFragment.getInstance();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
