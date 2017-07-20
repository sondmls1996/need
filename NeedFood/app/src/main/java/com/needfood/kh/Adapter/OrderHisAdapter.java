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
import com.needfood.kh.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 17/07/2017.
 */

public class OrderHisAdapter extends ArrayAdapter<OrderHisConstructor> {
    Context context;
    List<OrderHisConstructor> items;

    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();

    public OrderHisAdapter(Context context, List<OrderHisConstructor> items) {
        super(context, -1, items);
        this.context = context;
        this.items = items;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.cusorderhis, null);
        }
        OrderHisConstructor p = getItem(position);
        if (p != null) {
            // Anh xa + Gan gia tri

            TextView tvct = (TextView) view.findViewById(R.id.title_pro);
            tvct.setText(p.getTitle());
            ImageView imgstt = (ImageView)view.findViewById(R.id.sttimg);
            if(p.getStatus().equals("new")){
                imgstt.setVisibility(View.VISIBLE);
            }else {
                imgstt.setVisibility(View.GONE);
            }
            TextView pri = (TextView) view.findViewById(R.id.price);
            pri.setText(NumberFormat.getNumberInstance(Locale.UK).format(Integer.parseInt(p.getMoney())));



        } else {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

}
