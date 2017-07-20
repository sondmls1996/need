package com.needfood.kh.News;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.needfood.kh.Adapter.NewsAdapter;
import com.needfood.kh.Constructor.NewsConstructor;
import com.needfood.kh.R;

import java.util.ArrayList;

public class TopDeal extends AppCompatActivity {
    RecyclerView lv;
    NewsAdapter adapter;
    ArrayList<NewsConstructor> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_deal);


    }
}
