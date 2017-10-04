package com.nf.vi.needfoodshipper.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.nf.vi.needfoodshipper.Constructor.ListUserContructor;
import com.nf.vi.needfoodshipper.Constructor.SearchContructer;
import com.nf.vi.needfoodshipper.R;
import com.nf.vi.needfoodshipper.database.DBHandle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Minh Nhat on 7/16/2017.
 */

public class
SearchAdapter extends RecyclerView.Adapter<SearchAdapter.RecyclerViewHolder> {
    public String tk, note, stt, stt1, id;
    private List<SearchContructer> listData = new ArrayList<>();
    Context context;
    DBHandle db;
    List<ListUserContructor> list;

    public SearchAdapter(Context context, List<SearchContructer> listData) {
        this.context = context;
        this.listData = listData;
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rl;
        public TextView tvcode;
        public TextView ord;
        public TextView loc;

        public TextView cta;
        public TextView rec;
        public TextView timel;
        public TextView paym;
        public TextView tvtrangthai;


        public RecyclerViewHolder(View itemView) {
            super(itemView);
            db = new DBHandle(context);
            list = db.getAllUser();
            for (ListUserContructor lu : list) {
                tk = lu.getAccessToken();
            }

            ord = (TextView) itemView.findViewById(R.id.tvod);
            loc = (TextView) itemView.findViewById(R.id.tvloc);
            cta = (TextView) itemView.findViewById(R.id.tvct);
            rec = (TextView) itemView.findViewById(R.id.tvre);
            timel = (TextView) itemView.findViewById(R.id.tvtm);
            paym = (TextView) itemView.findViewById(R.id.tvpay);

        }

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public SearchAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup,
                                                                int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.custom_history, viewGroup, false);
        return new SearchAdapter.RecyclerViewHolder(itemView);
    }

//    @Override
//    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
//
//    }


    @Override
    public void onBindViewHolder(final SearchAdapter.RecyclerViewHolder viewHolder, int position) {
        final SearchContructer ip = listData.get(position);
        id = ip.getId();
        stt1 = ip.getStt();
        viewHolder.tvcode.setText(ip.getCode());
        viewHolder.ord.setText(ip.getOrder());
        viewHolder.loc.setText(ip.getLc());
        viewHolder.cta.setText(ip.getCt());
        viewHolder.rec.setText(ip.getRe());
        viewHolder.timel.setText(ip.getTl());
        viewHolder.paym.setText(ip.getPay());
//        viewHolder.tvtrangthai.setText(ip.getStt());
        if(stt1.equals("cancel")){

            viewHolder.tvtrangthai.setBackgroundResource(R.color.red);
        }else if(stt1.equals("finish")){

            viewHolder.tvtrangthai.setBackgroundResource(R.color.yellow);
        }
        else if(stt1.equals("waiting")){

            viewHolder.tvtrangthai.setBackgroundResource(R.color.blue);
        }
    }
//    public void removeItem(int position) {
//        listData.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, listData.size());
//        notifyDataSetChanged();
//    }
}
