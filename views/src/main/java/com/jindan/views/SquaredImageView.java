package com.jindan.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/** An image view which always remains square with respect to its width. */
@SuppressLint("AppCompatCustomView")
public class SquaredImageView extends ImageView {
  public SquaredImageView(Context context) {
    super(context);
  }

  public SquaredImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
  }

  @Override
  protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      //在它从屏幕中消失时回调，去掉drawable引用，能加快内存的回收
      setImageDrawable(null);
  }
}
