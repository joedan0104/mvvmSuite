package com.jindan.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class IconFontTextView extends TextView {

	private Context context;

	public IconFontTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	public IconFontTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public IconFontTextView(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	private void initView() {
		try {
			Typeface iconfont = Typeface.createFromAsset(context.getAssets(),
					"fonts/iconfont.ttf");
			setTypeface(iconfont);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
