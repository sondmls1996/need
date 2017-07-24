package com.needfood.kh.Notif;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.needfood.kh.Adapter.NotifAdapter;
import com.needfood.kh.Constructor.NotiConstructor;
import com.needfood.kh.Constructor.NotifConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notif extends Fragment {

    ListView lv;
    NotifAdapter adapter;
    List<NotiConstructor> arr;
    TextView nop;

    public Notif() {
        // Required empty public constructor
    }

    DataHandle db;
    String a, b, c;
    SwipeRefreshLayout refresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notif, container, false);
        lv = (ListView) v.findViewById(R.id.lvnotif);
        nop = (TextView) v.findViewById(R.id.nop8);
        db = new DataHandle(getActivity());
        arr = new ArrayList<>();
        adapter = new NotifAdapter(getContext(), arr);
        lv.setAdapter(adapter);
        arr = db.getNoti();
        for (NotiConstructor nc : arr) {
            a = nc.getTitle();
            c = nc.getTime();
        }
        Collections.reverse(arr);

        if (arr.size() == 0) {
            nop.setVisibility(View.VISIBLE);
        } else {
            nop.setVisibility(View.GONE);

            adapter.notifyDataSetChanged();
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadagain();
    }

    public void loadagain() {
        arr.clear();
        arr = db.getNoti();
        adapter = new NotifAdapter(getContext(), arr);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
//        refresh.setRefreshing(false);

    }
}
