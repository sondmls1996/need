package com.nf.vi.needfoodshipper.Adapter;

import android.app.Activity;
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
import com.nf.vi.needfoodshipper.Constructor.WaittingContructor;
import com.nf.vi.needfoodshipper.MainClass.DeliveryActivity;
import com.nf.vi.needfoodshipper.MainClass.MainActivity;
import com.nf.vi.needfoodshipper.MainClass.WaittingActivity;
import com.nf.vi.needfoodshipper.R;
import com.nf.vi.needfoodshipper.Request.TrangThaiRequest;
import com.nf.vi.needfoodshipper.database.DBHandle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Minh Nhat on 7/19/2017.
 */

public class WaittingAdapter extends
        RecyclerView.Adapter<WaittingAdapter.RecyclerViewHolder> {
    public String tk, note, stt, stt1, id;
    private List<WaittingContructor> listData = new ArrayList<>();
    Context context;
    DBHandle db;
    String id2;
    List<ListUserContructor> list;

    public WaittingAdapter(Context context, List<WaittingContructor> listData) {
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

            imgstt = (ImageView) itemView.findViewById(R.id.imgstt);
            textstt = (TextView) itemView.findViewById(R.id.textstt);


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
        View itemView = inflater.inflate(R.layout.customwaitting, viewGroup, false);
        return new WaittingAdapter.RecyclerViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final WaittingAdapter.RecyclerViewHolder viewHolder, int position) {
        final WaittingContructor ip = listData.get(position);
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
                Intent intent = new Intent(context, DeliveryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", ip.getId());
                intent.putExtra("order", ip.getOrder());
                intent.putExtra("lc", ip.getLc());
                intent.putExtra("ct", ip.getCt());
                intent.putExtra("re", ip.getRe());
                intent.putExtra("tl", ip.getTl());
                intent.putExtra("pay", ip.getPay());
                intent.putExtra("moneyship", ip.getMoneyship());
                intent.putExtra("stt", ip.getStt());
                intent.putExtra("code", ip.getCode());
                intent.putExtra("listsanpham", ip.getListsanpham());

                context.startActivity(intent);

             //  System.exit(0);
            }
        });
        if (stt1.equals("waiting")) {
//            viewHolder.lnb.setVisibility(View.GONE);
            viewHolder.imgstt.setImageDrawable(context.getResources().getDrawable(R.drawable.grdot));
            viewHolder.textstt.setText(context.getResources().getString(R.string.wait));
            viewHolder.textstt.setTextColor(context.getResources().getColor(R.color.greenL));
        }
//        viewHolder.btnDeny.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewHolder.rl.setVisibility(View.GONE);
//                viewHolder.btnacc.setVisibility(View.VISIBLE);
//            }
//        });
//        viewHolder.btndn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewHolder.rl.setVisibility(View.VISIBLE);
//                viewHolder.btnacc.setVisibility(View.GONE);
//
//            }
//        });
//        viewHolder.btnacc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewHolder.lnb.setVisibility(View.GONE);
//                viewHolder.imgstt.setImageDrawable(context.getResources().getDrawable(R.drawable.grdot));
//                viewHolder.textstt.setText(context.getResources().getString(R.string.wait));
//                viewHolder.textstt.setTextColor(context.getResources().getColor(R.color.greenL));
//                Toast.makeText(context, "ACC", Toast.LENGTH_SHORT).show();
//                note = "";
//                stt = "waiting";
//                id2 = ip.getId();
//                sendSV();
//            }
//        });
//        viewHolder.btnSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                note = viewHolder.edfedd.getText().toString();
//                id2 = ip.getId();
//                stt = "cancel";
//                sendSV();
//                viewHolder.rl.setVisibility(View.GONE);
//            }
//        });
    }

//    private void sendSV() {
//
//        //  String link = getResources().getString(R.string.saveStatusOrderAPI);
//        String link = "http://needfood.webmantan.com/saveStatusOrderAPI";
//        Response.Listener<String> response = new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    Log.d("CODE", response);
//                    JSONObject json = new JSONObject(response);
//                    String code = json.getString("code");
//
//
//                    if (code.equals("0")) {
//                        Intent i = new Intent(context, MainActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(i);
//                    } else {
//                        Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        TrangThaiRequest save = new TrangThaiRequest(tk, note, stt, id2, "", link, response);
//        RequestQueue qe = Volley.newRequestQueue(context);
//        qe.add(save);
//
//    }

    public void removeItem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listData.size());
        notifyDataSetChanged();
    }
}

/**
 * ViewHolder for item view of list
 */


