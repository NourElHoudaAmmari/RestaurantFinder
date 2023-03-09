package com.example.restaurantapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.restaurantapp.Model.MyPlaces;
import com.example.restaurantapp.Model.Results;
import com.example.restaurantapp.Remote.IGoogleAPIService;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback
      ,
GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener,LocationListener
{
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    GoogleMap map;
    Marker marker;
    LocationRequest mLocationRequest;
    SearchView searchView;
    IGoogleAPIService mService;
    private boolean isTrafficEnable;
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    double latitude = 0, longitude = 0;
    MyPlaces currentPlace;
    LatLng currentlatLng;
    Button btn_find;
    FloatingActionButton btnMapType,enableTraffic,currentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnMapType=findViewById(R.id.btnMapType);
        btn_find=findViewById(R.id.btn_find);
btn_find.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        nearByPlace("restaurant");
       /* String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?"+
                "location="+latitude+","+longitude+
                "&radius=5000"+
                "&keyword=restaurant"+
                "&sensor=true"+
                "&key="+getResources().getString(R.string.map_key);

        new PlaceTask().execute(url);*/
    }
});
        enableTraffic=findViewById(R.id.enableTraffic);
        enableTraffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isTrafficEnable){
                    if(map !=null){
                        map.setTrafficEnabled(false);
                        isTrafficEnable = false;
                    }else{
                        if(map != null){
                            map.setTrafficEnabled(true);
                            isTrafficEnable = true;
                        }
                    }
                }
            }
        });
        currentLocation=findViewById(R.id.currentLocation);
        currentLocation.setOnClickListener(currentLocation->getCurrentLocation());
        btnMapType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
                popupMenu.getMenuInflater().inflate(R.menu.map_type_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // handle menu item clicks here
                        switch (item.getItemId()) {
                            case R.id.btnNormal:
                                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                               break;
                            case R.id.btnSatellite:
                                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                               break;
                            case R.id.btnTerrain:
                                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show(); // show the popup menu
            }
        });
        searchView = findViewById(R.id.sv_location);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                if(location == null){
                    Toast.makeText(MainActivity.this,R.string.location,Toast.LENGTH_SHORT).show();
                }else{
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    try {
                        List<Address>addressList = geocoder.getFromLocationName(location,1);
                        if(addressList.size()>0){
                            LatLng latLng = new LatLng(addressList.get(0).getLatitude(),addressList.get(0).getLongitude());
                            if (marker !=null){
                                marker.remove();
                            }
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(location);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,5);
                            map.animateCamera(cameraUpdate);
                            marker = map.addMarker(markerOptions);
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
              /*  List<Address> addressList = null;
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(MainActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title(location));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }*/
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        String[] placeTypeList={"restaurant"};
        String[]placeNameList={"Restaurant"};


        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        supportMapFragment.getMapAsync(this);
        mService = Common.getGoogleAPIService();
    }

    private void nearByPlace(String placeType) {
        map.clear();
        String url = getUrl(latitude,longitude,placeType);
        mService.getNearByPlaces(url)
                .enqueue(new Callback<MyPlaces>() {
                    @Override
                    public void onResponse(Call<MyPlaces> call, Response<MyPlaces> response) {
                        if(response.isSuccessful()){
                            for(int i=0; i<response.body().getResults().length;i++){
                                MarkerOptions markerOptions = new MarkerOptions();
                                Results googlePlaces = response.body().getResults()[i];
                                double lat = Double.parseDouble(googlePlaces.getGeometry().getLocation().getLat());
                                double lng = Double.parseDouble(googlePlaces.getGeometry().getLocation().getLng());
                                String placeName = googlePlaces.getName();
                                String vicinity = googlePlaces.getVicinity();
                                LatLng latLng = new LatLng(lat,lng);
                                markerOptions.position(latLng);
                                markerOptions.title(placeName + ","+vicinity);
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                                markerOptions.snippet(String.valueOf(i)); // assign index for marker
                                map.addMarker(markerOptions);
                                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                map.animateCamera(CameraUpdateFactory.zoomTo(11));


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyPlaces> call, Throwable t) {

                    }
                });
    }

    private String getUrl(double latitude, double longitude, String placeType) {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+5000);
        googlePlaceUrl.append("&type="+placeType);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+getResources().getString(R.string.map_key));
        Log.d("getUrl",googlePlaceUrl.toString());
        return googlePlaceUrl.toString();
    }

    private void getCurrentLocation() {
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
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    latitude= location.getLatitude();
                    longitude = location.getLongitude();
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                           map = googleMap;
                            LatLng latLng = new LatLng(location.getLatitude()
                                    , location.getLongitude());
                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title(String.valueOf(R.string.mark_location));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                            googleMap.addMarker(options);

                            googleMap.getUiSettings().setZoomControlsEnabled(true);
                            googleMap.getUiSettings().setCompassEnabled(true);
                            googleMap.getUiSettings().setScrollGesturesEnabled(true);
                           // googleMap.setMyLocationEnabled(true);


                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Common.currentResult = currentPlace.getResults()[Integer.parseInt(marker.getSnippet())];


                return true;
            }
        });


    }
    private synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
mLocationRequest = new LocationRequest();
mLocationRequest.setInterval(1000);
mLocationRequest.setFastestInterval(1000);
mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
mLastLocation = location;
if(marker != null)
    marker.remove();
latitude = location.getLatitude();
longitude = location.getLongitude();
LatLng latLng = new LatLng(latitude,longitude);
MarkerOptions markerOptions = new MarkerOptions()
        .position(latLng)
        .title(String.valueOf(R.string.mark_location))
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
marker = map.addMarker(markerOptions);
map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
map.animateCamera(CameraUpdateFactory.zoomTo(11));
if(mGoogleApiClient != null)
    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
    }

   /* private class PlaceTask extends AsyncTask<String, Integer,String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException{
URL url = new URL(string);
HttpURLConnection connection =(HttpURLConnection) url.openConnection();
connection.connect();
InputStream stream = connection.getInputStream();
BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
StringBuilder builder = new StringBuilder();
String line="";
while((line = reader.readLine())!=null)
{
    builder.append(line);
}
String data = builder.toString();
reader.close();
return data;
    }

    private class ParserTask extends AsyncTask<String,Integer,List<HashMap<String,String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
JsonParser jsonParser = new JsonParser();
List<HashMap<String,String>>mapList = null;
JSONObject object = null;
            try {
                object = new JSONObject(strings[0]);
                mapList = jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            map.clear();
            for(int i =0; i<hashMaps.size();i++){
                HashMap<String,String>hashMapList = hashMaps.get(i);
                double lat = Double.parseDouble(hashMapList.get("lat"));
                double lng = Double.parseDouble(hashMapList.get("lng"));
                String name = hashMapList.get("name");
                String vicinity = hashMapList.get("vicinity");
                LatLng latLng = new LatLng(lat,lng);
                MarkerOptions options = new MarkerOptions();
                options.position(latLng);

                options.title(name+" : "+vicinity);
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                map.addMarker(options);
            }
        }
    }*/


    /*public void findRestaurant(View v) {
        Object dataTransfer[]=new Object[2];
        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
        map.clear();
        String restaurant = "restaurant";
      String url = getUrl(latitude,longitude, restaurant);
        dataTransfer[0]=map;
        dataTransfer[1]=url;
        getNearbyPlaces.execute(dataTransfer);

    }
    private String getUrl(double latitude, double longitude,String nearbyPlace){
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location"+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+10000);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+R.string.map_key);
return  googlePlaceUrl.toString();
    }
   /* public void findRestaurant(View v){
        Log.d("tag","clicked here");

StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        Log.d("tag", String.valueOf(currentLat));
        Log.d("tag", String.valueOf(currentLong));
stringBuilder.append("location="+ currentLat + "," + currentLong);
stringBuilder.append("&radius="+10000);
stringBuilder.append("&keyword="+R.string.keyword);
stringBuilder.append("&key="+getResources().getString(R.string.map_key));

String url = stringBuilder.toString();
Object dataTransfer[] = new Object[2];
dataTransfer[0] = map;
dataTransfer[1] = url ;
GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces(this);
getNearbyPlaces.execute(dataTransfer);
    }*/

 /*   @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.map = googleMap;
       mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

       mGoogleApiClient.connect();
    }

   @Override
    public void onConnected(@Nullable Bundle bundle) {
       mLocationRequest = new LocationRequest().create();
       mLocationRequest.setInterval(1000);
     mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

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
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

   @Override
    public void onLocationChanged(@NonNull Location location) {
if(location ==null){
    Toast.makeText(getApplicationContext(),"location not found",Toast.LENGTH_SHORT).show();
}else{
    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng,15);
    map.animateCamera(update);
    MarkerOptions options = new MarkerOptions();
    options.position(latLng);
    options.title(String.valueOf(R.string.mark_location));
}
    }*/

}