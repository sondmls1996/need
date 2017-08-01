package com.needfood.kh.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.Constructor.SellingConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;
import com.needfood.kh.SupportClass.ChangeTimestamp;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Vi on 7/28/2017.
 */

public class SellingOutAdapter  extends
        RecyclerView.Adapter<SellingOutAdapter.RecyclerViewHolder> {
    DataHandle db;
    String tymn;
    ChangeTimestamp change;
    Calendar c;
    List<ListMN> list;
    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
    private List<SellingConstructor> listData = new ArrayList<>();
    Context context;

    public SellingOutAdapter(Context context, List<SellingConstructor> listData) {
        this.context = context;
        this.listData = listData;
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView tvname, tvdv, tvdv2, namea, price, price2, vote, unv,txttime;
        public ImageView imageView, imga;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            c = Calendar.getInstance();

            change = new ChangeTimestamp();
            db = new DataHandle(context);
            txttime = (TextView) itemView.findViewById(R.id.txttime);
            tvname = (TextView) itemView.findViewById(R.id.tvname);

            tvdv = (TextView) itemView.findViewById(R.id.unit);

            tvdv2 = (TextView) itemView.findViewById(R.id.unit2);

            namea = (TextView) itemView.findViewById(R.id.tvau);

            price = (TextView) itemView.findViewById(R.id.pr1);

            price2 = (TextView) itemView.findViewById(R.id.pr2);

            vote = (TextView) itemView.findViewById(R.id.vt);

            unv = (TextView) itemView.findViewById(R.id.votecount);


            imageView = (ImageView) itemView.findViewById(R.id.imgnews);
            imga = (ImageView) itemView.findViewById(R.id.imgau);


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
        View itemView = inflater.inflate(R.layout.customtop, viewGroup, false);
        //     new ScaleInAnimation(itemView).animate();
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
        SellingConstructor p = listData.get(position);
        list = db.getMNid(p.getMn());
        long now =p.getNow();
        long tl = p.getTmleft()-now;
        Log.d("SED",tl+"");
        for (ListMN lu : list) {
            tymn = lu.getMn();
        }
        long giocl = tl / 3600;
        long phutcl = (tl % 3600) / 60;
        if (p.getName().length() < 30) {
            viewHolder.tvname.setText(p.getName());
        } else {
            viewHolder.tvname.setText(p.getName().substring(0, 30) + "...");
        }

        viewHolder.tvdv.setText(p.dv);
        viewHolder.txttime.setVisibility(View.VISIBLE);
        viewHolder.txttime.setText(giocl+":"+phutcl);
        viewHolder.tvdv2.setText(p.dv);
        // TextView namea = (TextView)view.findViewById(R.id.tvau);
        viewHolder.namea.setText(p.nameauth);
        //TextView price = (TextView)view.findViewById(R.id.pr1);
        viewHolder.price.setText(NumberFormat.getNumberInstance(Locale.UK).format(Integer.parseInt(p.getPrice())) + tymn);

        ;
        //TextView price2 = (TextView)view.findViewById(R.id.pr2);

        viewHolder.price2.setText(NumberFormat.getNumberInstance(Locale.UK).format(Integer.parseInt(p.getPrice2())) + tymn, TextView.BufferType.SPANNABLE);
        Spannable spannable = (Spannable) viewHolder.price2.getText();
        spannable.setSpan(STRIKE_THROUGH_SPAN, 0, viewHolder.price2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //TextView vote = (TextView)view.findViewById(R.id.vt);
        viewHolder.vote.setText(p.vt);

//        ImageView imageView = (ImageView)view.findViewById(R.id.imgnews);
        //      ImageView imga = (ImageView)view.findViewById(R.id.imgau);

        if (p.linkimg == null) {

        } else {
            Picasso.with(context).load(p.linkimg).into(viewHolder.imageView);
        }
        if (p.getImga() == "") {

        } else {
            Picasso.with(context).load(p.getImga()).into(viewHolder.imga);
        }


    }

    public void removeItem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listData.size());
        notifyDataSetChanged();
    }
}
