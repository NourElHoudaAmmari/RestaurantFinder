package com.example.restaurantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.restaurantapp.Model.Photos;
import com.example.restaurantapp.Model.PlaceDetail;
import com.example.restaurantapp.Remote.IGoogleAPIService;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class secondActivity extends AppCompatActivity {
ImageView photo;
IGoogleAPIService mService;
TextView opening_hour,place_addresse,place_name;
RatingBar ratingBar;
PlaceDetail myPlace;
Button btn,btn_show_d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mService = Common.getGoogleAPIService();
        opening_hour=findViewById(R.id.place_open_hour);
        place_addresse=findViewById(R.id.place_adress);
        place_name=findViewById(R.id.place_name);
        ratingBar=findViewById(R.id.ratingBar);
        btn=findViewById(R.id.btn_show_map);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myPlace.getResult().getUrl()));
                startActivity(mapIntent);
            }
        });
        btn_show_d=findViewById(R.id.btn_show_direction);
        place_name.setText("");
        place_addresse.setText("");
        opening_hour.setText("");
        btn_show_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
Intent intent = new Intent(secondActivity.this,ViewDirection.class);
startActivity(intent);
            }
        });
        photo=(ImageView) findViewById(R.id.photo);
        if(Common.currentResult.getPhotos()!= null && Common.currentResult.getPhotos().length > 0)
        {
           Picasso.get()
                   .load(getPhotoOfPlace(Common.currentResult.getPhotos()[0].getPhoto_reference(),1000))
                   .placeholder(R.drawable.ic_image)
                   .error(R.drawable.ic_error)
                   .into(photo);
        }
        if(Common.currentResult.getRating() != null && !TextUtils.isEmpty(Common.currentResult.getRating()))
        {
ratingBar.setRating(Float.parseFloat(Common.currentResult.getRating()));
        }else{
            ratingBar.setVisibility(View.GONE);
        }
        //opening hour
        if(Common.currentResult.getOpening_hours() != null )
        {
            opening_hour.setText(getString(R.string.openhour) + Common.currentResult.getOpening_hours().getOpen_now());
        }else{
           opening_hour.setVisibility(View.GONE);
        }
        //fetching adress and name
        mService.getDetailPlace(getPlaceDetailUrl(Common.currentResult.getPlace_id()))
                .enqueue(new Callback<PlaceDetail>() {
                    @Override
                    public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                        myPlace = response.body();
                        place_addresse.setText(myPlace.getResult().getFormatted_address());
                        place_name.setText(myPlace.getResult().getName());
                    }

                    @Override
                    public void onFailure(Call<PlaceDetail> call, Throwable t) {

                    }
                });
    }

    private String getPlaceDetailUrl(String place_id) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json");
        url.append("?placeid="+place_id);
        url.append("&key="+getResources().getString(R.string.map_key));
        return url.toString();
    }

    private String getPhotoOfPlace(String photo_references,int maxWidth) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
        url.append("?maxwidth="+maxWidth);
        url.append("&photo_reference="+photo_references);
        url.append("&key="+getResources().getString(R.string.map_key));
        return url.toString();
    }


}