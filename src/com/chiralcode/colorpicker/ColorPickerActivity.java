package com.chiralcode.colorpicker;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.colorpicker.R;

public class ColorPickerActivity extends Activity {

    private ColorPicker colorPicker;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colorPicker = (ColorPicker) findViewById(R.id.colorPicker);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                int color = colorPicker.getSelectedColor();

                button.setBackgroundColor(color);
                button.setTextColor(Color.rgb(255 - Color.red(color), 255 - Color.green(color), 255 - Color.blue(color)));

                Toast.makeText(ColorPickerActivity.this, "R: " + Color.red(color) + " B: " + Color.blue(color) + " G: " + Color.green(color),
                        Toast.LENGTH_SHORT).show();

            }
        });

    }

}
