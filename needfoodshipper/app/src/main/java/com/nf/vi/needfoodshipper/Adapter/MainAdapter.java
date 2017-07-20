package com.nf.vi.needfoodshipper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.nf.vi.needfoodshipper.Constructor.ListUserContructor;
import com.nf.vi.needfoodshipper.Constructor.MainConstructor;
import com.nf.vi.needfoodshipper.MainClass.DeliveryActivity;
import com.nf.vi.needfoodshipper.R;
import com.nf.vi.needfoodshipper.Request.TrangThaiRequest;
import com.nf.vi.needfoodshipper.database.DBHandle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Minh Nhat on 4/28/2017.
 */

public class MainAdapter extends
        RecyclerView.Adapter<MainAdapter.RecyclerViewHolder> {
    public String tk, note, stt, stt1, id;
    private List<MainConstructor> listData = new ArrayList<>();
    Context context;
    DBHandle db;
    List<ListUserContructor> list;

    public MainAdapter(Context context, List<MainConstructor> listData) {
        this.context = context;
        this.listData = listData;
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rl;
        public RelativeLayout btnacc;
        public RelativeLayout btndn;
        public Button btnSend;
        public Button btnDeny;
        public LinearLayout lnb;
        public LinearLayout lntop;
        public TextView ord;
        public TextView loc, textstt;
        public ImageView imgstt;
        public TextView cta;
        public TextView rec;
        public TextView timel;
        public TextView paym;
        public EditText edfedd;


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
            lntop = (LinearLayout) itemView.findViewById(R.id.lntop);
            timel = (TextView) itemView.findViewById(R.id.tvtm);
            paym = (TextView) itemView.findViewById(R.id.tvpay);
            btnacc = (RelativeLayout) itemView.findViewById(R.id.btnac2);
            btndn = (RelativeLayout) itemView.findViewById(R.id.btndn2);
            imgstt = (ImageView) itemView.findViewById(R.id.imgstt);
            textstt = (TextView) itemView.findViewById(R.id.textstt);
            lnb = (LinearLayout) itemView.findViewById(R.id.lnbot2);
            rl = (RelativeLayout) itemView.findViewById(R.id.rlfeed);
            btnSend = (Button) itemView.findViewById(R.id.btnSend);
            btnDeny = (Button) itemView.findViewById(R.id.btnDeny);
            edfedd = (EditText) itemView.findViewById(R.id.edfedd);
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
        View itemView = inflater.inflate(R.layout.cusmainitem, viewGroup, false);
        return new RecyclerViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder viewHolder, int position) {
        final MainConstructor ip = listData.get(position);
        id = ip.getId();
        stt1 = ip.getStt();

        viewHolder.ord.setText(ip.getOrder());
        viewHolder.loc.setText(ip.getLc());
        viewHolder.cta.setText(ip.getCt());
        viewHolder.rec.setText(ip.getRe());
        viewHolder.timel.setText(ip.getTl());
        viewHolder.paym.setText(ip.getPay());
        viewHolder.lntop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, DeliveryActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                it.putExtra("id", ip.getId());
                it.putExtra("order", ip.getOrder());
                it.putExtra("lc", ip.getLc());
                it.putExtra("ct", ip.getCt());
                it.putExtra("re", ip.getRe());
                it.putExtra("tl", ip.getTl());
                it.putExtra("pay", ip.getPay());
                it.putExtra("moneyship", ip.getMoneyship());
                it.putExtra("stt", ip.getStt());
                it.putExtra("code", ip.getCode());
                context.startActivity(it);
            }
        });
        if (stt1.equals("waiting")) {
            viewHolder.lnb.setVisibility(View.GONE);
            viewHolder.imgstt.setImageDrawable(context.getResources().getDrawable(R.drawable.grdot));
            viewHolder.textstt.setText(context.getResources().getString(R.string.wait));
            viewHolder.textstt.setTextColor(context.getResources().getColor(R.color.greenL));
        }
        viewHolder.btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.rl.setVisibility(View.GONE);
                viewHolder.btnacc.setVisibility(View.VISIBLE);
            }
        });
        viewHolder.btndn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.rl.setVisibility(View.VISIBLE);
                viewHolder.btnacc.setVisibility(View.GONE);

            }
        });
        viewHolder.btnacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.lnb.setVisibility(View.GONE);
                viewHolder.imgstt.setImageDrawable(context.getResources().getDrawable(R.drawable.grdot));
                viewHolder.textstt.setText(context.getResources().getString(R.string.wait));
                viewHolder.textstt.setTextColor(context.getResources().getColor(R.color.greenL));
                Toast.makeText(context, "ACC", Toast.LENGTH_SHORT).show();
                note = "";
                stt = "waiting";
                sendSV();
            }
        });
        viewHolder.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note = viewHolder.edfedd.getText().toString();
                stt = "cancel";
                sendSV();
                viewHolder.rl.setVisibility(View.GONE);
            }
        });
    }

    private void sendSV() {

        //  String link = getResources().getString(R.string.saveStatusOrderAPI);
        String link = "http://needfood.webmantan.com/saveStatusOrderAPI";
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("CODE", response);
                    JSONObject json = new JSONObject(response);
                    String code = json.getString("code");


                    if (code.equals("0")) {
//                            Intent i = new Intent(SentPassEmail.this, DangNhapActivity.class);
//                            startActivity(i);
                    } else {
//                        Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        TrangThaiRequest save = new TrangThaiRequest(tk, note, stt, id,"", link, response);
        RequestQueue qe = Volley.newRequestQueue(context);
        qe.add(save);

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