package com.jindan.views.sweetalertdialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;
import com.jindan.views.R;

/**
 * Author zhaolei
 * Date 2018/4/12
 * Description desc
 */

@SuppressLint("AppCompatCustomView")
public class PasswordInputView extends EditText {
    private int textLength;
    private int borderColor;
    private float borderWidth;
    private float borderRadius;
    private int passwordLength;
    private int passwordColor;
    private float passwordWidth;
    private float passwordRadius;
    private Paint passwordPaint = new Paint(1);
    private Paint borderPaint = new Paint(1);
    private final int defaultSplitLineWidth = 1;

    public PasswordInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Resources res = this.getResources();
        int defaultBorderColor = res.getColor(R.color.border_line_grey);
        float defaultBorderWidth = res.getDimension(R.dimen.x1);
        float defaultBorderRadius = res.getDimension(R.dimen.x6);
        int defaultPasswordColor = res.getColor(R.color.text_black);
        float defaultPasswordWidth = res.getDimension(R.dimen.x6);
        float defaultPasswordRadius = res.getDimension(R.dimen.x6);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PasswordInputView, 0, 0);

        try {
            this.borderColor = a.getColor(R.styleable.PasswordInputView_borderColor, defaultBorderColor);
            this.borderWidth = a.getDimension(R.styleable.PasswordInputView_borderSize, defaultBorderWidth);
            this.borderRadius = a.getDimension(R.styleable.PasswordInputView_borderRadius, defaultBorderRadius);
            this.passwordLength = a.getInt(R.styleable.PasswordInputView_passwordLength, 6);
            this.passwordColor = a.getColor(R.styleable.PasswordInputView_passwordColor, defaultPasswordColor);
            this.passwordWidth = a.getDimension(R.styleable.PasswordInputView_passwordWidth, defaultPasswordWidth);
            this.passwordRadius = a.getDimension(R.styleable.PasswordInputView_passwordRadius, defaultPasswordRadius);
        } finally {
            a.recycle();
        }

        this.borderPaint.setStrokeWidth(this.borderWidth);
        this.borderPaint.setColor(this.borderColor);
        this.passwordPaint.setStrokeWidth(this.passwordWidth);
        this.passwordPaint.setStyle(Paint.Style.FILL);
        this.passwordPaint.setColor(this.passwordColor);
        this.setSingleLine(true);
    }

    protected void onDraw(Canvas canvas) {
        int width = this.getWidth();
        int height = this.getHeight();
        this.borderPaint.setColor(this.borderColor);
        this.borderPaint.setStrokeWidth(1.0F);

        float cy;
        for (int i = 1; i < this.passwordLength; ++i) {
            cy = (float) (width * i / this.passwordLength);
            canvas.drawLine(cy, 0.0F, cy, (float) height, this.borderPaint);
        }

        cy = (float) (height / 2);
        float half = (float) (width / this.passwordLength / 2);

        for (int i = 0; i < this.textLength; ++i) {
            float cx = (float) (width * i / this.passwordLength) + half;
            canvas.drawCircle(cx, cy, this.passwordWidth, this.passwordPaint);
        }

    }

    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.textLength = text.toString().length();
        this.invalidate();
    }

    public int getBorderColor() {
        return this.borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        this.borderPaint.setColor(borderColor);
        this.invalidate();
    }

    public float getBorderWidth() {
        return this.borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        this.borderPaint.setStrokeWidth(borderWidth);
        this.invalidate();
    }

    public float getBorderRadius() {
        return this.borderRadius;
    }

    public void setBorderRadius(float borderRadius) {
        this.borderRadius = borderRadius;
        this.invalidate();
    }

    public int getPasswordLength() {
        return this.passwordLength;
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
        this.invalidate();
    }

    public int getPasswordColor() {
        return this.passwordColor;
    }

    public void setPasswordColor(int passwordColor) {
        this.passwordColor = passwordColor;
        this.passwordPaint.setColor(passwordColor);
        this.invalidate();
    }

    public float getPasswordWidth() {
        return this.passwordWidth;
    }

    public void setPasswordWidth(float passwordWidth) {
        this.passwordWidth = passwordWidth;
        this.passwordPaint.setStrokeWidth(passwordWidth);
        this.invalidate();
    }

    public float getPasswordRadius() {
        return this.passwordRadius;
    }

    public void setPasswordRadius(float passwordRadius) {
        this.passwordRadius = passwordRadius;
        this.invalidate();
    }
}
