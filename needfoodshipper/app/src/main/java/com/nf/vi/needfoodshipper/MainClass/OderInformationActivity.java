package com.nf.vi.needfoodshipper.MainClass;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nf.vi.needfoodshipper.Adapter.ListviewAdapter;
import com.nf.vi.needfoodshipper.Constructor.ListUserContructor;
import com.nf.vi.needfoodshipper.Constructor.ListviewContructor;
import com.nf.vi.needfoodshipper.MainClass.BandoActivity;
import com.nf.vi.needfoodshipper.MainClass.DeliveryActivity;
import com.nf.vi.needfoodshipper.R;
import com.nf.vi.needfoodshipper.SupportClass.ChangeDatetoTimestamp;
import com.nf.vi.needfoodshipper.SupportClass.ChangeTimeToHours;
import com.nf.vi.needfoodshipper.database.DBHandle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OderInformationActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private TabLayout tabLayout;

    private String a, id, order, lc, ct, re, tl, pay, moneyship, stt, code, stt2, listsanpham, tm;


    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder_information);
        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        Intent data = getIntent();
        id = data.getStringExtra("id");
        order = data.getStringExtra("order");
        lc = data.getStringExtra("lc");
        ct = data.getStringExtra("ct");
        re = data.getStringExtra("re");
        tl = data.getStringExtra("tl");
        pay = data.getStringExtra("pay");
        moneyship = data.getStringExtra("moneyship");
        code = data.getStringExtra("code");
        stt = data.getStringExtra("stt");
        listsanpham = data.getStringExtra("listsanpham");
        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.t1)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.t2)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        //Creating our pager adapter
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
       viewPager.setCurrentItem(tab.getPosition());
//        tabLayout.setTabTextColors(R.color.white, R.color.white);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
//        tabLayout.setTabTextColors(R.color.white, R.color.white);

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
//        tabLayout.setTabTextColors(R.color.white, R.color.white);
    }
}

class PagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public PagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                DeliveryActivity tab1 = new DeliveryActivity();

                return tab1;
            case 1:
                BandoActivity tab2 = new BandoActivity();
                return tab2;

            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
//    public void setupViewPager(ViewPager viewPager) {
//        viewAdapter = new ViewAdapter(getSupportFragmentManager());
//        viewAdapter.addFragment(new DetailRoom(), "Chi tiết");
//        viewAdapter.addFragment(new MapRoom(), "Bản đồ");
//        viewPager.setAdapter(viewAdapter);
//}
}

