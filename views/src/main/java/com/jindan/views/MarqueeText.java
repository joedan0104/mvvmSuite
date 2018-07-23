package com.jindan.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/5/9 09:58
 * <p/>
 * Description: TextView 跑马灯效果
 */
@SuppressLint("AppCompatCustomView")
public class MarqueeText extends TextView {
    public MarqueeText(Context context) {
        this(context, null);
    }

    public MarqueeText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setGravity(Gravity.CENTER);
        // marquee_forever
        setMarqueeRepeatLimit(-1);
        setSingleLine(true);
        setMaxLines(1);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
