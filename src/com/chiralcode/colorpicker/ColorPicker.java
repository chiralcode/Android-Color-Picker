/*
 * Copyright 2013 Piotr Adamus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chiralcode.colorpicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ColorPicker extends View {

    private Paint colorWheelPaint;
    private Paint valueSliderPaint;
    private Paint colorViewPaint;

    private Paint colorPointerPaint;
    private RectF colorPointerCoords;

    private Paint valuePointerPaint;

    private RectF outerWheelRect;
    private RectF innerWheelRect;

    private Path path;

    private Bitmap colorWheelBitmap;

    private int valueSliderWidth;
    private int innerPadding;
    private int outerPadding;

    private int outerWheelRadius;
    private int innerWheelRadius;
    private int colorWheelRadius;

    private Matrix gradientRotationMatrix;

    private int selectedColor = Color.WHITE;
    private float[] selectedColorHSV = new float[] { 0f, 0f, 1f };

    public ColorPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorPicker(Context context) {
        super(context);
        init();
    }

    private void init() {

        colorPointerPaint = new Paint();
        colorPointerPaint.setStyle(Style.STROKE);
        colorPointerPaint.setStrokeWidth(2f);
        colorPointerPaint.setARGB(128, 0, 0, 0);

        valuePointerPaint = new Paint();
        valuePointerPaint.setStyle(Style.STROKE);
        valuePointerPaint.setStrokeWidth(4f);

        colorWheelPaint = new Paint();
        colorWheelPaint.setAntiAlias(true);
        colorWheelPaint.setDither(true);

        valueSliderPaint = new Paint();
        valueSliderPaint.setAntiAlias(true);
        valueSliderPaint.setDither(true);

        colorViewPaint = new Paint();
        colorViewPaint.setAntiAlias(true);

        path = new Path();

        outerWheelRect = new RectF();
        innerWheelRect = new RectF();

        colorPointerCoords = new RectF();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(widthSize, heightSize);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // drawing color wheel

        canvas.drawBitmap(colorWheelBitmap, centerX - colorWheelRadius, centerY - colorWheelRadius, null);

        // drawing color view

        path.reset();
        path.arcTo(outerWheelRect, 270, -180);
        path.arcTo(innerWheelRect, 90, 180);
        path.close();

        colorViewPaint.setColor(selectedColor);
        canvas.drawPath(path, colorViewPaint);

        // drawing color picker

        path.reset();
        path.arcTo(outerWheelRect, 270, 180);
        path.arcTo(innerWheelRect, 90, -180);
        path.close();

        float[] hsv = new float[] { selectedColorHSV[0], selectedColorHSV[1], 1f };

        SweepGradient sweepGradient = new SweepGradient(centerX, centerY, new int[] { Color.BLACK, Color.HSVToColor(hsv), Color.WHITE }, null);
        sweepGradient.setLocalMatrix(gradientRotationMatrix);
        valueSliderPaint.setShader(sweepGradient);

        canvas.drawPath(path, valueSliderPaint);

        // drawing color wheel pointer

        float hueAngle = (float) (selectedColorHSV[0] / 180f * Math.PI);
        int colorPointX = (int) (-Math.cos(hueAngle) * selectedColorHSV[1] * colorWheelRadius) + centerX;
        int colorPointY = (int) (-Math.sin(hueAngle) * selectedColorHSV[1] * colorWheelRadius) + centerY;

        float pointerRadius = 0.075f * colorWheelRadius;
        int pointerX = (int) (colorPointX - pointerRadius / 2);
        int pointerY = (int) (colorPointY - pointerRadius / 2);

        colorPointerCoords.set(pointerX, pointerY, pointerX + pointerRadius, pointerY + pointerRadius);
        canvas.drawOval(colorPointerCoords, colorPointerPaint);

        // drawing value pointer

        valuePointerPaint.setColor(Color.HSVToColor(new float[] { 0f, 0f, 1f - selectedColorHSV[2] }));

        float valueAngle = (float) ((selectedColorHSV[2] * 180f + 90f) / 180f * Math.PI);

        canvas.drawLine((float) (-Math.cos(valueAngle) * innerWheelRadius) + centerX, (float) (-Math.sin(valueAngle) * innerWheelRadius) + centerY,
                (float) (-Math.cos(valueAngle) * outerWheelRadius) + centerX, (float) (-Math.sin(valueAngle) * outerWheelRadius) + centerY, valuePointerPaint);

    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {

        int centerX = width / 2;
        int centerY = height / 2;

        valueSliderWidth = (int) (0.1f * width);
        innerPadding = (int) (0.05f * width);
        outerPadding = (int) (0.05f * width);

        outerWheelRadius = width / 2 - outerPadding;
        innerWheelRadius = outerWheelRadius - valueSliderWidth;
        colorWheelRadius = innerWheelRadius - innerPadding;

        outerWheelRect.set(centerX - outerWheelRadius, centerY - outerWheelRadius, centerX + outerWheelRadius, centerY + outerWheelRadius);
        innerWheelRect.set(centerX - innerWheelRadius, centerY - innerWheelRadius, centerX + innerWheelRadius, centerY + innerWheelRadius);

        colorWheelBitmap = createColorWheelBitmap(colorWheelRadius * 2, colorWheelRadius * 2);

        gradientRotationMatrix = new Matrix();
        gradientRotationMatrix.preRotate(270, width / 2, height / 2);

    }

    private Bitmap createColorWheelBitmap(int width, int height) {

        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);

        int colorCount = 12;
        int colorAngleStep = 360 / 12;
        int colors[] = new int[colorCount + 1];
        float hsv[] = new float[] { 0f, 1f, 1f };
        for (int i = 0; i < colors.length; i++) {
            hsv[0] = (i * colorAngleStep + 180) % 360;
            colors[i] = Color.HSVToColor(hsv);
        }
        colors[colorCount] = colors[0];

        SweepGradient sweepGradient = new SweepGradient(width / 2, height / 2, colors, null);
        RadialGradient radialGradient = new RadialGradient(width / 2, height / 2, colorWheelRadius, 0xFFFFFFFF, 0x00FFFFFF, TileMode.CLAMP);
        ComposeShader composeShader = new ComposeShader(sweepGradient, radialGradient, PorterDuff.Mode.SRC_OVER);

        colorWheelPaint.setShader(composeShader);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(width / 2, height / 2, colorWheelRadius, colorWheelPaint);

        return bitmap;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_MOVE:

            int x = (int) event.getX();
            int y = (int) event.getY();
            int cx = x - getWidth() / 2;
            int cy = y - getHeight() / 2;
            double d = Math.sqrt(cx * cx + cy * cy);

            if (d <= colorWheelRadius) {

                selectedColorHSV[0] = (float) (Math.atan2(cy, cx) / Math.PI * 180f) + 180;
                selectedColorHSV[1] = Math.max(0f, Math.min(1f, (float) (d / colorWheelRadius)));

                selectedColor = Color.HSVToColor(selectedColorHSV);

                invalidate();

            } else if (x >= getWidth() / 2 && d >= innerWheelRadius) {

                selectedColorHSV[2] = (float) Math.max(0, Math.min(1, (Math.atan2(cy, cx) / Math.PI * 180f + 90) / 180));

                selectedColor = Color.HSVToColor(selectedColorHSV);

                invalidate();
            }

            return true;
        }
        return super.onTouchEvent(event);
    }

    public void setInitialColor(int color) {
        this.selectedColor = color;
        Color.colorToHSV(color, selectedColorHSV);
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.d("onSaveInstanceState", "onSaveInstanceState");
        Bundle state = new Bundle();
        state.putInt("color", selectedColor);
        state.putParcelable("super", super.onSaveInstanceState());
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.d("onRestoreInstanceState", "onRestoreInstanceState");
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            setInitialColor(bundle.getInt("color"));
            super.onRestoreInstanceState(bundle.getParcelable("super"));
        } else {
            super.onRestoreInstanceState(state);
        }
    }

}
