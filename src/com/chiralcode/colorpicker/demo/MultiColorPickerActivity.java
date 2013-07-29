package com.chiralcode.colorpicker.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.chiralcode.colorpicker.MultiColorPicker;
import com.chiralcode.colorpicker.R;

public class MultiColorPickerActivity extends Activity {

    private MultiColorPicker multiColorPicker;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_multi_picker);

        multiColorPicker = (MultiColorPicker) findViewById(R.id.multiColorPicker);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                int color = multiColorPicker.getColor();
                String rgbString = "R: " + Color.red(color) + " B: " + Color.blue(color) + " G: " + Color.green(color);

                Toast.makeText(MultiColorPickerActivity.this, rgbString, Toast.LENGTH_SHORT).show();

            }
        });

    }

}
