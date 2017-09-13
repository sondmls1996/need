package com.needfood.kh.Adapter.ProductDetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.needfood.kh.Constructor.ProductDetail.QuanConstructor;
import com.needfood.kh.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vi on 4/27/2017.
 */

public class QuanAdapter extends  RecyclerView.Adapter<QuanAdapter.RecyclerViewHolder> {

private List<QuanConstructor> listData = new ArrayList<>();
        Context context;
public QuanAdapter(Context context,List<QuanConstructor> listData) {
        this.context = context;
        this.listData = listData;
        }



public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView tvName;
    public TextView stt;
    public ImageView img;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        img = (ImageView)itemView.findViewById(R.id.quanimg);
        tvName = (TextView)itemView.findViewById(R.id.quanname);

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
        View itemView = inflater.inflate(R.layout.customquanitem, viewGroup, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
        QuanConstructor ip = listData.get(position);

        if(ip.getImg()==null){
            Picasso.with(context).load(R.drawable.bg).into(viewHolder.img);
        }else{
            Picasso.with(context).load(ip.getImg()).into(viewHolder.img);
        }
        if(ip.getName()==null){
            viewHolder.tvName.setText("Chờ xử lý");
        }else{
            viewHolder.tvName.setText(ip.getName());
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
