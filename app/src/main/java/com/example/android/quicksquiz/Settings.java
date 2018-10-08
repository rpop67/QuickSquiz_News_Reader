package com.example.android.quicksquiz;

import android.content.SharedPreferences;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Set;

public class Settings extends AppCompatActivity {

    private static final String OR_SEPARATOR = ", ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class SettingsFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference postCount = findPreference(getString(R.string.keyPosts));
            bindPreferenceSummaryToValue(postCount);

            Preference yourInterested = findPreference(getString(R.string.keyTopic));
            bindPreferenceSummaryToValue(yourInterested);
        }


        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference instanceof MultiSelectListPreference) {
                @SuppressWarnings("unchecked")
                Set<String> values = (Set<String>) newValue;
                MultiSelectListPreference pref = (MultiSelectListPreference) preference;
                CharSequence[] entries = pref.getEntries();

                StringBuilder stringValue = new StringBuilder();
                boolean isFirst = true;
                for (String section : values) {
                    int index = pref.findIndexOfValue(section);
                    if (index >= 0) {
                        if (!isFirst) {
                            stringValue.append(OR_SEPARATOR);
                        } else {
                            isFirst = false;
                        }
                        stringValue.append(entries[index]);
                    }
                }
                preference.setSummary(stringValue.toString());
            } else {
                String valueString = (String) newValue;
                preference.setSummary(valueString);
            }

            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            if (preference instanceof MultiSelectListPreference) {
                Set<String> sectionsSet = preferences.getStringSet(preference.getKey()
                        , null);
                onPreferenceChange(preference, sectionsSet);
            } else {
                String preferenceString = preferences.getString(preference.getKey(), "");
                onPreferenceChange(preference, preferenceString);
            }
        }
    }
}


