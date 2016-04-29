package jp.spaism.weatherforcastapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by spaism on 4/29/16.
 */
public class FragmentWeather extends Fragment {

    private static final String KEY_CITY_CODE = "key_city_code";

    public static FragmentWeather newInstance(String cityCode) {
        FragmentWeather fragment = new FragmentWeather();
        Bundle args = new Bundle();
        args.putString(KEY_CITY_CODE, cityCode);
        fragment.setArguments(args);

        return fragment;
    }

    private TextView location;
    private LinearLayout forecastLayout;
    private ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, null);

        location = (TextView) view.findViewById(R.id.tv_location);
        forecastLayout = (LinearLayout) view.findViewById(R.id.ll_forecasts);
        progress = (ProgressBar) view.findViewById(R.id.progress);

        GetWeatherForecastTask forecastTask = new GetWeatherForecastTask(getActivity(), getArguments().getString(KEY_CITY_CODE));
        forecastTask.execute();

        return view;
    }

    private class GetWeatherForecastTask extends WeatherApi {

        private String pointID;
        private Activity activity;

        public GetWeatherForecastTask(Activity activity, String pointID){
            super(activity, pointID);
            this.activity = activity;
            this.pointID = pointID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(WeatherForecast data) {
            super.onPostExecute(data);

            if (data != null) {
                progress.setVisibility(View.GONE);

                location.setText(data.location.area + " " + data.location.prefecture + " " + data.location.city);

                // 予報を一覧表示
                for (WeatherForecast.Forecast forecast : data.forecastList) {
                    View row = View.inflate(getActivity(), R.layout.forecast_row, null);

                    TextView date = (TextView) row.findViewById(R.id.tv_date);
                    date.setText(forecast.dateLabel);

                    TextView telop = (TextView) row.findViewById(R.id.tv_telop);
                    telop.setText(forecast.telop);

                    TextView temp = (TextView) row.findViewById(R.id.tv_tempreture);
                    temp.setText(forecast.temperature.toString());

                    ImageView imageView = (ImageView) row.findViewById(R.id.iv_weather);
                    imageView.setTag(forecast.image.url); // 読み込むURLを設定

                    // 読み込み処理の実行
                    ImageLoaderTask task = new ImageLoaderTask(getActivity());
                    task.execute(imageView);

                    forecastLayout.addView(row);
                }

            } else {
                Toast.makeText(getActivity(), "Error Not found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
