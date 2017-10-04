package com.needfood.kh.Adapter.ProductDetail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.Constructor.Often2Constructor;
import com.needfood.kh.Constructor.ProductDetail.OftenConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.Product.ProductDetail;
import com.needfood.kh.R;
import com.needfood.kh.Service.BubbleService;
import com.needfood.kh.SupportClass.PostCL;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.needfood.kh.R.id.edquan;

/**
 * Created by Vi on 4/27/2017.
 */

public class OftenAdapter extends  RecyclerView.Adapter<OftenAdapter.RecyclerViewHolder> {
    public static ArrayList<CheckConstructor> arrcheck = new ArrayList<>();
    public List<ListMN> listmn;
    public RecyclerView rcq;
    public String quantity="";
    public int numquan=0;
    private ArrayList<Often2Constructor> arrq;
    public DataHandle db;
    public int tong=0;
    private QuanAdapter quana;
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

                 Dialog dialog = new Dialog(v.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layoutdetail);
                final ProgressBar pgr = (ProgressBar)dialog.findViewById(R.id.prg1);
                final TextView inven = (TextView)dialog.findViewById(R.id.txt_inven);
                final LinearLayout v1 = (LinearLayout)dialog.findViewById(R.id.v1);
                final ImageView imgn = (ImageView)dialog.findViewById(R.id.imgnews);
                final TextView tvname = (TextView)dialog.findViewById(R.id.tvname2);
                final TextView tvgia = (TextView)dialog.findViewById(R.id.pr1);
                final TextView tvdv = (TextView)dialog.findViewById(R.id.donvi1) ;
                 rcq = (RecyclerView)dialog.findViewById(R.id.rcquan);
                arrq = new ArrayList<Often2Constructor>();
                quana= new QuanAdapter(context,arrq);
                rcq.setAdapter(quana);
                StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
                rcq.setLayoutManager(mStaggeredVerticalLayoutManager);
                final EditText edq = (EditText)dialog.findViewById(edquan);
                edq.setText(viewHolder.edo.getText().toString());


                final String link = context.getResources().getString(R.string.linkprdde);
                Map<String, String> map = new HashMap<>();
                map.put("idProduct", ip.getId());
                map.put("type", "");
                Response.Listener<String> response = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FGFG",response);

                        try {

                            JSONObject jo = new JSONObject(response);

                            JSONObject prd = jo.getJSONObject("Product");
                            pgr.setVisibility(View.GONE);
                            v1.setVisibility(View.VISIBLE);
                            JSONArray img = prd.getJSONArray("images");
                            StringBuilder simg = new StringBuilder("http://needfood.webmantan.com" + img.getString(0));
                            Picasso.with(getApplicationContext()).load(simg.toString()).into(imgn);
                            tvname.setText(prd.getString("title"));
                            String tym = prd.getString("typeMoneyId");
                            listmn = db.getMNid(tym);
                            quantity = prd.getString("quantity");
                            if (Integer.parseInt(quantity) < 0) {
                                inven.setText("0");
                            } else {
                                inven.setText(quantity);
                            }
                            for (ListMN lu:listmn){
                                tvgia.setText(NumberFormat.getNumberInstance(Locale.UK).format(Integer.parseInt(prd.getString("price"))) + lu.getMn());
                            }
                            tvdv.setText(prd.getString("nameUnit"));

                            getQuan(ip.getId());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                PostCL pos = new PostCL(link,map,response);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                que.add(pos);
                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        db.addPDR(new CheckConstructor("1",
                                ip.getPrize(), "false", "", "", ip.getBar(), ip.getCode()
                                , ip.getName(),
                                "", ip.getId(), ip.getTymn(),ip.getNmship()));
                        viewHolder.edo.setText("1");
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (edq.getText().toString().equals("")) {
                            inven.setText(quantity);
                            if (db.isProductEmpty(ip.getId()) == false) {
                                db.updatePrd(ip.getId(), "1");
                                viewHolder.edo.setText("1");
                            } else {
                                db.addPDR(new CheckConstructor("1",
                                        ip.getPrize(), "false", "", "", ip.getBar(), ip.getCode()
                                        , ip.getName(),
                                        "", ip.getId(), ip.getTymn(),ip.getNmship()));
                                viewHolder.edo.setText("1");
                            }

                        } else {
                            numquan = Integer.parseInt(quantity) - Integer.parseInt(edq.getText().toString());
                            if (numquan < 0) {
                                inven.setText("0");
                            } else {
                                inven.setText(numquan + "");
                            }
                            if (!db.isProductEmpty(ip.getId())) {
                                db.updatePrd(ip.getId(), edq.getText().toString());
                                viewHolder.edo.setText(edq.getText().toString());
                            } else {
                                db.addPDR(new CheckConstructor(edq.getText().toString(),
                                        ip.getPrize(), "false", "", "", ip.getBar(), ip.getCode()
                                        , ip.getName(),
                                        "", ip.getId(), ip.getTymn(),ip.getNmship()));
                                viewHolder.edo.setText(edq.getText().toString());
                            }

                        }
                        context.startService(new Intent(context, BubbleService.class));
                    }
                };
                edq.addTextChangedListener(textWatcher);
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
                        ProductDetail.listship.remove(position);
                        context.startService(new Intent(context, BubbleService.class));
                    }else{
                        viewHolder.edo.setText("");
                        db.deletePrd(ip.getId());
                        ProductDetail.listship.remove(position);
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
                                ip.getNote(), ip.getId(), ip.getTymn(),ip.getNmship()));
                        context.startService(new Intent(context, BubbleService.class));

                    }


            }
        });

    }

    private void getQuan(String id) {
        final String link = context.getResources().getString(R.string.linkprdat);

        Map<String, String> map = new HashMap<>();
        map.put("idProduct", id );

        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("EEE", response);
                try {
                    JSONArray ja = new JSONArray(response);

                    for (int i = 0; i < ja.length(); i++) {
                        String mn = "";
                        JSONObject jo = ja.getJSONObject(i);
                        JSONObject prd = jo.getJSONObject("Product");
                        JSONArray jaimg = prd.getJSONArray("images");
                        String typemn = prd.getString("typeMoneyId");
                        listmn = db.getMNid(typemn);
                        for (ListMN lu : listmn) {
                            mn = lu.getMn();
                        }
                        Log.d("ert",prd.getString("id")+"\n"+prd.getString("title"));
                        arrq.add(new Often2Constructor("http://needfood.webmantan.com" +
                                jaimg.getString(0), prd.getString("title"),
                                prd.getString("price"), mn, prd.getString("nameUnit"),
                                false, prd.getString("id"), prd.getString("code"),
                                "", prd.getString("id"), prd.getString("moneyShip"), typemn));
                    }
                    quana.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        PostCL get = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
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

