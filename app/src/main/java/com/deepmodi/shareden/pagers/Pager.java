package com.deepmodi.shareden.pagers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.deepmodi.shareden.ui.FromRequest.FromRequest;
import com.deepmodi.shareden.ui.Home.HomeFragment;
import com.deepmodi.shareden.ui.ToRequest.ToRequest;

public class Pager extends FragmentPagerAdapter {
    public Pager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new FromRequest();
        }
        else if (position == 1)
        {
            fragment = new ToRequest();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "From Requests";
        }
        else if (position == 1)
        {
            title = "To Requests";
        }
        return title;
    }
}