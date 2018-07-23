package com.jindan.views.sweetalertdialog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.jindan.views.R;

/**
 * Author zhaolei
 * Date 2018/4/12
 * Description desc
 */

public class SuccessTickView extends View {
    private float mDensity = -1.0F;
    private Paint mPaint;
    private final float CONST_RADIUS = this.dip2px(1.2F);
    private final float CONST_RECT_WEIGHT = this.dip2px(3.0F);
    private final float CONST_LEFT_RECT_W = this.dip2px(15.0F);
    private final float CONST_RIGHT_RECT_W = this.dip2px(25.0F);
    private final float MIN_LEFT_RECT_W = this.dip2px(3.3F);
    private final float MAX_RIGHT_RECT_W;
    private float mMaxLeftRectWidth;
    private float mLeftRectWidth;
    private float mRightRectWidth;
    private boolean mLeftRectGrowMode;

    public SuccessTickView(Context context) {
        super(context);
        this.MAX_RIGHT_RECT_W = this.CONST_RIGHT_RECT_W + this.dip2px(6.7F);
        this.init();
    }

    public SuccessTickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.MAX_RIGHT_RECT_W = this.CONST_RIGHT_RECT_W + this.dip2px(6.7F);
        this.init();
    }

    private void init() {
        this.mPaint = new Paint();
        this.mPaint.setColor(this.getResources().getColor(R.color.success_stroke_color));
        this.mLeftRectWidth = this.CONST_LEFT_RECT_W;
        this.mRightRectWidth = this.CONST_RIGHT_RECT_W;
        this.mLeftRectGrowMode = false;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        int totalW = this.getWidth();
        int totalH = this.getHeight();
        canvas.rotate(45.0F, (float) (totalW / 2), (float) (totalH / 2));
        totalW = (int) ((double) totalW / 1.2D);
        totalH = (int) ((double) totalH / 1.4D);
        this.mMaxLeftRectWidth = ((float) totalW + this.CONST_LEFT_RECT_W) / 2.0F + this.CONST_RECT_WEIGHT - 1.0F;
        RectF leftRect = new RectF();
        if (this.mLeftRectGrowMode) {
            leftRect.left = 0.0F;
            leftRect.right = leftRect.left + this.mLeftRectWidth;
            leftRect.top = ((float) totalH + this.CONST_RIGHT_RECT_W) / 2.0F;
            leftRect.bottom = leftRect.top + this.CONST_RECT_WEIGHT;
        } else {
            leftRect.right = ((float) totalW + this.CONST_LEFT_RECT_W) / 2.0F + this.CONST_RECT_WEIGHT - 1.0F;
            leftRect.left = leftRect.right - this.mLeftRectWidth;
            leftRect.top = ((float) totalH + this.CONST_RIGHT_RECT_W) / 2.0F;
            leftRect.bottom = leftRect.top + this.CONST_RECT_WEIGHT;
        }

        canvas.drawRoundRect(leftRect, this.CONST_RADIUS, this.CONST_RADIUS, this.mPaint);
        RectF rightRect = new RectF();
        rightRect.bottom = ((float) totalH + this.CONST_RIGHT_RECT_W) / 2.0F + this.CONST_RECT_WEIGHT - 1.0F;
        rightRect.left = ((float) totalW + this.CONST_LEFT_RECT_W) / 2.0F;
        rightRect.right = rightRect.left + this.CONST_RECT_WEIGHT;
        rightRect.top = rightRect.bottom - this.mRightRectWidth;
        canvas.drawRoundRect(rightRect, this.CONST_RADIUS, this.CONST_RADIUS, this.mPaint);
    }

    public float dip2px(float dpValue) {
        if (this.mDensity == -1.0F) {
            this.mDensity = this.getResources().getDisplayMetrics().density;
        }

        return dpValue * this.mDensity + 0.5F;
    }

    public void startTickAnim() {
        this.mLeftRectWidth = 0.0F;
        this.mRightRectWidth = 0.0F;
        this.invalidate();
        Animation tickAnim = new Animation() {
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                if (0.54D < (double) interpolatedTime && 0.7D >= (double) interpolatedTime) {
                    SuccessTickView.this.mLeftRectGrowMode = true;
                    SuccessTickView.this.mLeftRectWidth = SuccessTickView.this.mMaxLeftRectWidth * ((interpolatedTime - 0.54F) / 0.16F);
                    if (0.65D < (double) interpolatedTime) {
                        SuccessTickView.this.mRightRectWidth = SuccessTickView.this.MAX_RIGHT_RECT_W * ((interpolatedTime - 0.65F) / 0.19F);
                    }

                    SuccessTickView.this.invalidate();
                } else if (0.7D < (double) interpolatedTime && 0.84D >= (double) interpolatedTime) {
                    SuccessTickView.this.mLeftRectGrowMode = false;
                    SuccessTickView.this.mLeftRectWidth = SuccessTickView.this.mMaxLeftRectWidth * (1.0F - (interpolatedTime - 0.7F) / 0.14F);
                    SuccessTickView.this.mLeftRectWidth = SuccessTickView.this.mLeftRectWidth < SuccessTickView.this.MIN_LEFT_RECT_W ? SuccessTickView.this.MIN_LEFT_RECT_W : SuccessTickView.this.mLeftRectWidth;
                    SuccessTickView.this.mRightRectWidth = SuccessTickView.this.MAX_RIGHT_RECT_W * ((interpolatedTime - 0.65F) / 0.19F);
                    SuccessTickView.this.invalidate();
                } else if (0.84D < (double) interpolatedTime && 1.0F >= interpolatedTime) {
                    SuccessTickView.this.mLeftRectGrowMode = false;
                    SuccessTickView.this.mLeftRectWidth = SuccessTickView.this.MIN_LEFT_RECT_W + (SuccessTickView.this.CONST_LEFT_RECT_W - SuccessTickView.this.MIN_LEFT_RECT_W) * ((interpolatedTime - 0.84F) / 0.16F);
                    SuccessTickView.this.mRightRectWidth = SuccessTickView.this.CONST_RIGHT_RECT_W + (SuccessTickView.this.MAX_RIGHT_RECT_W - SuccessTickView.this.CONST_RIGHT_RECT_W) * (1.0F - (interpolatedTime - 0.84F) / 0.16F);
                    SuccessTickView.this.invalidate();
                }

            }
        };
        tickAnim.setDuration(750L);
        tickAnim.setStartOffset(100L);
        this.startAnimation(tickAnim);
    }
}
