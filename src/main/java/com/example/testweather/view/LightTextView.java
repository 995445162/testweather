package com.example.testweather.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by tujianhua on 2017/11/12.
 */

public class LightTextView extends android.support.v7.widget.AppCompatTextView{
    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private Paint mpaint;
    private int mViewWidth=0;
    private int mTranslate;

    private boolean mAnimating=true;
    public LightTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0) {
            mViewWidth=getMeasuredWidth();
            if (mViewWidth > 0) {
                mpaint=getPaint();
                mLinearGradient = new LinearGradient(-mViewWidth, 0, 0, 0, new int[]{0xffF505B7, 0xffffffff, 0xffF505B7}, new float[]{0, 0.5f, 1}, Shader.TileMode.MIRROR);
                mpaint.setShader(mLinearGradient);
                mGradientMatrix = new Matrix();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAnimating&&mGradientMatrix!=null) {
            mTranslate += mViewWidth / 10;
            if (mTranslate > mViewWidth * 2) {
                mTranslate -= mViewWidth;
            }
            mGradientMatrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(100);
        }
    }
}
