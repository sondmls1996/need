package com.needfood.kh;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.Product.ProductDetail;
import com.needfood.kh.SupportClass.PostCL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QRCamera extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {
    private QRCodeReaderView qrCodeReaderView;
    String idsp;
    DataHandle db;
    TextView tvpr, namesel, tvnameprd, tvgia1, tvgia2, dess, tvdv1, tvdv2,txtcode;
    ImageView imgprd;
    RelativeLayout rl;
    List<ListMN> list;
    LinearLayout view1;
    ProgressBar pr1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcamera);
        db = new DataHandle(this);
        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        tvgia1 = (TextView) findViewById(R.id.pr1);
        txtcode = (TextView)findViewById(R.id.txtcode);
        tvnameprd = (TextView) findViewById(R.id.tvname2);
        rl = (RelativeLayout) findViewById(R.id.rlmain);
        rl.setVisibility(View.GONE);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), ProductDetail.class);
                it.putExtra("idprd", idsp);
                startActivity(it);
                finish();
            }
        });
        tvgia2 = (TextView) findViewById(R.id.pr2);
        tvdv1 = (TextView) findViewById(R.id.donvi1);
        dess = (TextView) findViewById(R.id.des);

        tvdv2 = (TextView) findViewById(R.id.donvi2);
        imgprd = (ImageView) findViewById(R.id.imgnews);
        view1 = (LinearLayout) findViewById(R.id.v1);
        pr1 = (ProgressBar) findViewById(R.id.prg1);
        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
        qrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        idsp = text;
        getProductDT();
//        Intent it = new Intent(getApplicationContext(), ProductDetail.class);
//        it.putExtra("idprd",text);
//        startActivity(it);
//       finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }

    private void getProductDT() {
        final String link = getResources().getString(R.string.linkprdde);
        Map<String, String> map = new HashMap<>();
        map.put("idProduct", idsp);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("FFFFUU",response);
                try {

                    view1.setVisibility(View.GONE);
                    txtcode.setVisibility(View.GONE);
                    pr1.setVisibility(View.GONE);
                    rl.setVisibility(View.GONE);

                    JSONObject jo = new JSONObject(response);

                //    String code = jo.getString("code");
                    if(jo.has("code")){
                        txtcode.setVisibility(View.VISIBLE);
                        view1.setVisibility(View.GONE);
                        txtcode.setText(getResources().getString(R.string.noprd));
                    }else {
                        qrCodeReaderView.stopCamera();
                        Intent it = new Intent(getApplicationContext(),ProductDetail.class);
                        it.putExtra("idprd",idsp);
                        startActivity(it);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PostCL get = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }
}
