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

import com.needfood.kh.Constructor.NotifConstructor;
import com.needfood.kh.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Vi on 4/25/2017.
 */

public class NotifAdapter extends ArrayAdapter<NotifConstructor> {
    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();

    public NotifAdapter(Context context, int resource, List<NotifConstructor> items) {


        super(context, resource, items);
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view =  inflater.inflate(R.layout.customnotif, null);
        }
        NotifConstructor p = getItem(position);
        if (p != null) {
            // Anh xa + Gan gia tri

            TextView tvct = (TextView)view.findViewById(R.id.notifct);
            tvct.setText(p.content);
            TextView min = (TextView)view.findViewById(R.id.min);
            min.setText(p.time);

            ImageView imageView = (ImageView)view.findViewById(R.id.imgnotif);

            if (p.linkimg == null) {
                Picasso.with(getContext()).load(R.drawable.txt1).into(imageView);
            } else{
                Picasso.with(getContext()).load(p.linkimg).into(imageView);
            }



        }else{
            Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
        }
        return view;
    }

}
