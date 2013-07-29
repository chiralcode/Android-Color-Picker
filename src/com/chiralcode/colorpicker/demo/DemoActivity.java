package com.chiralcode.colorpicker.demo;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.chiralcode.colorpicker.ColorPickerDialog;
import com.chiralcode.colorpicker.ColorPickerDialog.OnColorSelectedListener;
import com.chiralcode.colorpicker.R;

public class DemoActivity extends ListActivity {

    private static final int DEMO_VIEW = 0;
    private static final int DEMO_DIALOG = 1;
    private static final int DEMO_PREFERENCE = 2;
    private static final int DEMO_MULTI_VIEW = 3;

    private String[] demos = new String[] { "Color picker view", "Color picker dialog", "Color picker preference", "Multi color picker view" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_demo);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, demos);
        setListAdapter(arrayAdapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        switch (position) {

        case DEMO_VIEW:
            startActivity(new Intent(this, ColorPickerActivity.class));
            break;

        case DEMO_DIALOG:
            showColorPickerDialogDemo();
            break;

        case DEMO_PREFERENCE:
            startActivity(new Intent(this, PreferencesActivity.class));
            break;

        case DEMO_MULTI_VIEW:
            startActivity(new Intent(this, MultiColorPickerActivity.class));
            break;

        default:
        }

    }

    /**
     * Example of using Color Picker in Alert Dialog.
     */
    private void showColorPickerDialogDemo() {

        int initialColor = Color.WHITE;

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, initialColor, new OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                showToast(color);
            }

        });
        colorPickerDialog.show();

    }

    /**
     * Displays Toast with RGB values of given color.
     * 
     * @param color the color
     */
    private void showToast(int color) {
        String rgbString = "R: " + Color.red(color) + " B: " + Color.blue(color) + " G: " + Color.green(color);
        Toast.makeText(this, rgbString, Toast.LENGTH_SHORT).show();
    }

}
