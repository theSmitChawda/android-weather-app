    package com.smitchawda.smitsweatherapp;

    import androidx.annotation.RequiresApi;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.constraintlayout.widget.ConstraintLayout;
    import android.content.res.Configuration;
    import android.graphics.Bitmap;
    import android.graphics.BlurMaskFilter;
    import android.os.Build;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.DragEvent;
    import android.view.MotionEvent;
    import android.view.View;
    import android.widget.*;

    import com.squareup.picasso.Picasso;

    import java.util.HashMap;
    import java.util.Map;

    public class MainActivity extends AppCompatActivity{
        /*---------------------Design Components---------------------*/
        //Layout
        ConstraintLayout main_constraint_layout;

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
                String[] city = {"Delhi","Paris","Dubai"};
                while(true) {
                    Log.d(TAG, "run: WeatherDataThread");
                    try {
                        PaintUIThread painterThread = new PaintUIThread(seconds);
                        //Testing City changes
                        int randomNumber = (int) (Math.random()*(3-0));
                        weather.setCity(city[randomNumber]);
                        System.out.println("City: "+weather.getCity());

                        System.out.println("\n-----Data-----\n");
                        weather.parseWeatherDataJSON();
                        data = weather.fetchLabelledData();
                        weather.fetchLabelledData().forEach((k,v)->{
                            System.out.println(k+": "+v);
                        });
                        painterThread.start();
                        Thread.sleep(this.seconds);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        class PaintUIThread extends Thread{
            private static final String TAG = "PaintUIThread";
            int seconds=1000;

            public PaintUIThread(int seconds){
                //converting to ms for Thread input
                this.seconds = seconds*1000;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                Log.d(TAG, "run: PaintingUI");
                    runOnUiThread(()->{
                        city.setText(weather.getCity());
                        temperature.setText(data.get("temperature"));
                        feels_like_temp.setText(data.get("feels_like_temperature"));
                        min_temp.setText(data.get("min_temperature"));
                        max_temp.setText(data.get("max_temperature"));
                        weather_description.setText(data.get("description"));
                        String str = data.get("icon");
                        weather_icon.setMinimumWidth(350);
                        weather_icon.setMinimumHeight(350);
                        Picasso.get().load("https://openweathermap.org/img/wn/"+str+"@2x.png").into(weather_icon);
                    });
            }
        }
    }