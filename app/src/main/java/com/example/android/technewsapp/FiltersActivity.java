package com.example.android.technewsapp;

import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FiltersActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filters_activity);
    }
    public static class EarthquakePreferenceFragment extends PreferenceFragment {

    }
}
