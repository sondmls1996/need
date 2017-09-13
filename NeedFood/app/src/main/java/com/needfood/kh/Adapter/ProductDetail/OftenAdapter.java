package com.needfood.kh.Adapter.ProductDetail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.needfood.kh.Constructor.ProductDetail.OftenConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;
import com.needfood.kh.Service.BubbleService;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Vi on 4/27/2017.
 */

public class OftenAdapter extends  RecyclerView.Adapter<OftenAdapter.RecyclerViewHolder> {
    public static ArrayList<CheckConstructor> arrcheck = new ArrayList<>();
    public DataHandle db;
    public int tong=0;
    Context mContext = getApplicationContext();
    private List<OftenConstructor> listData = new ArrayList<>();
    Context context;
    public OftenAdapter(Context context,List<OftenConstructor> listData) {
        this.context = context;
        this.listData = listData;
    }



    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView prize;
        public ImageView img;
        public TextWatcher textWatcher;
        public ImageView imgcong,imgtru;
        public TextView tvd;
        public EditText edo;
        public CheckBox cb;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            db = new DataHandle(getApplicationContext());
            img = (ImageView)itemView.findViewById(R.id.imgsug);
            tvName = (TextView)itemView.findViewById(R.id.namesug);
            prize = (TextView)itemView.findViewById(R.id.prizesug);
             tvd = (TextView)itemView.findViewById(R.id.tvdv);
            cb = (CheckBox)itemView.findViewById(R.id.check);
            imgcong = (ImageView) itemView.findViewById(R.id.imgcong);
            imgtru = (ImageView) itemView.findViewById(R.id.imgtru);
            edo = (EditText)itemView.findViewById(R.id.edot);

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
    public void onBindViewHolder(final RecyclerViewHolder viewHolder, final int position) {
        final OftenConstructor ip = listData.get(position);

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
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 Dialog dialog = new Dialog((Activity) v.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layoutdetail);
                dialog.show();
            }
        });
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
//        viewHolder.textWatcher = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                if (viewHolder.edo.getText().toString().equals("")) {
//                        db.deletePrd(ip.getId());
//
//                    context.startService(new Intent(context, BubbleService.class));
//                } else {
//                    if (viewHolder.edo.getText().equals("1")) {
//                        db.deletePrd(ip.getId());
//                        db.addPDR(new CheckConstructor(viewHolder.edo.getText().toString(),
//                                ip.getPrize(), "false", "", "", ip.getBar(), ip.getCode(),
//                                ip.getName(),
//                                ip.getNote(), ip.getId(), ip.getTymn()));
//
//                        context.startService(new Intent(context, BubbleService.class));
//                    } else {
//                        db.updatePrd(ip.getId(), viewHolder.edo.getText().toString());
//                        context.startService(new Intent(context, BubbleService.class));
//                    }
//
//                }
//
//            }
//
//
//        };
//        viewHolder.edo.addTextChangedListener(viewHolder.textWatcher);
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(getApplicationContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layoutdetail);
        dialog.show();
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

