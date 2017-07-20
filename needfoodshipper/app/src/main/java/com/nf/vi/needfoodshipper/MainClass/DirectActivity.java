package com.nf.vi.needfoodshipper.MainClass;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nf.vi.needfoodshipper.R;

public class DirectActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap mMap;
    private String a;
    GoogleMap gg;
    double la = 0, lo = 0, la2, lo2;
    GoogleApiClient client;
    LocationManager locationManager;
    Location l2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chiduong);
        buildAPI();
        if (client != null) {
            client.connect();
        } else {
            Toast.makeText(getApplicationContext(), "API dissconnect", Toast.LENGTH_SHORT).show();
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider callingi8l

            return;
        }
        //chinh thoi gian cap nhat toa do
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50000, 0, this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(la, lo);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            la = location.getLatitude();
            lo = location.getLongitude();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(la,lo)));
//            Toast.makeText(getApplicationContext(), "Location change" + "\n" + "la: " + la + "\n" + "lo: " + lo, Toast.LENGTH_SHORT).show();
//            String code = edtcode.getText().toString();
//            guitoado();


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
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(la,lo)));
//           Toast.makeText(getApplicationContext(), "Location Connected" + "\n" + "la: " + la + "\n" + "lo: " + lo, Toast.LENGTH_SHORT).show();
//            Log.d("TOADO", String.valueOf(la)+"\n"+String.valueOf(lo));
//            guitoado();

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
}
