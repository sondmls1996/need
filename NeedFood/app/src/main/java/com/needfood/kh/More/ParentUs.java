package com.needfood.kh.More;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.needfood.kh.R;

public class ParentUs extends AppCompatActivity {
    WebView myWebView;

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
        myWebView = (WebView) findViewById(R.id.webview1);
        myWebView.setWebViewClient(new MyBrowser());
        String ac = "http://needfood.webmantan.com/getContactPartnerAPI";
        myWebView.loadUrl(ac);
        myWebView.getSettings().setLoadsImagesAutomatically(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
