package com.needfood.kh.Notif;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.needfood.kh.Adapter.NotifAdapter;
import com.needfood.kh.Constructor.NotifConstructor;
import com.needfood.kh.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notif extends Fragment {

    ListView lv;
    NotifAdapter adapter;
    ArrayList<NotifConstructor> arr;
    public Notif() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notif,container,false);
        lv = (ListView)v.findViewById(R.id.lvnotif);
        arr = new ArrayList<>();
        adapter = new NotifAdapter(getContext(),R.layout.customnotif,arr);
        lv.setAdapter(adapter);
        arr.add(new NotifConstructor("http://unameitcs.com/wp-content/uploads/2013/08/blue-mail-icon-300x300.png","Bạn có 1 tin nhắn","15 min"));
        arr.add(new NotifConstructor("http://unameitcs.com/wp-content/uploads/2013/08/blue-mail-icon-300x300.png","Bạn có 1 tin nhắn","15 min"));
        arr.add(new NotifConstructor("https://cdn.pixabay.com/photo/2016/08/20/05/38/avatar-1606916_640.png","Long đã bình luận về bài viết","15 min"));
        arr.add(new NotifConstructor("http://unameitcs.com/wp-content/uploads/2013/08/blue-mail-icon-300x300.png","Bạn có 1 tin nhắn","15 min"));
        adapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(getContext(),ReplyNotif.class);
                startActivity(it);
            }
        });
        return v;
    }

}
