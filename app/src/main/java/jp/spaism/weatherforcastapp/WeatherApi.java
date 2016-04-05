package jp.spaism.weatherforcastapp;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

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
public class WeatherApi extends AsyncTask<Void, Void, String>{

    private static final String USER_AGENT = "WeatherForecasts Sample";
    private static final String BASE_URL = "http://weather.livedoor.com/forecast/webservice/json/v1?city=";

    private StringBuilder sb = new StringBuilder();
    private String pointID;

    private Activity activity;

    public WeatherApi(Activity activity, String pointID){
        this.activity = activity;
        this.pointID = pointID;
    }

    @Override
    protected String doInBackground(Void... params) {

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

        return sb.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        TextView tv = (TextView)activity.findViewById(R.id.tv_main);
        tv.setText(s);
    }
}
