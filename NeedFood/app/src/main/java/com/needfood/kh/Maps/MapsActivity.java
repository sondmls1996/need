package com.needfood.kh.Maps;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
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
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GPSTracker tracker;
    Geocoder geocoder;
    List<Address> addresses;
    double latitudee = 0, longitudee = 0;
    List<MapConstructor> list;
    String brandName, fullName, fone, address, id, latt, loo;
    String linkbrand, linkname, linkfone, linkadd, linkid;
    PlaceAutocompleteFragment autocompleteFragment;

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

        geocoder = new Geocoder(this, Locale.getDefault());
        autocompleteFragment = (PlaceAutocompleteFragment) MapsActivity.this.getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment1);
        list = new ArrayList<>();
        tracker = new GPSTracker(this);
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
        } else {
            latitudee = tracker.getLatitude();
            longitudee = tracker.getLongitude();
            getMoney(latitudee, longitudee);
        }
        Log.d("LATLONG", latitudee + "-" + longitudee);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();

        // mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
        // Add a marker in Sydney and move the camera

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
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                latitudee = latLng.latitude;
                longitudee = latLng.longitude;

                mMap.addMarker(new MarkerOptions().position(new LatLng(latitudee, longitudee)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .title(getResources().getString(R.string.loc)));

                getMoney(latitudee, longitudee);
            }
        });
        AutocompleteFilter filter = new AutocompleteFilter.Builder().setCountry("VN").setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS).build();
        autocompleteFragment.setFilter(filter);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMap.clear();
                latitudee = place.getLatLng().latitude;
                longitudee = place.getLatLng().longitude;
                Log.d("CHECKGPS", latitudee + "-" + longitudee);
                LatLng yourlocal = new LatLng(latitudee, longitudee);
                mMap.addMarker(new MarkerOptions().position(yourlocal).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Vị trí chỉ định")).showInfoWindow();
                CameraPosition update = new CameraPosition.Builder().target(yourlocal).zoom(14).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(update));
                getMoney(latitudee, longitudee);
            }

            @Override
            public void onError(Status status) {

            }
        });

    }

    private void getMoney(final double lat, final double lo) {
        list.clear();
        String link = getResources().getString(R.string.linkgetselleraround);
        Map<String, String> map = new HashMap<>();
        map.put("lat", lat + "");
        map.put("long", lo + "");
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
                        latt = jo3.getString("lat");
                        loo = jo3.getString("long");
                        list.add(new MapConstructor(id, brandName, fullName, fone, address, latt, loo));
                        Log.d("ABCC", id + "-" + brandName + "-" + fullName + "-" + fone + "-" + address + "-" + latt + "-" + loo);

                    }
                    showMap(lat, lo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PostCL get = new PostCL(link, map, response);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }

    public void showMap(Double latitudee, Double longitudee) {

        mMap.clear();
        if (latitudee != 0 && longitudee != 0) {
            LatLng sydney = new LatLng(latitudee, longitudee);

            mMap.addMarker(new MarkerOptions().position(sydney)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title(getResources().getString(R.string.yourin)));
            CameraPosition update2 = new CameraPosition.Builder().target(sydney).zoom(10).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(update2));
        } else {
            LatLng vm = new LatLng(21.028663, 105.836454);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(vm, 7);
            mMap.animateCamera(update, 1000, null);
        }


        for (int j = 0; j < list.size(); j++) {

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(list.get(j).getLat()), Double.parseDouble(list.get(j).getLo())))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .title(list.get(j).getBrandname().toString())
            );

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
                showMap(latitudee, longitudee);
                dialog.dismiss();
                dialog.cancel();
            }
        });
        dialog.show();


    }

}
