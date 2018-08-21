package com.example.android.technewsapp;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FiltersActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filters_activity);
    }
    public static class ArticlePreferenceFragment extends PreferenceFragment
    implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.filters_main);

            Preference articleCategory = findPreference(getString(R.string.filters_categories_key));
            bindPreferenceSummaryToValue(articleCategory);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            // updates the displayed preference after it has been changed
            String stringValue = value.toString();
            preference.setSummary(stringValue);
            return true;
        }

        public void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }
}
