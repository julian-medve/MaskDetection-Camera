// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.silverit.maskdetectioncamera.activity.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.DecimalFormat;

import com.silverit.maskdetectioncamera.model.FaceResult;

/**
 * Created by Nguyen on 5/20/2016.
 */

/**
 * This class is a simple View to display the faces.
 */
public class FaceOverlayView extends View {

    private Paint mPaint;
    private Paint mTextPaint;
    private int mDisplayOrientation;
    private int mOrientation;
    private int previewWidth;
    private int previewHeight;
    private FaceResult[] mFaces;
    private double fps;
    private boolean isFront = false;

    public FaceOverlayView(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {
        // We want a green box around the face:
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        int stroke = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(stroke);
        mPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, metrics);
        mTextPaint.setTextSize(size);
        mTextPaint.setColor(Color.GREEN);
        mTextPaint.setStyle(Paint.Style.FILL);
    }

    public void setRectColor(int color){
        mPaint.setColor(color);
    }

    public void setFPS(double fps) {
        this.fps = fps;
    }

    // ========================         Bluetooth           ========================== //

    private String bluetoothData = "";

    public void setBluetoothData(String data){
        this.bluetoothData = data;
    }


    public void setFaces(FaceResult[] faces) {
        mFaces = faces;
        invalidate();
    }

    public void setOrientation(int orientation) {
        mOrientation = orientation;
    }

    public void setDisplayOrientation(int displayOrientation) {
        mDisplayOrientation = displayOrientation;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mFaces != null && mFaces.length > 0) {

            float scaleX = (float) getWidth() / (float) previewWidth;
            float scaleY = (float) getHeight() / (float) previewHeight;

            switch (mDisplayOrientation) {
                case 90:
                case 270:
                    scaleX = (float) getWidth() / (float) previewHeight;
                    scaleY = (float) getHeight() / (float) previewWidth;
                    break;
            }

            canvas.save();
            canvas.rotate(-mOrientation);
            RectF rectF = new RectF();
            for (FaceResult face : mFaces) {
                PointF mid = new PointF();
                face.getMidPoint(mid);

                if (mid.x != 0.0f && mid.y != 0.0f) {
                    float eyesDis = face.eyesDistance();

                    rectF.set(new RectF(
                            (mid.x - eyesDis * 1.2f) * scaleX,
                            (mid.y - eyesDis * 0.65f) * scaleY,
                            (mid.x + eyesDis * 1.2f) * scaleX,
                            (mid.y + eyesDis * 1.75f) * scaleY));
                    if (isFront) {
                        float left = rectF.left;
                        float right = rectF.right;
                        rectF.left = getWidth() - right;
                        rectF.right = getWidth() - left;
                    }


                    canvas.drawRect(rectF, mPaint);


                    byte[] bytes = bluetoothData.getBytes(Charset.forName("UTF-8"));
                    float[] floatVlaues = new float[4];


                    String temperature = "";

                    for(int i = 0; i < 4; i++){

                        if(bytes.length >= (i + 1) * 5){

                            byte[] tempBytes = new byte[4];

                            tempBytes[0] = bytes[i * 5];
                            tempBytes[1] = bytes[i * 5 + 1];
                            tempBytes[2] = bytes[i * 5 + 2];
                            tempBytes[3] = bytes[i * 5 + 3];

                            floatVlaues[i] = ByteBuffer.wrap(tempBytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                        }
                        else
                            floatVlaues[i] = 0.0f;

                        if(i < 2)
                            temperature += floatVlaues[i] +"F ";
                        else
                            temperature += floatVlaues[i] +"C ";
                    }


                    canvas.drawText("Bluetooth data : " + temperature, rectF.left, rectF.bottom + mTextPaint.getTextSize(), mTextPaint);

                    /*
                    canvas.drawText("ID " + face.getId(), rectF.left, rectF.bottom + mTextPaint.getTextSize(), mTextPaint);
                    canvas.drawText("Confidence " + face.getConfidence(), rectF.left, rectF.bottom + mTextPaint.getTextSize() * 2, mTextPaint);
                    canvas.drawText("EyesDistance " + face.eyesDistance(), rectF.left, rectF.bottom + mTextPaint.getTextSize() * 3, mTextPaint);
                    */
                }
            }
            canvas.restore();
        }

        DecimalFormat df2 = new DecimalFormat(".##");

        // Draw Text FPS
//        canvas.drawText("Detected_Frame/s: " + df2.format(fps) + " @ " + previewWidth + "x" + previewHeight, mTextPaint.getTextSize(), mTextPaint.getTextSize(), mTextPaint);
    }

    private float convertToFloat(byte[] bytes, int index){

        byte[] bytes1 = new byte[4];
        bytes1[0] = bytes[0];

        return ByteBuffer.wrap(bytes1).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }


    public void setPreviewWidth(int previewWidth) {
        this.previewWidth = previewWidth;
    }

    public void setPreviewHeight(int previewHeight) {
        this.previewHeight = previewHeight;
    }

    public void setFront(boolean front) {
        isFront = front;
    }
}