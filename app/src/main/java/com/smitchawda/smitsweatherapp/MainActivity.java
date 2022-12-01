package com.smitchawda.smitsweatherapp;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import android.os.Build;
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
    Weather weather = new Weather("Brampton");
    static Map<String, String> data = new HashMap<String, String>();
    int delayTimeInSeconds=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onConfigurationChanged(getResources().getConfiguration());
        WeatherDataThread thread = new WeatherDataThread(delayTimeInSeconds);
        thread.start();
    }

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

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main);
            Toast.makeText(MainActivity.this, "Landscape", Toast.LENGTH_SHORT).show();
            initializeDesignComponents();
        }
        else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main);
            Toast.makeText(MainActivity.this, "Potrait", Toast.LENGTH_SHORT).show();
            initializeDesignComponents();
        }
    }

    class WeatherDataThread extends Thread{
        private static final String TAG = "WeatherDataThread";
        int seconds=1000;

        public WeatherDataThread(int seconds){
            //converting to ms for Thread input
            this.seconds = seconds*1000;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            while(true) {
                Log.d(TAG, "run: ");
                try {
                    System.out.println("\n\n-----Data-----\n\n");
                    weather.parseWeatherDataJSON();
                    data = weather.fetchLabelledData();
                    weather.fetchLabelledData().forEach((k,v)->{
                        System.out.println(k+": "+v);
                    });
                    Thread.sleep(this.seconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}