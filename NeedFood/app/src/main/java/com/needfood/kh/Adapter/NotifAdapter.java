package com.needfood.kh.Adapter;

import android.content.Context;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.needfood.kh.Constructor.NotiConstructor;
import com.needfood.kh.Constructor.NotifConstructor;
import com.needfood.kh.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Vi on 4/25/2017.
 */

public class NotifAdapter extends ArrayAdapter<NotiConstructor> {
    Context context;
    List<NotiConstructor> items;
    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();

    public NotifAdapter(Context context, List<NotiConstructor> items) {
        super(context, -1, items);
        this.context = context;
        this.items = items;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.customnotif, null);
        }
        NotiConstructor p = getItem(position);
        if (p != null) {
            // Anh xa + Gan gia tri
            Log.d("NOTI", p.getTitle() + "-" + p.getTime());
            TextView tvct = (TextView) view.findViewById(R.id.notifct);
            tvct.setText(p.getTitle());
            TextView min = (TextView) view.findViewById(R.id.min);
            min.setText(p.getTime());

            ImageView imageView = (ImageView) view.findViewById(R.id.imgnotif);

//            if (p.getImg().isEmpty()) {
            Picasso.with(getContext()).load(R.drawable.logo).into(imageView);
//            } else {
//                Picasso.with(getContext()).load(p.getImg()).into(imageView);
//            }


        } else {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

}
