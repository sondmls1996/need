package com.needfood.kh.Sugges;


import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.needfood.kh.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuggessFrag extends Fragment {
    LocalActivityManager mlam;

    public SuggessFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_suggess, container, false);

        mlam = new LocalActivityManager(getActivity(), false);
        final TabHost tabHost = (TabHost) v.findViewById(android.R.id.tabhost);
        mlam.dispatchCreate(savedInstanceState);
        tabHost.setup(mlam);
        tabHost.getTabWidget().setStripEnabled(true);
        TabWidget widget = tabHost.getTabWidget();
        for (int i = 0; i < widget.getChildCount(); i++) {
            View v2 = widget.getChildAt(i);

            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tv = (TextView) v2.findViewById(android.R.id.title);
            if (tv == null) {
                continue;
            }
            v2.setBackgroundResource(R.drawable.tabselect);
        }
        //   TabHost tabHost = getTabHost();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("Raw-Food");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Food");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Drink");
        TabHost.TabSpec tab4 = tabHost.newTabSpec("Fruits");

        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected
        tab1.setIndicator(getResources().getString(R.string.raw));
        tab2.setIndicator(getResources().getString(R.string.food));
        tab3.setIndicator(getResources().getString(R.string.drink));
        tab4.setIndicator(getResources().getString(R.string.fruits));
        tab1.setContent(new Intent(getActivity(), RawFood.class));
        tab2.setContent(new Intent(getActivity(), Food.class));
        tab3.setContent(new Intent(getActivity(), Drink.class));
        tab4.setContent(new Intent(getActivity(), Fruits.class));

        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
        tabHost.addTab(tab4);
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
        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(getResources().getColor(R.color.darkred)); // selected

        return v;
    }

}
