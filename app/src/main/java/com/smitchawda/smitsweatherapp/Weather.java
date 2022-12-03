package com.smitchawda.smitsweatherapp;

import com.smitchawda.smitsweatherapp.secret.ApiKey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather {
    /**
     * JSON variables
     */
    private String city;
    private String country = "CA";
    private Double temperature;
    private Double feelsLikeTemperature;
    private Double minTemperature;
    private Double maxTemperature;
    private String imageId;
    private String weatherDescription;

    private final String apiKey = ApiKey.API_KEY.getApiKey();
    private String apiUrl = "";
    private URL url;
    private HttpURLConnection http;
    private int connectionTimeout = 5000;
    private int readTimeout = 5000;
    private BufferedReader in;
    private JSONObject jsonObject;

    /**
     * Returning variables
     */
    private Map<String, String> cleanData = new HashMap<String, String>();

    public Weather(String city) {
        this.city = city;
    }

    public void parseWeatherDataJSON(){
        this.apiUrl = "https://api.openweathermap.org/data/2.5/weather?q="+this.city+"&units=metric&appid=" +this.apiKey;
        try {
            this.url = new URL(this.apiUrl);
        } catch (MalformedURLException e) {
            System.out.println("URL connection failed");
            e.printStackTrace();
        }

        try {
            this.http = (HttpURLConnection) this.url.openConnection();
            this.http.setRequestMethod("GET");
            this.http.setRequestProperty("Content-Type", "application/json");
            this.http.setConnectTimeout(connectionTimeout);
            this.http.setReadTimeout(readTimeout);
            while(http.getResponseCode()!=200) {
                System.out.println("Waiting");
            }
            this.in = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String line;
            StringBuffer content = new StringBuffer();

            while ((line = this.in.readLine()) != null) {
                content.append(line);
            }

            try {
                this.jsonObject = new JSONObject(content.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            System.out.println("HttpURLConnection failed");
            e.printStackTrace();
        }
    }

    public Map<String, String> fetchLabelledData(){
        JSONObject rootObj = this.jsonObject;
        try {
            this.temperature =          Double.parseDouble(rootObj.getJSONObject("main").get("temp").toString());
            this.feelsLikeTemperature = Double.parseDouble(rootObj.getJSONObject("main").get("feels_like").toString());
            this.minTemperature =       Double.parseDouble(rootObj.getJSONObject("main").get("temp_min").toString());
            this.maxTemperature =       Double.parseDouble(rootObj.getJSONObject("main").get("temp_max").toString());

            JSONObject childObj = (JSONObject) rootObj.getJSONArray("weather").get(0);

            this.weatherDescription = childObj.getString("description");
            this.imageId = childObj.getString("icon");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.cleanData.put("temperature", this.temperature+"");
        this.cleanData.put("feels_like_temperature", this.feelsLikeTemperature+"");
        this.cleanData.put("min_temperature", this.minTemperature+"");
        this.cleanData.put("max_temperature", this.maxTemperature+"");
        this.cleanData.put("description", this.weatherDescription+"");
        this.cleanData.put("icon", this.imageId+"");
        return this.cleanData;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
