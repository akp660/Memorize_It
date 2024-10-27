package com.example.memorizeit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchInterceptorView extends View {

    public TouchInterceptorView(Context context) {
        super(context);
    }

    public TouchInterceptorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchInterceptorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Intercept all touch events
        return true;
    }
}
