package com.needfood.kh.SupportClass;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Vi on 3/29/2017.
 */

public class ViewAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragment = new ArrayList<Fragment>();
    ArrayList<String> titles = new ArrayList<String>();


    public void addFragment(Fragment fragment, String title){
        this.fragment.add(fragment);
        this.titles.add(title);
    }
    public ViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragment.get(position);
    }

    @Override
    public int getCount() {
        return fragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
