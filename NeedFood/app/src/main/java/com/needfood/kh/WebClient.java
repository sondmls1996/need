package com.needfood.kh;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebClient extends AppCompatActivity {
    WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_client);
        wv = (WebView)findViewById(R.id.wv);
        wv.setWebViewClient(new WebViewClient());
        wv.loadUrl("http://needfood.webmantan.com/getRoleUserAPI");

    }
}
