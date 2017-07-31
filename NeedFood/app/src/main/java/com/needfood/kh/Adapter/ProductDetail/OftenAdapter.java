package com.needfood.kh.Adapter.ProductDetail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.needfood.kh.Constructor.ProductDetail.OftenConstructor;
import com.needfood.kh.Product.ProductDetail;
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
        public TextView tvd;
        public EditText edo;
        public CheckBox cb;
        public RecyclerViewHolder(View itemView) {
            super(itemView);

            img = (ImageView)itemView.findViewById(R.id.imgsug);
            tvName = (TextView)itemView.findViewById(R.id.namesug);
            prize = (TextView)itemView.findViewById(R.id.prizesug);
             tvd = (TextView)itemView.findViewById(R.id.tvdv);
            cb = (CheckBox)itemView.findViewById(R.id.check);
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
        if(ip.getImg()==null){

        }else{
            Picasso.with(context).load(ip.getImg()).into(viewHolder.img);
        }


   //     viewHolder.cb.setTag(ip);
        viewHolder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    arrcheck.add(new CheckConstructor("1",
                            ip.getPrize(),"false",null,null,ip.getBar(),ip.getCode(),
                            ip.getName(),Integer.parseInt(ip.getPrize())+"",
                            ip.getNote(),ip.getId()
                    ));
                    viewHolder.edo.setEnabled(true);

                    ProductDetail.listship.add(Integer.parseInt(ip.getNmship()));
                    viewHolder.edo.setText("1");
                    viewHolder.textWatcher = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                            if(viewHolder.edo.getText().toString().equals("")){

                                if(arrcheck.size()==1){
                                    arrcheck.get(0).setQuanli("1");
                                    arrcheck.get(0).setMoney(Integer.parseInt(ip.getPrize())*Integer.parseInt("1")+"");
                                }else{
                                    for(int i = 0 ; i < arrcheck.size() ; i++){
                                        if(ip.getId().equalsIgnoreCase(arrcheck.get(i).id)){
                                            arrcheck.get(i).setQuanli(viewHolder.edo
                                                    .getText().toString());
                                            arrcheck.get(i).setMoney(Integer.parseInt(ip.getPrize())*Integer.parseInt("1")+"");
                                        }
                                    }

                                }
                                int prdmoney=0;
                                for (int i2 = 0; i2<arrcheck.size();i2++){
                                    prdmoney = Integer.parseInt(arrcheck.get(i2).getMoney())+ prdmoney;
                                }
                                Intent it = new Intent(getApplicationContext(),BubbleService.class);
                                it.putExtra("MN",prdmoney+Integer.parseInt(ProductDetail.priceprd)+"");
                                context.startService(it);
                            }else {
                                    if(arrcheck.size()==1){
                                        arrcheck.get(0).setQuanli(viewHolder.edo.getText().toString());
                                        arrcheck.get(0).setMoney(Integer.parseInt(ip.getPrize())*Integer.parseInt(viewHolder.edo.getText().toString())+"");
                                    }else{
                                        for(int i = 0 ; i < arrcheck.size() ; i++){
                                            if(ip.getId().equalsIgnoreCase(arrcheck.get(i).id)){
                                                arrcheck.get(i).setQuanli(viewHolder.edo.getText().toString());
                                                arrcheck.get(i).setMoney(Integer.parseInt(ip.getPrize())*Integer.parseInt(viewHolder.edo.getText().toString())+"");
                                            }
                                        }

                                    }
                                int prdmoney=0;
                                for (int i2 = 0; i2<arrcheck.size();i2++){
                                    prdmoney = Integer.parseInt(arrcheck.get(i2).getMoney())+ prdmoney;
                                }
                                Intent it = new Intent(getApplicationContext(),BubbleService.class);
                                it.putExtra("MN",prdmoney+Integer.parseInt(ProductDetail.priceprd)+"");
                                context.startService(it);
                            }


                        }
                    };
                    int prdmoney=0;
                    for (int i2 = 0; i2<arrcheck.size();i2++){
                        prdmoney = Integer.parseInt(arrcheck.get(i2).getMoney())+ prdmoney;
                    }
                    Intent it = new Intent(getApplicationContext(),BubbleService.class);
                    it.putExtra("MN",prdmoney+Integer.parseInt(ProductDetail.priceprd)+"");
                    context.startService(it);
                    viewHolder.edo.addTextChangedListener(viewHolder.textWatcher);
                    Log.d("ARRSIZE",arrcheck.size()+"");
                }else {
                    viewHolder.edo.setEnabled(false);
                    viewHolder.edo.removeTextChangedListener(viewHolder.textWatcher);
                    viewHolder.edo.setText(null);
                    if(arrcheck.size()==1){
                        arrcheck.remove(0);
                        ProductDetail.listship.remove(0);
                    }else{
                        for(int i = 0 ; i < arrcheck.size() ; i++){
                            if(ip.getId().equalsIgnoreCase(arrcheck.get(i).id)){
                                arrcheck.remove(i);
                                ProductDetail.listship.remove(i);
                            }
                        }


                    }
                    int prdmoney=0;
                    for (int i2 = 0; i2<arrcheck.size();i2++){
                        prdmoney = Integer.parseInt(arrcheck.get(i2).getMoney())+ prdmoney;
                    }
                    Intent it = new Intent(getApplicationContext(),BubbleService.class);
                    it.putExtra("MN",prdmoney+Integer.parseInt(ProductDetail.priceprd)+"");
                    context.startService(it);

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

