package com.nf.vi.needfoodshipper.MainClass;

import android.Manifest;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.nf.vi.needfoodshipper.Adapter.MainAdapter;
import com.nf.vi.needfoodshipper.Constructor.ListUserContructor;
import com.nf.vi.needfoodshipper.Constructor.MainConstructor;
import com.nf.vi.needfoodshipper.R;
import com.nf.vi.needfoodshipper.Request.OrderRequest;
import com.nf.vi.needfoodshipper.Request.SaveGpsRequest;
import com.nf.vi.needfoodshipper.SupportClass.TransImage;
import com.nf.vi.needfoodshipper.database.DBHandle;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.R.id.tabhost;

public class MainActivity extends TabActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private ImageView imgShipper;

    private TextView tvTitle, tvTenShiper;
    MainAdapter adapter;
    ArrayList<MainConstructor> arr;

    private List<MainConstructor> ld;
    RecyclerView rc;
    private DBHandle db;
    private List<ListUserContructor> list;
    String fullName, accessToken;
    SwipeRefreshLayout swipeRefresh;
    private TabHost tabHost;
    private LinearLayout lnYourInformation, lnHistory, lnSettings;

    GoogleMap gg;
    double la = 0, lo = 0, la2, lo2;
    GoogleApiClient client;
    LocationManager locationManager;
    Location l2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHandle(this);
        list = db.getAllUser();
        for (ListUserContructor nu : list) {
            fullName = nu.getFullName();
            accessToken = nu.getAccessToken();

        }

        final TabHost host = (TabHost) findViewById(tabhost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec1 = host.newTabSpec("Tab One");
        spec1.setIndicator("Chi tiết");

        Intent it = new Intent(this, NewDealActivity.class);
        spec1.setContent(it);
        host.addTab(spec1);

        //Tab 2
        TabHost.TabSpec spec = host.newTabSpec("Tab Two");

        spec.setIndicator("Chỉ đường");
        Intent it1 = new Intent(this, WaittingActivity.class);
        spec.setContent(it1);
//        TextView tv = (TextView) host.getTabWidget().findViewById(android.R.id.title); //Unselected Tabs
//        tv.setTextColor(Color.parseColor("#ffffff"));

        host.addTab(spec);
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {

                for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
                    host.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.red)); // unselected
                    TextView tv = (TextView) host.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
                    tv.setTextColor(Color.parseColor("#ffffff"));
                }

                host.getTabWidget().getChildAt(host.getCurrentTab()).setBackgroundColor(getResources().getColor(R.color.darkred)); // selected
                TextView tv = (TextView) host.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
                tv.setTextColor(Color.parseColor("#ffffff"));
            }
        });
        host.setCurrentTab(0);
//        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.SwipeRefresh);
//
//        rc = (RecyclerView) findViewById(R.id.rcv);
//        ld = new ArrayList<>();
//        adapter = new MainAdapter(getApplicationContext(), ld);
//        rc.setAdapter(adapter);
//        rc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        rc.addOnItemTouchListener(
//                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override public void onItemClick(View view, int position) {
//
//                        Intent it = new Intent(getApplicationContext(), DeliveryActivity.class);
////                        it.putExtra("id",ld.get(position).getId());
////                        it.putExtra("order",ld.get(position).getOrder());
////                        it.putExtra("lc",ld.get(position).getLc());
////                        it.putExtra("ct",ld.get(position).getCt());
////                        it.putExtra("re",ld.get(position).getRe());
////                        it.putExtra("tl",ld.get(position).getTl());
////                        it.putExtra("pay",ld.get(position).getPay());
////                        it.putExtra("stt",ld.get(position).getStt());
//                        startActivity(it);
////                      Toast.makeText(getApplicationContext(), ld.get(position).getOrder(), Toast.LENGTH_SHORT).show();
//                        // TODO Handle item click
//                    }
//                })
//        );

//        swipeRefresh.setOnRefreshListener(this);
//        order();
        buildAPI();
        if (client != null) {
            client.connect();
        } else {
            Toast.makeText(getApplicationContext(), "API dissconnect", Toast.LENGTH_SHORT).show();
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        //chinh thoi gian cap nhat toa do
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);

        lnYourInformation = (LinearLayout) findViewById(R.id.lnYourInformation);
        lnHistory = (LinearLayout) findViewById(R.id.lnHistory);
        lnSettings = (LinearLayout) findViewById(R.id.lnSettings);





//        arr.add(new MainConstructor("3kg Rau", "102 Nguyen Chi Thanh - Ha Noi", "0123456789", "Nguyen Van A", "30 mins", "No"));
//        arr.add(new MainConstructor("3kg Rau", "102 Nguyen Chi Thanh - Ha Noi", "0123456789", "Nguyen Van A", "30 mins", "No"));
//        arr.add(new MainConstructor("3kg Rau", "102 Nguyen Chi Thanh - Ha Noi", "0123456789", "Nguyen Van A", "30 mins", "No"));
//        arr.add(new MainConstructor("3kg Rau", "102 Nguyen Chi Thanh - Ha Noi", "0123456789", "Nguyen Van A", "30 mins", "No"));
//        arr.add(new MainConstructor("3kg Rau", "102 Nguyen Chi Thanh - Ha Noi", "0123456789", "Nguyen Van A", "30 mins", "No"));
//        adapter.notifyDataSetChanged();

        lnYourInformation.setOnClickListener(this);
        lnHistory.setOnClickListener(this);
        lnSettings.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("Home");
//        setSupportActionBar(toolbar);
//        toolbar.
//        setTitle("Home");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        imgShipper = (ImageView) header.findViewById(R.id.imgShipper);
        tvTenShiper = (TextView) header.findViewById(R.id.tvTenShpper);
        tvTenShiper.setText(fullName);
        Picasso.with(getApplicationContext()).load(R.drawable.daidien).transform(new TransImage()).into(imgShipper);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == lnYourInformation) {
            startActivity(new Intent(getApplicationContext(), YourInformationActivity.class));
        }
        if (v == lnHistory) {
            startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
        }
        if (v == lnSettings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        }

    }

//    private void order() {
////        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.F_REF), Context.MODE_PRIVATE);
////        dvtoken = sharedPreferences.getString(getString(R.string.F_CM), "");
//        String page = "1";
//        final String link = getResources().getString(R.string.getListOrderShiperAPI);
//
//        Response.Listener<String> response = new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                   Log.d("GG", response);
//                    JSONArray arr = new JSONArray(response);
//                    for (int i = 0; i < arr.length(); i++) {
//                        StringBuilder sb = new StringBuilder();
//                        String money;
//                        JSONObject json = arr.getJSONObject(i);
//                        JSONObject Order = json.getJSONObject("Order");
//
//                        JSONObject listProduct = Order.getJSONObject("listProduct");
//                        JSONObject infoOrder = Order.getJSONObject("infoOrder");
//                        JSONObject infoCustomer = Order.getJSONObject("infoCustomer");
//
//
//                        Iterator<String> ite = listProduct.keys();
//
//                        while (ite.hasNext()) {
//                            String key = ite.next();
//
//                            JSONObject idx = listProduct.getJSONObject(key);
//                            sb.append((idx.getString("quantity") + idx.getString("title")) + ";" + "\t");
//
//
//                        }
//                        String timeShiper = infoOrder.getString("timeShiper");
//
//                        String fullName = infoCustomer.getString("fullName");
//                        String fone = infoCustomer.getString("fone");
//                        String address = infoCustomer.getString("address");
//                        String id = Order.getString("id");
//                        String status = Order.getString("status");
//
//                        Log.d("hh", fullName);
//                        ld.add(new MainConstructor(id, sb.toString(), address, fone, fullName, timeShiper, infoOrder.getString("totalMoneyProduct"),status));
//
//
//                    }
//                    adapter.notifyDataSetChanged();
//                    swipeRefresh.setRefreshing(false);
//
//                } catch (JSONException e1) {
//                    e1.printStackTrace();
//                }
//
//            }
//        };
//
//        OrderRequest loginRequest = new OrderRequest(page, accessToken, link, response);
//        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
//        queue.add(loginRequest);
//    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            la = location.getLatitude();
            lo = location.getLongitude();
//            Toast.makeText(getApplicationContext(), "Location change" + "\n" + "la: " + la + "\n" + "lo: " + lo, Toast.LENGTH_SHORT).show();
//            String code = edtcode.getText().toString();
            guitoado();


        } else {

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getApplicationContext(), "Da bat gps", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getApplicationContext(), "Da tat gps", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        l2 = LocationServices.FusedLocationApi.getLastLocation(client);
        if (l2 != null) {
            la = l2.getLatitude();
            lo = l2.getLongitude();
//           Toast.makeText(getApplicationContext(), "Location Connected" + "\n" + "la: " + la + "\n" + "lo: " + lo, Toast.LENGTH_SHORT).show();
//            Log.d("TOADO", String.valueOf(la)+"\n"+String.valueOf(lo));
            guitoado();

        } else {
//            Toast.makeText(getApplicationContext(), "Khong thay vi tri", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected synchronized void buildAPI() {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void guitoado() {
        String la1 = String.valueOf(la);
        String lo1 = String.valueOf(lo);
        Log.d("TEST", accessToken + "\n" + la1 + "\n" + lo1 + "\n");
        String link = getResources().getString(R.string.saveGPSShiperAPI);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("CODE", response);
                    JSONObject json = new JSONObject(response);
                    String code = json.getString("code");


                    if (code.equals("0")) {
//                        Toast.makeText(getApplicationContext(), "Gửi thành công", Toast.LENGTH_SHORT).show();
//                            Intent i = new Intent(SentPassEmail.this, DangNhapActivity.class);
//                            startActivity(i);
                    } else {
//                        Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        SaveGpsRequest save = new SaveGpsRequest(accessToken, la1, lo1, link, response);
        RequestQueue qe = Volley.newRequestQueue(getApplicationContext());
        qe.add(save);
    }

//    @Override
//    public void onRefresh() {
//        ld.clear();
//        order();
//    }


}

