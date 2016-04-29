package jp.spaism.weatherforcastapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    private static final String[] POINT_LIST = {
            "270000", "130010", "040010"
    };

    private List<String> pointList;

    private Adapter adapter;
    private ViewPager viewPager;

    private class Adapter extends FragmentStatePagerAdapter{

        public Adapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentWeather.newInstance(pointList.get(position));
        }

        @Override
        public int getCount() {
            return pointList.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(pointList == null){
            pointList = Arrays.asList(POINT_LIST);
        }

        viewPager = (ViewPager)findViewById(R.id.vp_main);
        adapter = new Adapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

//        WeatherApi weatherApi = new WeatherApi(this, "400040");
//        weatherApi.execute();


    }
}
