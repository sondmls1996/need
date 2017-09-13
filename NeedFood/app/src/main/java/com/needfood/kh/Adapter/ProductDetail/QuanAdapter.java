package com.needfood.kh.Adapter.ProductDetail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.needfood.kh.Constructor.ProductDetail.OftenConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;
import com.needfood.kh.Service.BubbleService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vi on 4/27/2017.
 */

public class QuanAdapter extends  RecyclerView.Adapter<QuanAdapter.RecyclerViewHolder> {

private List<OftenConstructor> listData = new ArrayList<>();
        Context context;
    public int tong = 0;

    public DataHandle db;
public QuanAdapter(Context context,List<OftenConstructor> listData) {
        this.context = context;
        this.listData = listData;
        }



public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView tvName;
    public TextView stt;
    public EditText edo;
    public ImageView imgtru,imgcong;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        db = new DataHandle(context);
        imgtru = (ImageView)itemView.findViewById(R.id.imgtru);
        imgcong = (ImageView)itemView.findViewById(R.id.imgcong);
        edo = (EditText)itemView.findViewById(R.id.edot);
      //  tvName = (TextView)itemView.findViewById(R.id.imgcong);

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
        View itemView = inflater.inflate(R.layout.customprdoften, viewGroup, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder viewHolder, int position) {
        final OftenConstructor ip = listData.get(position);
        viewHolder.imgtru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!viewHolder.edo.getText().toString().equals("")&&!viewHolder.edo.getText().toString().equals("0")){
                    tong=Integer.parseInt(viewHolder.edo.getText().toString())-1;
                    viewHolder.edo.setText(tong+"");
                    db.updatePrd(ip.getId(), viewHolder.edo.getText().toString());
                    context.startService(new Intent(context, BubbleService.class));
                }else if(viewHolder.edo.getText().toString().equals("1")){
                    tong=Integer.parseInt(viewHolder.edo.getText().toString())-1;
                    viewHolder.edo.setText(tong+"");
                    db.deletePrd(ip.getId());
                    context.startService(new Intent(context, BubbleService.class));
                }else{
                    viewHolder.edo.setText("");
                    db.deletePrd(ip.getId());
                    context.startService(new Intent(context, BubbleService.class));
                }
            }
        });
        viewHolder.imgcong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!viewHolder.edo.getText().toString().equals("")){
                    tong=Integer.parseInt(viewHolder.edo.getText().toString())+1;
                    viewHolder.edo.setText(tong+"");
                    db.updatePrd(ip.getId(), viewHolder.edo.getText().toString());
                    context.startService(new Intent(context, BubbleService.class));
                }else{

                    viewHolder.edo.setText("1");
                    db.addPDR(new CheckConstructor(viewHolder.edo.getText().toString(),
                            ip.getPrize(), "false", "", "", ip.getBar(), ip.getCode(),
                            ip.getName(),
                            ip.getNote(), ip.getId(), ip.getTymn()));
                    context.startService(new Intent(context, BubbleService.class));
                }
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
