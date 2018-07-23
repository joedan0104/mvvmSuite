package com.jindan.views.sweetalertdialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.jindan.views.R;

/**
 * Author zhaolei
 * Date 2018/4/12
 * Description desc
 */

public class Rotate3dAnimation extends Animation {
    private int mPivotXType = 0;
    private int mPivotYType = 0;
    private float mPivotXValue = 0.0F;
    private float mPivotYValue = 0.0F;
    private float mFromDegrees;
    private float mToDegrees;
    private float mPivotX;
    private float mPivotY;
    private Camera mCamera;
    private int mRollType;
    public static final int ROLL_BY_X = 0;
    public static final int ROLL_BY_Y = 1;
    public static final int ROLL_BY_Z = 2;

    Rotate3dAnimation.Description parseValue(TypedValue value) {
        Rotate3dAnimation.Description d = new Rotate3dAnimation.Description();
        if (value == null) {
            d.type = 0;
            d.value = 0.0F;
        } else {
            if (value.type == 6) {
                d.type = (value.data & 15) == 1 ? 2 : 1;
                d.value = TypedValue.complexToFloat(value.data);
                return d;
            }

            if (value.type == 4) {
                d.type = 0;
                d.value = value.getFloat();
                return d;
            }

            if (value.type >= 16 && value.type <= 31) {
                d.type = 0;
                d.value = (float) value.data;
                return d;
            }
        }

        d.type = 0;
        d.value = 0.0F;
        return d;
    }

    public Rotate3dAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Rotate3dAnimation);
        this.mFromDegrees = a.getFloat(R.styleable.Rotate3dAnimation_fromDeg, 0.0F);
        this.mToDegrees = a.getFloat(R.styleable.Rotate3dAnimation_toDeg, 0.0F);
        this.mRollType = a.getInt(R.styleable.Rotate3dAnimation_rollType, 0);
        Rotate3dAnimation.Description d = this.parseValue(a.peekValue(R.styleable.Rotate3dAnimation_pivotX));
        this.mPivotXType = d.type;
        this.mPivotXValue = d.value;
        d = this.parseValue(a.peekValue(R.styleable.Rotate3dAnimation_pivotY));
        this.mPivotYType = d.type;
        this.mPivotYValue = d.value;
        a.recycle();
        this.initializePivotPoint();
    }

    public Rotate3dAnimation(int rollType, float fromDegrees, float toDegrees) {
        this.mRollType = rollType;
        this.mFromDegrees = fromDegrees;
        this.mToDegrees = toDegrees;
        this.mPivotX = 0.0F;
        this.mPivotY = 0.0F;
    }

    public Rotate3dAnimation(int rollType, float fromDegrees, float toDegrees, float pivotX, float pivotY) {
        this.mRollType = rollType;
        this.mFromDegrees = fromDegrees;
        this.mToDegrees = toDegrees;
        this.mPivotXType = 0;
        this.mPivotYType = 0;
        this.mPivotXValue = pivotX;
        this.mPivotYValue = pivotY;
        this.initializePivotPoint();
    }

    public Rotate3dAnimation(int rollType, float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue) {
        this.mRollType = rollType;
        this.mFromDegrees = fromDegrees;
        this.mToDegrees = toDegrees;
        this.mPivotXValue = pivotXValue;
        this.mPivotXType = pivotXType;
        this.mPivotYValue = pivotYValue;
        this.mPivotYType = pivotYType;
        this.initializePivotPoint();
    }

    private void initializePivotPoint() {
        if (this.mPivotXType == 0) {
            this.mPivotX = this.mPivotXValue;
        }

        if (this.mPivotYType == 0) {
            this.mPivotY = this.mPivotYValue;
        }

    }

    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        this.mCamera = new Camera();
        this.mPivotX = this.resolveSize(this.mPivotXType, this.mPivotXValue, width, parentWidth);
        this.mPivotY = this.resolveSize(this.mPivotYType, this.mPivotYValue, height, parentHeight);
    }

    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float fromDegrees = this.mFromDegrees;
        float degrees = fromDegrees + (this.mToDegrees - fromDegrees) * interpolatedTime;
        Matrix matrix = t.getMatrix();
        this.mCamera.save();
        switch (this.mRollType) {
            case 0:
                this.mCamera.rotateX(degrees);
                break;
            case 1:
                this.mCamera.rotateY(degrees);
                break;
            case 2:
                this.mCamera.rotateZ(degrees);
        }

        this.mCamera.getMatrix(matrix);
        this.mCamera.restore();
        matrix.preTranslate(-this.mPivotX, -this.mPivotY);
        matrix.postTranslate(this.mPivotX, this.mPivotY);
    }

    protected static class Description {
        public int type;
        public float value;

        protected Description() {
        }
    }
}
