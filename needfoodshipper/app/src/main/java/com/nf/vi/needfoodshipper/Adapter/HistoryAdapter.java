package com.nf.vi.needfoodshipper.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nf.vi.needfoodshipper.Constructor.HistoryConstructor;
import com.nf.vi.needfoodshipper.Constructor.MainConstructor;
import com.nf.vi.needfoodshipper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Minh Nhat on 5/2/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.RecyclerViewHolder> {
    private List<HistoryConstructor> listData = new ArrayList<>();
    Context context;
    public HistoryAdapter(Context context,List<HistoryConstructor> listData) {
        this.context = context;
        this.listData = listData;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.custom_history, viewGroup, false);
        return new HistoryAdapter.RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        HistoryConstructor ip = listData.get(position);
//        holder.stt.setText(ip.getStt());
        holder.bcode.setText(ip.getBcode());
        holder.time.setText(ip.getTime());
        holder.timeleft.setText(ip.getTimeleft());
        holder.reason.setText(ip.getReason());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView stt;
        public TextView bcode;
        public TextView time;
        public TextView timeleft;
        public TextView reason;
        public RecyclerViewHolder(View itemView) {
            super(itemView);

            bcode=(TextView)itemView.findViewById(R.id.tvBCode);
            time=(TextView)itemView.findViewById(R.id.tvTime);
            timeleft=(TextView)itemView.findViewById(R.id.tvTimeLeft);
            reason=(TextView)itemView.findViewById(R.id.tvReason);
        }
    }
    public void removeItem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listData.size());
        notifyDataSetChanged();
    }
}
