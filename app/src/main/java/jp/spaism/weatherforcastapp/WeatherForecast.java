package jp.spaism.weatherforcastapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spaism on 4/5/16.
 */
public class WeatherForecast {

    public final Location location;
    public final List<Forecast> forecastList = new ArrayList<Forecast>();

    public WeatherForecast(JSONObject jsonObject) throws JSONException{

        JSONObject locationObject = jsonObject.getJSONObject("location");
        location = new Location(locationObject);

        JSONArray forecastArray = jsonObject.getJSONArray("forecasts");

        int len = forecastArray.length();
        for(int i = 0; i < len; i++){
            JSONObject forecastJson = forecastArray.getJSONObject(i);

            Forecast forecast = new Forecast(forecastJson);

            forecastList.add(forecast);
        }
    }

    public class Location{
        public final String area;
        public final String prefecture;
        public final String city;

        public Location(JSONObject jsonObject) throws JSONException{
            area = jsonObject.getString("area");
            prefecture = jsonObject.getString("prefecture");
            city = jsonObject.getString("city");
        }
    }

    public class Forecast{
        public final String date;
        public final String dateLabel;
        public final String telop;
        public final Image image;
        public final Temperature temperature;

        public Forecast(JSONObject jsonObject) throws JSONException{
            date = jsonObject.getString("date");
            dateLabel = jsonObject.getString("dateLabel");
            telop = jsonObject.getString("telop");
            image = new Image(jsonObject.getJSONObject("image"));
            temperature = new Temperature(jsonObject.getJSONObject("temperature"));
        }

        public class Image{
            public final String title;
            public final String link;
            public final String url;
            public final int width;
            public final int height;

            public Image(JSONObject jsonObject) throws JSONException{
                title = jsonObject.getString("title");
                if(jsonObject.has("link")){
                    link = jsonObject.getString("link");
                }else{
                    link = null;
                }
                url = jsonObject.getString("url");
                width = jsonObject.getInt("width");
                height = jsonObject.getInt("height");
            }
        }

        public class Temperature{
            public final Temp min;
            public final Temp max;

            public Temperature(JSONObject jsonObject) throws JSONException{
                if(!jsonObject.isNull("min")){
                    min = new Temp(jsonObject.getJSONObject("min"));
                }else{
                    min = new Temp(null);
                }

                if(!jsonObject.isNull("max")){
                    max = new Temp(jsonObject.getJSONObject("max"));
                }else{
                    max = new Temp(null);
                }
            }

            public class Temp {
                public final String celsius;
                public final String fahrenheit;

                public Temp(JSONObject jsonObject) throws JSONException{
                    if(jsonObject == null){
                        celsius = null;
                        fahrenheit = null;
                        return;
                    }
                    celsius = jsonObject.getString("celsius");
                    fahrenheit = jsonObject.getString("fahrenheit");
                }
            }
        }
    }
}
