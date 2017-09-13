package com.needfood.kh.Barcode;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.zxing.Result;
import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.Product.ProductDetail;
import com.needfood.kh.R;
import com.needfood.kh.SupportClass.PostCL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeCamera extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private QRCodeReaderView qrCodeReaderView;
    String idsp;
    DataHandle db;
    TextView tvpr, namesel, tvnameprd, tvgia1, tvgia2, dess, tvdv1, tvdv2, txtcode;
    ImageView imgprd;
    RelativeLayout rl;
    List<ListMN> list;
    LinearLayout view1;
    ProgressBar pr1;
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

        String text = rawResult.getText();
        Log.d("CHECKBAR", text);
        if (!text.equals("")) {
            getProductDT(text);
        } else {
            diaglog();
        }
    }

    public void diaglog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
        alertDialogBuilder.setMessage(getResources().getString(R.string.noprd));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void getProductDT(final String idsp) {
        final String link = getResources().getString(R.string.linkprdde);
        Map<String, String> map = new HashMap<>();
        map.put("idProduct", idsp);
        map.put("type", "barcode");
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 3 && response != null) {
                    Intent it = new Intent(getApplicationContext(), ProductDetail.class);
                    it.putExtra("idprd", idsp);
                    it.putExtra("icheck", "barcode");
                    startActivity(it);
                    finish();
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(BarcodeCamera.this);
                    dialog.setTitle(getResources().getString(R.string.notif));
                    dialog.setMessage(getResources().getString(R.string.xl)).setCancelable(false).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mScannerView.setResultHandler(BarcodeCamera.this); // Register ourselves as a handler for scan results.
                            mScannerView.startCamera();
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                }
            }
        };
        PostCL get = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }
}
