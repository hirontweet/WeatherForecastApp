package jp.spaism.weatherforcastapp;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.MalformedInputException;

/**
 * Created by spaism on 4/5/16.
 */
public class WeatherApi extends AsyncTask<Void, Void, WeatherForecast>{

    private static final String BASE_URL = "http://weather.livedoor.com/forecast/webservice/json/v1?city=";

    private StringBuilder sb = new StringBuilder();
    private String pointID;

    private Activity activity;

    public WeatherApi(Activity activity, String pointID){
        this.activity = activity;
        this.pointID = pointID;
    }

    @Override
    protected WeatherForecast doInBackground(Void... params) {

        HttpURLConnection connection = null;
        StringBuilder sb = new StringBuilder();

        try{
            URL url = new URL(BASE_URL + pointID);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(false);
            connection.connect();

            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String str = br.readLine();
            while(str != null){
                sb.append(str);
                str = br.readLine();
            }
            br.close();
            isr.close();
            is.close();
        }catch(MalformedURLException e){
            Toast.makeText(activity.getApplicationContext(), "MalformedURLException", Toast.LENGTH_SHORT).show();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.v("App-Response", sb.toString());



        WeatherForecast forecast = null;
        try{
            forecast = new WeatherForecast(new JSONObject(sb.toString()));
        }catch(JSONException e){
            e.printStackTrace();
            Log.e("Error:Instance", "JSONException occured here");
        }

        if(forecast == null){
            Log.v("App-Variable", "forecast is null");
        }

        return forecast;
    }

    @Override
    protected void onPostExecute(WeatherForecast data) {
        super.onPostExecute(data);

        TextView textView = (TextView) activity.findViewById(R.id.tv_location);

        if(data != null){
            textView.setText(data.location.area + " " + data.location.prefecture + " " + data.location.city);

            for(WeatherForecast.Forecast forecast : data.forecastList){
                View row = View.inflate(activity.getApplication(), R.layout.forecast_row, null);

                TextView date = (TextView) row.findViewById(R.id.tv_date);
                date.setText(forecast.dateLabel);

                TextView telop = (TextView) row.findViewById(R.id.tv_telop);
                telop.setText(forecast.telop);

                TextView temp = (TextView) row.findViewById(R.id.tv_tempreture);
                temp.setText(forecast.temperature.toString());

                ImageView imageView = (ImageView) row.findViewById(R.id.iv_weather);
                imageView.setTag(forecast.image.url);

                ImageLoaderTask task = new ImageLoaderTask(activity);
                task.execute(imageView);

                LinearLayout forecastLayout = (LinearLayout) activity.findViewById(R.id.ll_forecasts);
                forecastLayout.addView(row);
            }
        }else{
            Toast.makeText(activity.getApplicationContext(), "Weather data was null", Toast.LENGTH_SHORT).show();
        }
    }
}
