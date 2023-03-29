package com.example.restaurantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import dmax.dialog.SpotsDialog;

import com.example.restaurantapp.Helper.DirectionJSONParser;
import com.example.restaurantapp.Remote.IGoogleAPIService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewDirection extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap map;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    Location mLastLocation;
    LocationResult locationResult;
    Marker mCurrentMarker;
    Polyline polyline;
    LocationRequest mLocationRequest;
    IGoogleAPIService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_direction);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapdirection);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }
        mService = Common.getGoogleAPIServiceScalars();

        buildLocationRequest();
        buildLocationCallBack();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mLastLocation = locationResult.getLastLocation();
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                        .title(String.valueOf(R.string.mark_location))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mCurrentMarker = map.addMarker(markerOptions);

                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())));
                map.animateCamera(CameraUpdateFactory.zoomTo(12.0f));

                LatLng destinationLatLng = new LatLng(Double.parseDouble(Common.currentResult.getGeometry().getLocation().getLat()),
                        Double.parseDouble(Common.currentResult.getGeometry().getLocation().getLng()));
                map.addMarker(new MarkerOptions()
                        .position(destinationLatLng)
                        .title(Common.currentResult.getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                drawPath(mLastLocation, Common.currentResult.getGeometry().getLocation());
                PolylineOptions polylineOptions = new PolylineOptions()
                        .color(Color.RED)
                        .width(5);

                polyline = map.addPolyline(polylineOptions);

            }
        };
    }

    private void buildLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setSmallestDisplacement(10f);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mLastLocation = location;
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()))
                        .title(String.valueOf(R.string.mark_location))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mCurrentMarker = map.addMarker(markerOptions);

                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude())));
                map.animateCamera(CameraUpdateFactory.zoomTo(12.0f));

                LatLng destinationLatLng = new LatLng(Double.parseDouble(Common.currentResult.getGeometry().getLocation().getLat()),
                        Double.parseDouble(Common.currentResult.getGeometry().getLocation().getLng()));
                map.addMarker(new MarkerOptions()
                        .position(destinationLatLng)
                        .title(Common.currentResult.getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                drawPath(mLastLocation,Common.currentResult.getGeometry().getLocation());
            }
        });
    }

    private void drawPath(Location mLastLocation, com.example.restaurantapp.Model.Location location) {
        if(polyline !=null)
            polyline.remove();

        String origin = new StringBuilder(String.valueOf(mLastLocation.getLatitude())).append(",").append(String.valueOf(mLastLocation.getLongitude()))
                .toString();
        String destination = new StringBuilder(location.getLat()).append(",").append(location.getLng())
                .toString();
        mService.getDirections(origin,destination)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        new ParserTask().execute(response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
    }

    @Override
    protected void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    private class ParserTask extends AsyncTask<String,Integer,List<List<HashMap<String, String>>>> {
        AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(ViewDirection.this).build();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            waitingDialog.show();
            waitingDialog.setMessage(getString(R.string.message));
        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            Log.d("method invoked","method call with success");
            JSONObject jsonObject;
            List<List<HashMap<String, String>>> routes = null;
            try{
                jsonObject = new JSONObject(strings[0]);
                DirectionJSONParser parser = new DirectionJSONParser();
                routes = parser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            Log.d("method invoked","method call with success");
            super.onPostExecute(lists);
            ArrayList points = null;
            PolylineOptions polylineOptions = null;
            for(int i=0; i<lists.size();i++){
                points = new ArrayList<>();
                polylineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = lists.get(i);
                for(int j=0; j<path.size();j++){
                    HashMap<String,String> point = path.get(j);
                    double lat = Double.parseDouble(Objects.requireNonNull(point.get("lat")));
                    double lng = Double.parseDouble(Objects.requireNonNull(point.get("lng")));
                    LatLng position = new LatLng(lat,lng);
                    points.add(position);

                }
                polylineOptions.addAll(points);
                polylineOptions.width(12);
                polylineOptions.color(Color.RED);
                polylineOptions.geodesic(true);
            }
            if(polylineOptions !=null) {
                polyline = map.addPolyline(polylineOptions);
                waitingDialog.dismiss();
            }
        }
    }
}