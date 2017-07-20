package com.needfood.kh.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.needfood.kh.Constructor.CommentNotifConstructor;
import com.needfood.kh.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vi on 5/5/2017.
 */

public class CommentNotifAdapter extends
        RecyclerView.Adapter<CommentNotifAdapter.RecyclerViewHolder> {

    private List<CommentNotifConstructor> listData = new ArrayList<>();
    Context context;
    public CommentNotifAdapter(Context context,List<CommentNotifConstructor> listData) {
        this.context = context;
        this.listData = listData;
    }



    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView stt;
        public ImageView img;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
       /*     img = (ImageView)itemView.findViewById(R.id.imgavt);
            tvName = (TextView)itemView.findViewById(R.id.name);
            stt = (TextView)itemView.findViewById(R.id.tvcontent);*/


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
        View itemView = inflater.inflate(R.layout.customcmtnotif, viewGroup, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
        CommentNotifConstructor ip = listData.get(position);

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
