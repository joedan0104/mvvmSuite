package com.jindan.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author zhaolei
 * @date 2017/12/15
 * @description 旋转文字
 */

@SuppressLint("AppCompatCustomView")
public class RotateTextView extends TextView {
    private static final int[] textDegree = new int[]{R.attr.degree, R.attr.transX, R.attr.transY};
    private int degree;
    private int transX;
    private int transY;

    public RotateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RotateTextView);
        degree = ta.getInteger(R.styleable.RotateTextView_degree, 0);
        transX = ta.getDimensionPixelSize(R.styleable.RotateTextView_transX, 0);
        transY = ta.getDimensionPixelSize(R.styleable.RotateTextView_transY, 0);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.rotate(degree, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        canvas.translate(transX, transY);
        super.onDraw(canvas);
        canvas.restore();
    }
}
