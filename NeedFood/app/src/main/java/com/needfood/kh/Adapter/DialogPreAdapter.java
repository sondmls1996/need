package com.needfood.kh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.needfood.kh.Constructor.PreDialogConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;
import com.needfood.kh.Service.BubbleService;
import com.needfood.kh.SupportClass.ChangeTimestamp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vi on 9/7/2017.
 */

public class DialogPreAdapter  extends
        RecyclerView.Adapter<DialogPreAdapter.RecyclerViewHolder> {
    public DataHandle db;
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
            db = new DataHandle(context);
            prename = (TextView) itemView.findViewById(R.id.prename2);
            prequan = (TextView)itemView.findViewById(R.id.prequan2);
            img = (ImageView)itemView.findViewById(R.id.imgclose);

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
        View itemView = inflater.inflate(R.layout.cuspredialog, viewGroup, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder viewHolder, final int position) {
        final PreDialogConstructor ip = listData.get(position);
        viewHolder.prename.setText(ip.getTitle());

        viewHolder.prequan.setText(ip.getQuanli());
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deletePrd(ip.getId());
                removeItem(position);
                context.startService(new Intent(context, BubbleService.class));
            }
        });


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
