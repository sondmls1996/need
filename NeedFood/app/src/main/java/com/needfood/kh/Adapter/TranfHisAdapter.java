package com.needfood.kh.Adapter;

import android.content.Context;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.needfood.kh.Constructor.OrderHisConstructor;
import com.needfood.kh.Constructor.TranfConstructor;
import com.needfood.kh.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 21/07/2017.
 */

public class TranfHisAdapter extends ArrayAdapter<TranfConstructor> {
    Context context;
    List<TranfConstructor> items;

    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();

    public TranfHisAdapter(Context context, List<TranfConstructor> items) {
        super(context, -1, items);
        this.context = context;
        this.items = items;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.custranhis, null);
        }
        TranfConstructor p = getItem(position);
        if (p != null) {
            // Anh xa + Gan gia tri

            TextView tvct = (TextView) view.findViewById(R.id.txt_date);
            tvct.setText(p.getTime());
            TextView tvc = (TextView) view.findViewById(R.id.txt_coin);
            tvc.setText(p.getCoin());
            TextView tvm = (TextView) view.findViewById(R.id.txt_mess);
            tvm.setText(p.getMess());


        } else {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

}

