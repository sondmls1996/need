package com.needfood.kh.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.needfood.kh.Constructor.PreDialogConstructor;
import com.needfood.kh.R;
import com.needfood.kh.SupportClass.ChangeTimestamp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vi on 9/7/2017.
 */

public class DialogPreAdapter  extends
        RecyclerView.Adapter<DialogPreAdapter.RecyclerViewHolder> {

    private List<PreDialogConstructor> listData = new ArrayList<>();
    Context context;
    public DialogPreAdapter(Context context,List<PreDialogConstructor> listData) {
        this.context = context;
        this.listData = listData;
    }



    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public ChangeTimestamp change;
        public TextView prequan;
        public TextView premn;
        public ImageView img;
        public TextView prename;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            prename = (TextView) itemView.findViewById(R.id.prename);
            prequan = (TextView)itemView.findViewById(R.id.prequan);
            premn = (TextView)itemView.findViewById(R.id.pretot);


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
        View itemView = inflater.inflate(R.layout.cuspre, viewGroup, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
        PreDialogConstructor ip = listData.get(position);
        viewHolder.prename.setText(ip.getTitle());

        viewHolder.prequan.setText(ip.getQuanli());


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
