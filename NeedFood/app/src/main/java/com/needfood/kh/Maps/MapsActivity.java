package com.needfood.kh.Maps;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.needfood.kh.Brand.BrandDetail;
import com.needfood.kh.Constructor.MapConstructor;
import com.needfood.kh.R;
import com.needfood.kh.SupportClass.GPSTracker;
import com.needfood.kh.SupportClass.PostCL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GPSTracker tracker;
    double latitude, longitude;
    List<MapConstructor> list;
    String brandName, fullName, fone, address, id, lat, lo;
    String linkbrand, linkname, linkfone, linkadd, linkid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ImageView imgb = (ImageView) findViewById(R.id.immgb);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView txt = (TextView) findViewById(R.id.titletxt);
        txt.setText(getResources().getString(R.string.aro));
        list = new ArrayList<>();
        tracker = new GPSTracker(this);
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
        } else {
            latitude = tracker.getLatitude();
            longitude = tracker.getLongitude();
        }
        Log.d("LATLONG", latitude + "-" + longitude);
        getMoney();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getResources().getString(R.string.loc)));
        CameraPosition update = new CameraPosition.Builder().target(sydney).zoom(14).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(update));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                // loc du lieu
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getBrandname().toString().equals(marker.getTitle().toString())) {
                        linkbrand = list.get(i).getBrandname();
                        linkadd = list.get(i).getAddress();
                        linkfone = list.get(i).getFone();
                        linkid = list.get(i).getId();
                        linkname = list.get(i).getFullname();
                        Log.d("LATLONG", linkbrand + "-" + linkadd);
                        LatLng yourlocal = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(yourlocal).zoom(16).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        showDialog();
                    }
                }
                return true;
            }
        });
//        for (int j = 0; j < list.size(); j++) {
//            mMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(Double.parseDouble(list.get(j).getLat()), Double.parseDouble(list.get(j).getLo())))
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//                    .title(list.get(j).getBrandname().toString()));
//        }
//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                for (int i = 0; i < list.size(); i++) {
//
//                }
//
//                return true;
//            }
//        });
    }

    private void getMoney() {
        String link = getResources().getString(R.string.linkgetselleraround);
        Map<String, String> map = new HashMap<>();
        map.put("lat", latitude + "");
        map.put("long", longitude + "");
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MNTTT", response);
                try {
                    JSONArray jo = new JSONArray(response);
                    for (int i = 0; i < jo.length(); i++) {
                        JSONObject jo2 = jo.getJSONObject(i);
                        JSONObject jo1 = jo2.getJSONObject("Seller");
                        brandName = jo1.getString("branchName");
                        fullName = jo1.getString("fullName");
                        fone = jo1.getString("fone");
                        address = jo1.getString("address");
                        id = jo1.getString("id");
                        JSONObject jo3 = jo1.getJSONObject("gps");
                        lat = jo3.getString("lat");
                        lo = jo3.getString("long");
                        list.add(new MapConstructor(id, brandName, fullName, fone, address, lat, lo));
                        Log.d("ABCC", id + "-" + brandName + "-" + fullName + "-" + fone + "-" + address + "-" + lat + "-" + lo);

                    }
                    showMap(latitude, longitude);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PostCL get = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }

    public void showMap(Double latitude, Double longitude) {
        LatLng yourlocal = new LatLng(latitude, longitude);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(yourlocal).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(getResources().getString(R.string.loc))).showInfoWindow();
        CameraPosition cameraPosition = new CameraPosition.Builder().target(yourlocal).zoom(14).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        for (int j = 0; j < list.size(); j++) {

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(list.get(j).getLat()), Double.parseDouble(list.get(j).getLo())))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .title(list.get(j).getBrandname().toString()));
        }

    }

    // show dialog
    public void showDialog() {
        final Dialog dialog = new Dialog(MapsActivity.this);
        dialog.setContentView(R.layout.promotiondialog1);
        dialog.setCancelable(false);
        TextView tv3 = (TextView) dialog.findViewById(R.id.title_dia);
        TextView tv2 = (TextView) dialog.findViewById(R.id.address_dia);
        tv3.setText(linkbrand);
        tv2.setText(linkadd);
        Button btn2 = (Button) dialog.findViewById(R.id.detail);
        Button btn3 = (Button) dialog.findViewById(R.id.quit);
        Log.d("LATLONG", linkbrand + "-" + linkadd);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), BrandDetail.class);
                it.putExtra("ids", linkid);
                startActivity(it);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap(latitude, longitude);
                dialog.dismiss();
                dialog.cancel();
            }
        });
        dialog.show();


    }

}
