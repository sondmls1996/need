package com.needfood.kh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.needfood.kh.Adapter.ProductDetail.CheckConstructor;
import com.needfood.kh.Constructor.ProductDetail.OftenConstructor;
import com.needfood.kh.Constructor.UpdateConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;
import com.needfood.kh.Service.BubbleService;
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
    public DataHandle db;
    public int tong = 0;
    public ArrayList<UpdateConstructor> arrud;
    private List<OftenConstructor> listData = new ArrayList<>();
    Context context;
    public MenuBrandAdapter(Context context,List<OftenConstructor> listData) {
        this.context = context;
        this.listData = listData;
    }



    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvgia,seltime2;
        public TextView tvunit;
        public CheckBox cb;
        public TextWatcher textWatcher;
        public EditText edb;
        public TextView stt;
        public ImageView rv,pl;
        public ImageView img;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            db = new DataHandle(context);
            cb = (CheckBox)itemView.findViewById(R.id.cbbrand);
            tvName = (TextView)itemView.findViewById(R.id.namemn);
            tvgia = (TextView)itemView.findViewById(R.id.mnmn);
            tvunit = (TextView)itemView.findViewById(R.id.unitmn);
            img = (ImageView)itemView.findViewById(R.id.imgmn);
            rv = (ImageView)itemView.findViewById(R.id.rm);
            pl = (ImageView)itemView.findViewById(R.id.pl);
            edb = (EditText)itemView.findViewById(R.id.idspb);
            seltime2 = (TextView)itemView.findViewById(R.id.seltime2);

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
    public void onBindViewHolder(final RecyclerViewHolder viewHolder, int position) {
        final OftenConstructor ip = listData.get(position);
        viewHolder.tvName.setText(ip.getName());
        viewHolder.tvgia.setText(NumberFormat.getNumberInstance(Locale.UK).format(Integer.parseInt(ip.getPrize()))+ip.getMn());
        viewHolder.tvunit.setText(ip.getDv());
        if(ip.getImg()!=null){
            Picasso.with(context).load(ip.getImg()).into(viewHolder.img);
        }
        if(ip.getSellend()!=0){
            long now =ip.getTimen();
            long tl = ip.getSellend()-now;
            if(tl>0){
                long giocl = tl / 3600;
                long phutcl = (tl % 3600) / 60;
                viewHolder.seltime2.setVisibility(View.VISIBLE);

                viewHolder.seltime2.setText(giocl+":"+phutcl);
            }
        }
        viewHolder.rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!viewHolder.edb.getText().toString().equals("")&&!viewHolder.edb.getText().toString().equals("0")){
                    tong=Integer.parseInt(viewHolder.edb.getText().toString())-1;
                    viewHolder.edb.setText(tong+"");
                    arrud = new ArrayList<>();
                    arrud.add(new UpdateConstructor(DataHandle.QUAN,viewHolder.edb.getText().toString()));
                    db.updatePrd(ip.getId(), arrud);
                    context.startService(new Intent(context, BubbleService.class));
                }else if(viewHolder.edb.getText().toString().equals("0")){
                    viewHolder.edb.setText("");
                    db.deletePrd(ip.getId());
                    context.startService(new Intent(context, BubbleService.class));
                }
            }
        });
        viewHolder.pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!viewHolder.edb.getText().toString().equals("")){
                    tong=Integer.parseInt(viewHolder.edb.getText().toString())+1;
                    viewHolder.edb.setText(tong+"");
                    arrud = new ArrayList<>();
                    arrud.add(new UpdateConstructor(DataHandle.QUAN,viewHolder.edb.getText().toString()));
                    db.updatePrd(ip.getId(), arrud);
                    context.startService(new Intent(context, BubbleService.class));
                }else{

                    viewHolder.edb.setText("1");
                    db.addPDR(new CheckConstructor(viewHolder.edb.getText().toString(),
                            ip.getPrize(), "false", "", "", ip.getBar(), ip.getCode(),
                            ip.getName(),
                            ip.getNote(), ip.getId(), ip.getTymn(),ip.getNmship()));
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
