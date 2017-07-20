package com.nf.vi.needfoodshipper.MainClass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;

import com.nf.vi.needfoodshipper.Adapter.HistoryAdapter;
import com.nf.vi.needfoodshipper.Adapter.MainAdapter;
import com.nf.vi.needfoodshipper.Constructor.HistoryConstructor;
import com.nf.vi.needfoodshipper.Constructor.MainConstructor;
import com.nf.vi.needfoodshipper.R;
import com.nf.vi.needfoodshipper.SupportClass.WrapSliding;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    WrapSliding wrap;
    private Spinner spthang;
    public String[] Dsthang = {"1", "2", "3","4","5","6","7","8","9","10","11","12"};
    private String ketqua = "";
    HistoryAdapter adapterh;
    ArrayList<HistoryConstructor> arr;
    private RecyclerView rcvHistory;
    private TextView tvTitle,thongbao;
    private ImageView imghand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

//        setTitle("DEAL HISTORY");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        wrap = (WrapSliding) findViewById(R.id.slide);
        // code tim kiem
        thongbao = (TextView) findViewById(R.id.textflit);
        imghand = (ImageView) findViewById(R.id.imgflit);


        tvTitle=(TextView) findViewById(R.id.tvTitle) ;
        tvTitle.setText("DEAL HISTORY");

        spthang = (Spinner) findViewById(R.id.spthang);
        rcvHistory=(RecyclerView)findViewById(R.id.rcvHistory) ;


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Dsthang);

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);


        spthang.setAdapter(adapter);

        spthang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
//                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                ketqua = Dsthang[position];
//


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        arr = new ArrayList<>();
        adapterh = new HistoryAdapter(getApplicationContext(),arr);
        rcvHistory.setAdapter(adapterh);
        rcvHistory.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        arr.add(new HistoryConstructor("1","VN 37122","13:12","12:24","N/A"));
        arr.add(new HistoryConstructor("1","VN 37122","13:12","12:24","N/A"));
        arr.add(new HistoryConstructor("1","VN 37122","13:12","12:24","N/A"));
        arr.add(new HistoryConstructor("1","VN 37122","13:12","12:24","N/A"));
        arr.add(new HistoryConstructor("1","VN 37122","13:12","12:24","N/A"));
        arr.add(new HistoryConstructor("1","VN 37122","13:12","12:24","N/A"));
        arr.add(new HistoryConstructor("1","VN 37122","13:12","12:24","N/A"));
        arr.add(new HistoryConstructor("1","VN 37122","13:12","12:24","N/A"));
        arr.add(new HistoryConstructor("1","VN 37122","13:12","12:24","N/A"));
        arr.add(new HistoryConstructor("1","VN 37122","13:12","12:24","N/A"));
        arr.add(new HistoryConstructor("1","VN 37122","13:12","12:24","N/A"));
        arr.add(new HistoryConstructor("1","VN 37122","13:12","12:24","N/A"));
        wrap.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                imghand.setImageDrawable(getResources().getDrawable(R.drawable.downb));
                thongbao.setText("Kéo xuống để đóng bộ lọc");
            }
        });
        wrap.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
//                fab.setVisibility(View.VISIBLE);
                imghand.setImageDrawable(getResources().getDrawable(R.drawable.upb));
                thongbao.setText("Kéo lên để mở bộ lọc");
            }
        });

    }

}
