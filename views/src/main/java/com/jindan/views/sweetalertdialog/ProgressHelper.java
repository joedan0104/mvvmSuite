package com.jindan.views.sweetalertdialog;

import android.content.Context;

import com.jindan.views.R;
import com.pnikosis.materialishprogress.ProgressWheel;

/**
 * Author zhaolei
 * Date 2018/4/12
 * Description desc
 */

public class ProgressHelper {
    private ProgressWheel mProgressWheel;
    private boolean mToSpin = true;
    private float mSpinSpeed = 0.75F;
    private int mBarWidth;
    private int mBarColor;
    private int mRimWidth;
    private int mRimColor;
    private boolean mIsInstantProgress;
    private float mProgressVal;
    private int mCircleRadius;

    public ProgressHelper(Context ctx) {
        this.mBarWidth = ctx.getResources().getDimensionPixelSize(R.dimen.common_circle_width) + 1;
        this.mBarColor = ctx.getResources().getColor(R.color.success_stroke_color);
        this.mRimWidth = 0;
        this.mRimColor = 0;
        this.mIsInstantProgress = false;
        this.mProgressVal = -1.0F;
        this.mCircleRadius = ctx.getResources().getDimensionPixelOffset(R.dimen.progress_circle_radius);
    }

    public ProgressWheel getProgressWheel() {
        return this.mProgressWheel;
    }

    public void setProgressWheel(ProgressWheel progressWheel) {
        this.mProgressWheel = progressWheel;
        this.updatePropsIfNeed();
    }

    private void updatePropsIfNeed() {
        if(this.mProgressWheel != null) {
            if(!this.mToSpin && this.mProgressWheel.isSpinning()) {
                this.mProgressWheel.stopSpinning();
            } else if(this.mToSpin && !this.mProgressWheel.isSpinning()) {
                this.mProgressWheel.spin();
            }

            if(this.mSpinSpeed != this.mProgressWheel.getSpinSpeed()) {
                this.mProgressWheel.setSpinSpeed(this.mSpinSpeed);
            }

            if(this.mBarWidth != this.mProgressWheel.getBarWidth()) {
                this.mProgressWheel.setBarWidth(this.mBarWidth);
            }

            if(this.mBarColor != this.mProgressWheel.getBarColor()) {
                this.mProgressWheel.setBarColor(this.mBarColor);
            }

            if(this.mRimWidth != this.mProgressWheel.getRimWidth()) {
                this.mProgressWheel.setRimWidth(this.mRimWidth);
            }

            if(this.mRimColor != this.mProgressWheel.getRimColor()) {
                this.mProgressWheel.setRimColor(this.mRimColor);
            }

            if(this.mProgressVal != this.mProgressWheel.getProgress()) {
                if(this.mIsInstantProgress) {
                    this.mProgressWheel.setInstantProgress(this.mProgressVal);
                } else {
                    this.mProgressWheel.setProgress(this.mProgressVal);
                }
            }

            if(this.mCircleRadius != this.mProgressWheel.getCircleRadius()) {
                this.mProgressWheel.setCircleRadius(this.mCircleRadius);
            }
        }

    }

    public void resetCount() {
        if(this.mProgressWheel != null) {
            this.mProgressWheel.resetCount();
        }

    }

    public boolean isSpinning() {
        return this.mToSpin;
    }

    public void spin() {
        this.mToSpin = true;
        this.updatePropsIfNeed();
    }

    public void stopSpinning() {
        this.mToSpin = false;
        this.updatePropsIfNeed();
    }

    public float getProgress() {
        return this.mProgressVal;
    }

    public void setProgress(float progress) {
        this.mIsInstantProgress = false;
        this.mProgressVal = progress;
        this.updatePropsIfNeed();
    }

    public void setInstantProgress(float progress) {
        this.mProgressVal = progress;
        this.mIsInstantProgress = true;
        this.updatePropsIfNeed();
    }

    public int getCircleRadius() {
        return this.mCircleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.mCircleRadius = circleRadius;
        this.updatePropsIfNeed();
    }

    public int getBarWidth() {
        return this.mBarWidth;
    }

    public void setBarWidth(int barWidth) {
        this.mBarWidth = barWidth;
        this.updatePropsIfNeed();
    }

    public int getBarColor() {
        return this.mBarColor;
    }

    public void setBarColor(int barColor) {
        this.mBarColor = barColor;
        this.updatePropsIfNeed();
    }

    public int getRimWidth() {
        return this.mRimWidth;
    }

    public void setRimWidth(int rimWidth) {
        this.mRimWidth = rimWidth;
        this.updatePropsIfNeed();
    }

    public int getRimColor() {
        return this.mRimColor;
    }

    public void setRimColor(int rimColor) {
        this.mRimColor = rimColor;
        this.updatePropsIfNeed();
    }

    public float getSpinSpeed() {
        return this.mSpinSpeed;
    }

    public void setSpinSpeed(float spinSpeed) {
        this.mSpinSpeed = spinSpeed;
        this.updatePropsIfNeed();
    }
}
