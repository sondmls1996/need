package com.needfood.kh.Adapter.ProductDetail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.needfood.kh.Constructor.Often2Constructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;
import com.needfood.kh.Service.BubbleService;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Vi on 4/27/2017.
 */

public class QuanAdapter extends  RecyclerView.Adapter<QuanAdapter.RecyclerViewHolder> {

private List<Often2Constructor> listDataa = new ArrayList<>();
        Context context;
    public int tong = 0;

    public DataHandle db;
public QuanAdapter(Context context,List<Often2Constructor> listData) {
        this.context = context;
        this.listDataa = listData;
        }



public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView tvName;
    public TextView stt;
    public TextView prize,tvd;
    private EditText edo;
    private ImageView imgtru,imgcong,img;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        db = new DataHandle(context);
        img = (ImageView)itemView.findViewById(R.id.imgsug);
        tvName = (TextView)itemView.findViewById(R.id.namesug);
        prize = (TextView)itemView.findViewById(R.id.prizesug);
        tvd = (TextView)itemView.findViewById(R.id.tvdv);
        imgtru = (ImageView)itemView.findViewById(R.id.imgtru);
        imgcong = (ImageView)itemView.findViewById(R.id.imgcong);
        edo = (EditText)itemView.findViewById(R.id.edot);
      //  tvName = (TextView)itemView.findViewById(R.id.imgcong);

    }

}
    @Override
    public int getItemCount() {
        return listDataa.size();
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
        final Often2Constructor ip = listDataa.get(position);
        Log.d("FFFFG",ip.getId());
        if (ip.getName() == null) {
            viewHolder.tvName.setText("Chờ xử lý");
        } else {
            viewHolder.tvName.setText(ip.getName());
        }
        if (ip.getPrize() == null) {
            viewHolder.prize.setText("Chờ xử lý");
        } else {
            viewHolder.prize.setText(NumberFormat.getNumberInstance(Locale.UK).format(Integer.parseInt(ip.getPrize())) + ip.getMn());
            viewHolder.tvd.setText(ip.getDv());
        }
        if (ip.getImg() == null) {

        } else {
            Picasso.with(context).load(ip.getImg()).into(viewHolder.img);
        }
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
        listDataa.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listDataa.size());
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for item view of list
     */

}
