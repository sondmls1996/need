package com.nf.vi.needfoodshipper.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nf.vi.needfoodshipper.Constructor.ListviewContructor;
import com.nf.vi.needfoodshipper.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Minh Nhat on 7/26/2017.
 */

public class ListviewAdapter extends ArrayAdapter<ListviewContructor> {

    public ListviewAdapter(Context context, int resource, List<ListviewContructor> textViewResourceId) {
        super(context, resource, textViewResourceId);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v2 = convertView;
        if (v2 == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v2 = inflater.inflate(R.layout.custom_lv, null);
        }
        ListviewContructor p = getItem(position);
        if (p != null) {

            TextView tvSanpham = (TextView) v2.findViewById(R.id.tvSanpham);
            TextView tvSoluong = (TextView) v2.findViewById(R.id.tvSoluong);
            TextView tvDongia = (TextView) v2.findViewById(R.id.tvDongia);
            TextView tvTinhtien = (TextView) v2.findViewById(R.id.tvTinhtien);
            tvSanpham.setText(p.sanpham);
            tvSoluong.setText(p.soluong);
            tvDongia.setText(p.dongia);
            tvTinhtien.setText(p.tinhtien);

        } else {

        }
        return v2;
    }
}
