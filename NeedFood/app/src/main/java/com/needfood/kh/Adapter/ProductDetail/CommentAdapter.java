package com.needfood.kh.Adapter.ProductDetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.needfood.kh.Constructor.ProductDetail.CommentConstructor;
import com.needfood.kh.R;
import com.needfood.kh.SupportClass.ChangeTimestamp;
import com.needfood.kh.SupportClass.TransImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vi on 4/27/2017.
 */

public class CommentAdapter extends
        RecyclerView.Adapter<CommentAdapter.RecyclerViewHolder> {

    private List<CommentConstructor> listData = new ArrayList<>();
    Context context;
    public CommentAdapter(Context context,List<CommentConstructor> listData) {
        this.context = context;
        this.listData = listData;
    }



    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public ChangeTimestamp change;
        public TextView tvName;
        public TextView stt;
        public ImageView img;
        public TextView txttime;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            change = new ChangeTimestamp();
            tvName = (TextView)itemView.findViewById(R.id.name);
            stt = (TextView)itemView.findViewById(R.id.tvcontent);
            txttime = (TextView)itemView.findViewById(R.id.txttime);


        }

    }
    @Override
    public int getItemCount() {
        if(listData.size()<5){
            return listData.size();
        }else{
            return 5;
        }

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup,
                                                 int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.customcmt, viewGroup, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
        CommentConstructor ip = listData.get(position);
        viewHolder.txttime.setText(viewHolder.change.getDateCurrentTimeZone(ip.getTime()));

        if(ip.getName()==null){
            viewHolder.tvName.setText("Chờ xử lý");
        }else{
            viewHolder.tvName.setText(ip.getName()+": ");
        }
        if(ip.getContent()==null){
            viewHolder.stt.setText("Chờ xử lý");
        }else{
            viewHolder.stt.setText(ip.getContent());
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
