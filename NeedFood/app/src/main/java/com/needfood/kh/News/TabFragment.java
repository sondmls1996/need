package com.needfood.kh.News;


import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.needfood.kh.Hotdeal.Hotdeal;
import com.needfood.kh.R;
import com.needfood.kh.StartActivity;

import static android.R.id.tabhost;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment {
    LocalActivityManager mlam;
    public  static  TabHost tabHost;
    public TabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab,container,false);
        mlam = new LocalActivityManager(getActivity(), false);
         tabHost = (TabHost) v.findViewById(tabhost);

        mlam.dispatchCreate(savedInstanceState);
        tabHost.setup(mlam);

        //   TabHost tabHost = getTabHost();
        TabHost.TabSpec taba = tabHost.newTabSpec(getString(R.string.new_));
        TabHost.TabSpec tab1 = tabHost.newTabSpec(getString(R.string.topdeall));
        TabHost.TabSpec tab2 = tabHost.newTabSpec(getString(R.string.besqua));

        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected
        taba.setIndicator(getString(R.string.new_));
        tab1.setIndicator(getString(R.string.topdeall));

        taba.setContent(new Intent(getActivity(),News.class));

        tab1.setContent(new Intent(getActivity(),Hotdeal.class));

        tab2.setIndicator(getString(R.string.besqua));
        tab2.setContent(new Intent(getActivity(),BestQuality.class));

        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(taba);
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {

                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    tabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.red)); // unselected
                    TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
                    tv.setTextColor(Color.parseColor("#ffffff"));
                }

                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(getResources().getColor(R.color.darkred)); // selected

            }
        });
        tabHost.setCurrentTab(StartActivity.pg);
        return v;
    }

    @Override
    public void onResume() {
        mlam.dispatchResume();
        super.onResume();
    }
}
