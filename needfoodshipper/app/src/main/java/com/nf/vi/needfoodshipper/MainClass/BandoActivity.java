package com.nf.vi.needfoodshipper.MainClass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nf.vi.needfoodshipper.Constructor.ListUserContructor;
import com.nf.vi.needfoodshipper.Constructor.LocalConstructor;
import com.nf.vi.needfoodshipper.R;
import com.nf.vi.needfoodshipper.SupportClass.GPSTracker;
import com.nf.vi.needfoodshipper.SupportClass.MyJsonReader;
import com.nf.vi.needfoodshipper.database.DBHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BandoActivity extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    PlaceAutocompleteFragment autocompleteFragment;
    GoogleApiClient mGoogleApiClient;
    View v;
    SupportMapFragment mapFragment;
    Context context;
    LocationManager locationManager;
    boolean GpsStatus;
    double latitude;
    double longitude;
    double lat;
    double lo;
    String lc;
    GPSTracker gpsTracker;
    DBHandle db;
    List<LocalConstructor> list;
    List<ListUserContructor> arr;
    String token, idshipper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.activity_bando, container, false);
        db = new DBHandle(getActivity());
        context = getContext();
        gps();
        Intent data = getActivity().getIntent();
        lc = data.getStringExtra("lc");
        list = db.getAllLocal();
        arr = db.getAllUser();
        if (list.size() != 0) {
            lat = list.get(list.size() - 1).getLat();
            lo = list.get(list.size() - 1).getLo();
        } else {
            lat = 21.067486;
            lo = 105.777115;
        }
        gpsTracker = new GPSTracker(getActivity());
        if (gpsTracker != null) {
            lat = gpsTracker.getLatitude();
            lo = gpsTracker.getLongitude();
        }
        buildAPI();
        final FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fabchiduong);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + lat + "," + lo + "&daddr=" + lc));
                startActivity(intent);

            }
        });
        return v;
    }

    private void buildAPI() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)

                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
      //  mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style_json));
        final LatLng yourlocal2 = new LatLng(lat, lo);
        CameraPosition update = new CameraPosition.Builder().target(yourlocal2).zoom(14).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(update));
        mMap.addMarker(new MarkerOptions().position(yourlocal2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("Vị trí của bạn")).showInfoWindow();
//        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lo)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("Vị trí của bạn"));
//        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.style_json));
        autocompleteFragment.setText(lc);

        AutocompleteFilter filter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS).build();
        autocompleteFragment.setFilter(filter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMap.clear();
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                Log.d("TAGG", latitude + "-" + longitude);
                LatLng yourlocal = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(yourlocal2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("Vị trí của tôi")).showInfoWindow();
                mMap.addMarker(new MarkerOptions().position(yourlocal).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Điểm đến")).showInfoWindow();
                CameraPosition update = new CameraPosition.Builder().target(yourlocal).zoom(14).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(update));
                drawway();
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void drawway() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadGMAP().execute("https://maps.googleapis.com/maps/api/directions/json?origin="+lat+","+lo+"&destination=" +
                        latitude+","+longitude+"&region=es&mode=driving&key=AIzaSyCkdyj_s2bXwu70mOyI_Hugzkt9vICzdi0");
            }
        });
    }


    private class ReadGMAP extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            return MyJsonReader.docNoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Log.d("MAPD",s);
                PolylineOptions options = new PolylineOptions().width(9).color(Color.RED).geodesic(true);
                JSONObject jo = new JSONObject(s);
                JSONArray route = jo.getJSONArray("routes");
                for (int i = 0; i < route.length(); i++) {
                    JSONObject idx = route.getJSONObject(i);
                    JSONArray leg = idx.getJSONArray("legs");
                    for (int k = 0; k < leg.length(); k++) {
                        JSONObject idleg = leg.getJSONObject(k);
                        JSONArray step = idleg.getJSONArray("steps");
                        for (int j = 0; j < step.length(); j++) {
                            JSONObject idstep = step.getJSONObject(j);
                            JSONObject startpoint = idstep.getJSONObject("start_location");
                            options.add(new LatLng(Double.parseDouble(startpoint.getString("lat")), Double.parseDouble(startpoint.getString("lng"))));
                            JSONObject endpoint = idstep.getJSONObject("end_location");
                            options.add(new LatLng(Double.parseDouble(endpoint.getString("lat")), Double.parseDouble(endpoint.getString("lng"))));
                        }
                    }
                }
                LatLng yourlocal = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(yourlocal).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Điểm đến")).showInfoWindow();
                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lo)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("Vị trí của bạn"));
                mMap.addPolyline(options);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }

    public void gps() {
        CheckGpsStatus();
        if (GpsStatus == true) {

//            textview.setText("Location Services Is Enabled");
        } else {

            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//            textview.setText("Location Services Is Disabled");
        }
    }

    public void CheckGpsStatus() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}

