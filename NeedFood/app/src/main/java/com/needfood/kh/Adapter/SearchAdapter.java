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

import com.needfood.kh.Constructor.SearchConstructor;
import com.needfood.kh.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Vi on 7/14/2017.
 */

public class SearchAdapter extends ArrayAdapter<SearchConstructor> {
    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();

    public SearchAdapter(Context context, int resource, List<SearchConstructor> items) {
        super(context, resource, items);
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view =  inflater.inflate(R.layout.customsearch, null);
        }
        SearchConstructor p = getItem(position);
        if (p != null) {
            // Anh xa + Gan gia tri

            TextView name = (TextView)view.findViewById(R.id.namesearch);
            name.setText(p.name);
            TextView gia = (TextView)view.findViewById(R.id.giasearch);
            gia.setText(NumberFormat.getNumberInstance(Locale.UK).format(Integer.parseInt(p.getGia()))+p.getDonvi());
            TextView un = (TextView)view.findViewById(R.id.unitsearch);
            un.setText(p.getUnit());
            ImageView imageView = (ImageView)view.findViewById(R.id.imgsearch);

            if (p.linkimg == null) {
               // Picasso.with(getContext()).load(R.drawable.txt1).into(imageView);
            } else{
                Picasso.with(getContext()).load("http://needfood.webmantan.com"+p.linkimg).into(imageView);
            }
        }else{
            Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
        }
        return view;
    }

}
