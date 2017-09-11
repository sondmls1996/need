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
        if (!text.equals("")) {


            Log.d("MMMM", text);
            if (text.contains("{idProduct=")) {
                text = text.substring(11, text.length() - 1);
                getProductDT(text);
            } else {
                diaglog();
            }

        } else {
            diaglog();
        }
    }

    public void diaglog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QRCamera.this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.notif));
        alertDialogBuilder.setMessage(getResources().getString(R.string.noprd)).setCancelable(false).setPositiveButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        mScannerView.setResultHandler(QRCamera.this); // Register ourselves as a handler for scan results.
                        mScannerView.startCamera();
                        dialog.cancel();
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
