package com.needfood.kh.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.needfood.kh.Constructor.MenuBrandConstructor;
import com.needfood.kh.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Vi on 5/8/2017.
 */

public class MenuBrandAdapter extends
        RecyclerView.Adapter<MenuBrandAdapter.RecyclerViewHolder> {

    private List<MenuBrandConstructor> listData = new ArrayList<>();
    Context context;
    public MenuBrandAdapter(Context context,List<MenuBrandConstructor> listData) {
        this.context = context;
        this.listData = listData;
    }



    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvgia;
        public TextView tvunit;

        public TextView stt;
        public ImageView img;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView)itemView.findViewById(R.id.namemn);
            tvgia = (TextView)itemView.findViewById(R.id.mnmn);
            tvunit = (TextView)itemView.findViewById(R.id.unitmn);
            img = (ImageView)itemView.findViewById(R.id.imgmn);


        }

    }
    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup,
                                                 int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.cusmenubrand, viewGroup, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
        MenuBrandConstructor ip = listData.get(position);
        viewHolder.tvName.setText(ip.getNamemn());
        viewHolder.tvgia.setText(NumberFormat.getNumberInstance(Locale.UK).format(Integer.parseInt(ip.getGiamn()))+ip.getMn());
        viewHolder.tvunit.setText(ip.getUnit());
        if(ip.getImgmenu()!=null){
            Picasso.with(context).load(ip.getImgmenu()).into(viewHolder.img);
        }

    }

    public void removeItem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listData.size());
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for item view of list
     */

}
