    package com.smitchawda.smitsweatherapp;

    import androidx.annotation.RequiresApi;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.cardview.widget.CardView;
    import androidx.constraintlayout.widget.ConstraintLayout;
    import android.content.res.Configuration;
    import android.graphics.Bitmap;
    import android.graphics.BlurMaskFilter;
    import android.os.Build;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.DragEvent;
    import android.view.KeyEvent;
    import android.view.MotionEvent;
    import android.view.View;
    import android.widget.*;

    import com.google.android.material.textfield.TextInputEditText;
    import com.squareup.picasso.Picasso;

    import java.util.HashMap;
    import java.util.Map;

    public class MainActivity extends AppCompatActivity{
        /*---------------------Design Components---------------------*/
        //Layout
        ConstraintLayout main_constraint_layout;
        CardView min_temp_card;
        CardView max_temp_card;
        CardView feels_like_temp_card;
        CardView wind_speed_card;

        //TextViews
        TextView city;
        TextInputEditText cityInput;
        TextView temperature;
        TextView weather_description;
        TextView min_temp;
        TextView max_temp;
        TextView feels_like_temp;
        TextView wind_speed;

        //Images
        ImageView weather_icon;

        /*---------------------Logic Variables---------------------*/
        Weather weather = new Weather("Brampton");
        static Map<String, String> data = new HashMap<String, String>();
        int delayTimeInSeconds=1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            onConfigurationChanged(getResources().getConfiguration());
            WeatherDataThread thread = new WeatherDataThread(delayTimeInSeconds);
            thread.start();

            /*Press on the city to change it */
            city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("City Change Initiated");
                    cityInput.setVisibility(View.VISIBLE);
                }
            });
            cityInput.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if((keyEvent.getAction()==KeyEvent.ACTION_DOWN)
                            &&(i==KeyEvent.KEYCODE_ENTER)){
                        weather.setCity(cityInput.getText().toString());
                        cityInput.setVisibility(View.GONE);
                        return true;
                    }
                    return false;
                }
            });



        }

        private void initializeDesignComponents(){
            try {
                main_constraint_layout = findViewById(R.id.main_constraint_layout);

                min_temp_card = findViewById(R.id.min_temp_card);
                max_temp_card = findViewById(R.id.max_temp_card);
                feels_like_temp_card = findViewById(R.id.feels_like_temp_card);
                wind_speed_card = findViewById(R.id.wind_temp_card);

                cityInput = findViewById(R.id.cityInput);
                city = findViewById(R.id.city);
                temperature = findViewById(R.id.temperature);
                weather_description = findViewById(R.id.weather_description);
                min_temp = findViewById(R.id.min_temp);
                max_temp = findViewById(R.id.max_temp);
                feels_like_temp = findViewById(R.id.feels_like_temp);
                wind_speed = findViewById(R.id.wind);
                weather_icon = findViewById(R.id.weather_icon);
            }
            catch (Exception e){
                System.out.println("Component Binding Failed");
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
                    Log.d(TAG, "run: WeatherDataThread");
                    try {
                        PaintUIThread painterThread = new PaintUIThread(seconds);

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
                        wind_speed.setText(data.get("wind_speed"));
                        String str = data.get("icon");

                        weather_icon.setMinimumWidth(350);
                        weather_icon.setMinimumHeight(350);

                        Picasso.get().load("https://openweathermap.org/img/wn/"+str+"@2x.png").into(weather_icon);
                    });
            }
        }
    }