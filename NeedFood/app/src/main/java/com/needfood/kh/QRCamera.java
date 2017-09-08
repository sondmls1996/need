package com.needfood.kh;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.zxing.Result;
import com.google.zxing.qrcode.QRCodeWriter;
import com.needfood.kh.Constructor.ListMN;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.Product.ProductDetail;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCamera extends AppCompatActivity implements ZXingScannerView.ResultHandler {
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
//        setContentView(R.layout.activity_qrcamera);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
//        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
//        qrCodeReaderView.setOnQRCodeReadListener(this);
//        tvgia1 = (TextView) findViewById(R.id.pr1);
//        txtcode = (TextView) findViewById(R.id.txtcode);
//        tvnameprd = (TextView) findViewById(R.id.tvname2);
//        rl = (RelativeLayout) findViewById(R.id.rlmain);
//        rl.setVisibility(View.GONE);
//        rl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent it = new Intent(getApplicationContext(), ProductDetail.class);
//                it.putExtra("idprd", idsp);
//                startActivity(it);
//                finish();
//            }
//        });
//        tvgia2 = (TextView) findViewById(R.id.pr2);
//        tvdv1 = (TextView) findViewById(R.id.donvi1);
//        dess = (TextView) findViewById(R.id.des);
//
//        tvdv2 = (TextView) findViewById(R.id.donvi2);
//        imgprd = (ImageView) findViewById(R.id.imgnews);
//        view1 = (LinearLayout) findViewById(R.id.v1);
//        pr1 = (ProgressBar) findViewById(R.id.prg1);
//        // Use this function to enable/disable decoding
//        qrCodeReaderView.setQRDecodingEnabled(true);
//
//        // Use this function to change the autofocus interval (default is 5 secs)
//        qrCodeReaderView.setAutofocusInterval(2000L);
//
//        // Use this function to enable/disable Torch
//        qrCodeReaderView.setTorchEnabled(true);
//
//        // Use this function to set front camera preview
//        qrCodeReaderView.setFrontCamera();
//
//        // Use this function to set back camera preview
//        qrCodeReaderView.setBackCamera();
    }

//    @Override
//    public void onQRCodeRead(String text, PointF[] points) {
////        getProductDT(text);
//
//
//    }

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
        // Do something with the result here
        // Log.v("tag", rawResult.getText()); // Prints scan results
        // Log.v("tag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        String text = rawResult.getText();
        if (!text.equals("")) {
//            txtcode.setVisibility(View.GONE);


            Log.d("MMMM", text);
            if (text.contains("{idProduct=")) {
                text = text.substring(11, text.length() - 1);

                qrCodeReaderView.stopCamera();
                getProductDT(text);
            } else {
                //    Toast.makeText(getApplicationContext(),"demo4",Toast.LENGTH_SHORT).show();
//                txtcode.setVisibility(View.VISIBLE);
//                view1.setVisibility(View.GONE);
                diaglog();
            }

        } else {
//            txtcode.setVisibility(View.VISIBLE);
//            view1.setVisibility(View.GONE);
            diaglog();
        }

//        onBackPressed();

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }

    public void diaglog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
        alertDialogBuilder.setMessage(getResources().getString(R.string.noprd));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
//                                Toast.makeText(MainActivity.this,"You clicked yes
//                                        button",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //}
    private void getProductDT(final String idsp) {
        Intent it = new Intent(getApplicationContext(), ProductDetail.class);
        it.putExtra("idprd", idsp);
        it.putExtra("icheck", "id");
        startActivity(it);
        finish();

    }
}
