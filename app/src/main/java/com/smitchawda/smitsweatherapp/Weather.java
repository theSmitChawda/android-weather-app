package com.smitchawda.smitsweatherapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
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
    private BigDecimal temperature;
    private BigDecimal feelsLikeTemperature;
    private BigDecimal minTemperature;
    private BigDecimal maxTemperature;
    private String imageId;
    private String weatherDescription;
    /**
     * Returning variables
     */
    private String[] data = new String[6];
    private Map<String, String> cleanData = new HashMap<String, String>();

    private final String apiKey = "d9b82647df4c26aa3ee3fc48a4d2a25a";
    private String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=Brampton&units=metric&appid=";
    private int status;
    private URL url;
    private HttpURLConnection http;
    private int connectionTimeout = 5000;
    private int readTimeout = 5000;
    private BufferedReader in;

    private JSONObject jsonObject;

    public Weather(String city) {
        super();
        this.city = city;
        this.apiUrl =
                "https://api.openweathermap.org/data/2.5/weather?q="+this.city+
                        "&units=metric&appid=" +this.apiKey;
        try {
            this.url = new URL(this.apiUrl);
        } catch (MalformedURLException e) {
            System.out.println("URL connection failed");
            e.printStackTrace();
        }
        parseWeatherDataJSON();
    }

    private void parseWeatherDataJSON(){
        try {
            this.http = (HttpURLConnection) this.url.openConnection();
            this.http.setRequestMethod("GET");
            this.http.setRequestProperty("Content-Type", "application/json");
            this.http.setConnectTimeout(connectionTimeout);
            this.http.setReadTimeout(readTimeout);

            this.status = http.getResponseCode();

            this.in = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String line;
            StringBuffer content = new StringBuffer();

            while((line = this.in.readLine()) != null) {
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

    public String[] fetchData() {
        JSONObject rootObj = this.jsonObject;
        try {
            this.temperature = (BigDecimal) rootObj.getJSONObject("main").get("temp");
            this.feelsLikeTemperature = (BigDecimal) rootObj.getJSONObject("main").get("feels_like");
            this.minTemperature = (BigDecimal) rootObj.getJSONObject("main").get("temp_min");
            this.maxTemperature = (BigDecimal) rootObj.getJSONObject("main").get("temp_max");

            JSONObject childObj = (JSONObject) rootObj.getJSONArray("weather").get(0);

            this.weatherDescription = childObj.getString("description");
            this.imageId = childObj.getString("icon");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.data[0] = this.temperature+"";
        this.data[1] = this.feelsLikeTemperature+"";
        this.data[2] = this.minTemperature+"";
        this.data[3] = this.maxTemperature+"";
        this.data[4] = this.weatherDescription;
        this.data[5] = this.imageId;

        return this.data;
    }


    public Map<String, String> fetchLabelledData(){
        JSONObject rootObj = this.jsonObject;
        try {
            this.temperature = (BigDecimal) rootObj.getJSONObject("main").get("temp");
            this.feelsLikeTemperature = (BigDecimal) rootObj.getJSONObject("main").get("feels_like");
            this.minTemperature = (BigDecimal) rootObj.getJSONObject("main").get("temp_min");
            this.maxTemperature = (BigDecimal) rootObj.getJSONObject("main").get("temp_max");

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
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
