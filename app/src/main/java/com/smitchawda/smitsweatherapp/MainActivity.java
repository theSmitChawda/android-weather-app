package com.smitchawda.smitsweatherapp;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity{
    /*---------------------Design Components---------------------*/

    //Layout
    ConstraintLayout main_constraint_layout;

    //Space holders
    Space space;
    Space space2;

    //TextViews
    TextView city;
    TextView temperature;
    TextView weather_description;
    TextView min_temp;
    TextView max_temp;
    TextView feels_like_temp;

    //Images
    ImageView weather_icon;

    /*---------------------Logic Variables---------------------*/
    Weather weather;
    static Map<String, String> data = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onConfigurationChanged(getResources().getConfiguration());
        initializeDesignComponents();
        initializeLogicComponents();
        applicationLogic();

        /* Created a new Thread to collect Weather Data
         * every 15s without disturbing the main UI thread. */
//        WeatherDataThread weatherDataThread = new WeatherDataThread(15);
//        weatherDataThread.start();
    }

    private void applicationLogic(){
//        city.setText(weather.getCity());
        temperature.setText(data.get("temperature"));
        min_temp.setText(data.get("min_temperature"));
        max_temp.setText(data.get("max_temperature"));
        feels_like_temp.setText(data.get("feels_like_temperature"));
        weather_description.setText(data.get("description"));
        Picasso.get().load("https://openweathermap.org/img/wn/"+data.get("icon")+"@2x.png").into(weather_icon);
    }

    @SuppressLint("ResourceType")
    private void initializeDesignComponents(){
        try {
            main_constraint_layout = findViewById(R.id.main_constraint_layout);
            space = findViewById(R.id.space);
            space2 = findViewById(R.id.space2);
            city = findViewById(R.id.city);
            temperature = findViewById(R.id.temperature);
            weather_description = findViewById(R.id.weather_description);
            min_temp = findViewById(R.id.min_temp);
            max_temp = findViewById(R.id.max_temp);
            feels_like_temp = findViewById(R.id.feels_like_temp);
            weather_icon = findViewById(R.id.weather_icon);
        }
        catch (Exception e){
            System.out.println("Component Initialization Failed");
            e.printStackTrace();
        }
    }

    private void initializeLogicComponents(){
        //TODO Get userlocation and set the city.
        //TODO weather = new Weather(city);
        weather = new Weather("Brampton");
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main);
            Toast.makeText(MainActivity.this, "Landscape", Toast.LENGTH_SHORT).show();
            initializeDesignComponents();
            applicationLogic();
        }
        else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main);
            Toast.makeText(MainActivity.this, "Potrait", Toast.LENGTH_SHORT).show();
            initializeDesignComponents();
            applicationLogic();
        }
    }

//    class WeatherDataThread extends Thread{
//        private static final String TAG = "WeatherDataThread";
//        int seconds=1000;
//
//        public WeatherDataThread(int seconds){
//            this.seconds = seconds*1000;
//        }
//
//        @Override
//        public void run() {
//            while(true) {
//                Log.d(TAG, "run: ");
//                try {
//                    System.out.println("\n\n-----Data-----\n\n");
//                    data = weather.fetchLabelledData();
//                    applicationLogic();
//                    Picasso.get().load("https://openweathermap.org/img/wn/"+data.get("icon")+"@2x.png").into(weather_icon);
//                    weather.fetchLabelledData().forEach((k,v)->{
//                        System.out.println("Key: "+k+" - Value: "+v);
//                    });
//                    Thread.sleep(this.seconds);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}