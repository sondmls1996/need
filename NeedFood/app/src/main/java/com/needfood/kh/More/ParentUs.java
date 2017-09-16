package com.needfood.kh.More;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.needfood.kh.Constructor.InfoConstructor;
import com.needfood.kh.Database.DataHandle;
import com.needfood.kh.R;
import com.needfood.kh.StartActivity;
import com.needfood.kh.SupportClass.DialogUtils;
import com.needfood.kh.SupportClass.PostCL;
import com.needfood.kh.SupportClass.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParentUs extends AppCompatActivity implements View.OnClickListener {
    WebView myWebView;
    EditText edtit,edcont;
    Button btnok,btnc;
    DataHandle db;
    List<InfoConstructor> listu;
    String access;
    Session ses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_us);
        ImageView imgb = (ImageView) findViewById(R.id.immgb);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView txt = (TextView) findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.Pwu));
        ses = new Session(getApplicationContext());
        db = new DataHandle(getApplicationContext());
        if(ses.loggedin()){
            listu = db.getAllInfor();
            for (InfoConstructor lu:listu){
                access = lu.getAccesstoken();
            }
        }

        edtit = (EditText)findViewById(R.id.edtitle);
        edcont = (EditText)findViewById(R.id.edcont);
        btnok = (Button)findViewById(R.id.btnok);
        btnc = (Button)findViewById(R.id.btncancel);
        btnok.setOnClickListener(this);
        btnc.setOnClickListener(this);

        myWebView = (WebView) findViewById(R.id.webview1);
        myWebView.setWebViewClient(new MyBrowser());
        String ac = "http://needfood.webmantan.com/getContactPartnerAPI";
        myWebView.loadUrl(ac);
        myWebView.getSettings().setLoadsImagesAutomatically(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnok:
                postRe();
                break;
            case R.id.btncancel:
                finish();
                break;
        }
    }

    private void postRe() {
        final ProgressDialog pro = DialogUtils.show(this, getResources().getString(R.string.wait));
        String link = getResources().getString(R.string.linkFeedback);
        if(ses.loggedin()){
            if(edcont.getText().toString().equals("")){
                pro.dismiss();
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.wrreg),Toast.LENGTH_SHORT).show();
            }else{
                Map<String,String> map = new HashMap<>();
                map.put("accessToken",access);
                map.put("title",edtit.getText().toString());
                map.put("content",edcont.getText().toString());
                Response.Listener<String> response = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jo = new JSONObject(response);
                            String code = jo.getString("code");
                            if(code.equals("0")){
                            pro.dismiss();
                                Toast.makeText(getApplicationContext(),getResources().getString(R.string.succ),Toast.LENGTH_SHORT).show();
                            }else if(code.equals("-1")){
                                pro.dismiss();
                                taoMotAlertDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                PostCL po = new PostCL(link,map,response);
                RequestQueue que = Volley.newRequestQueue(getApplicationContext());
                que.add(po);
            }

        }else{
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.tbao),Toast.LENGTH_SHORT).show();
        }

    }
    private AlertDialog taoMotAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Thiết lập tiêu đề hiển thị
        builder.setTitle(getResources().getString(R.string.er));
        //Thiết lập thông báo hiển thị
        builder.setMessage(getResources().getString(R.string.lostss));
        builder.setCancelable(false);
        //Tạo nút Chu hang
        builder.setNegativeButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        db.deleteInfo();
                        db.deleteAll();
                        ses = new Session(getBaseContext());
                        ses.setLoggedin(false);
                        Intent i = new Intent(getApplicationContext(), StartActivity.class);
                        startActivity(i);
                        finish();
                        //  db.deleteAllPRD();
                    }
                });
        AlertDialog dialog = builder.create();
        return dialog;
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
