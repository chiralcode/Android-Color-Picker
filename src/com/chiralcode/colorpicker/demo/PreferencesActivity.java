package com.chiralcode.colorpicker.demo;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.chiralcode.colorpicker.R;

public class PreferencesActivity extends PreferenceActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.act_pref);
    }

}
