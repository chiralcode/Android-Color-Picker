Color Picker for Android
===================================

Easy to use, fully functional color picker with compact design based on HSV color model. Project consists two main classes which can be easily extended and suited to user requirements.

* ColorPicker.java - basic color picker which allows user to pick a single color

![Screenshot 1](http://www.chiralcode.com/colorpicker/base-screen1.png) ![Screenshot 2](http://www.chiralcode.com/colorpicker/base-screen2.png) ![Screenshot 3](http://www.chiralcode.com/colorpicker/base-screen3.png) 

* MultiColorPicker.java - color picker implementation which allows user to pick multiple colors with adjacent hue value

![Screenshot 1](http://www.chiralcode.com/colorpicker/multi-screen1.png) ![Screenshot 2](http://www.chiralcode.com/colorpicker/multi-screen2.png) ![Screenshot 3](http://www.chiralcode.com/colorpicker/multi-screen3.png) 

Usage
-----

Color Picker has been implemented as an extension of a View class. This gives a possibility to use it as any other Android component. Following are the most basic usage examples.

### In activity

Color Picker can be put in layout xml file just like any other component.

    <com.chiralcode.colorpicker.ColorPicker
        android:id="@+id/colorPicker"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
        
Example can be found in ColorPickerActivity.java file.

### In dialog

Using Color Picker in dialog requires extending AlertDialog class. Listener interface OnColorSelectedListener is required to return selected color value back to the caller.

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, initialColor, new OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                // do action
            }

        });
        colorPickerDialog.show();


See ColorPickerDialog.java for working sample.

### In preference

Color Picker can be used also on Preferece Screen. Usage is the same as for any other preferences. You can provide default, initial color value by setting `android:defaultValue` attribute. Value selected in dialog will be stored under the key provided with `android:key` attribute.

    <com.chiralcode.colorpicker.ColorPickerPreference
        android:defaultValue="0xffffffff"
        android:key="preferenceKeyName"
        android:title="@string/pref_name"
        android:summary="@string/pref_summary" />

Provided demo implementation extends DialogPreference. See ColorPickerPreference.java.

License
-------

Code is licensed under the Apache License, Version 2.0.

