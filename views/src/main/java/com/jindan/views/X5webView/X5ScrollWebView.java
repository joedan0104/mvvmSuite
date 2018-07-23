package com.jindan.views.X5webView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;


public class X5ScrollWebView extends X5WebView {
	protected final String TAG = X5ScrollWebView.class.getSimpleName();
	/** ScrollChange事件监听器 */
	public OnScrollChangeListener listener;
    /** 是否已经滑动到底部 */
    private boolean isScrollBottom = false;
    /** 是否已滑动到顶部 */
    private boolean isScrollTop = true;

	/**
	 * 滚动状态事件监听器
	 * 
	 */
	public interface OnScrollChangeListener {
		public void onPageEnd(int l, int t, int oldl, int oldt);
		public void onPageTop(int l, int t, int oldl, int oldt);
		public void onScrollChanged(int l, int t, int oldl, int oldt);
	}

	public X5ScrollWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public X5ScrollWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public X5ScrollWebView(Context context) {
		super(context);
	}

	/**
	 * 滚动变化事件
	 * 说明: 重写滚动事件处理，方便外部回调
	 * 
	 * @param l: left 当前滑动位置的left
	 * @param t: top 当前滑动位置的top
	 * @param oldl: old left 上一个滑动位置left
	 * @param oldt: old top 上一个滑动位置top
	 * 
	 */
	@Override
	public void super_onScrollChanged(int l, int t, int oldl, int oldt) {
		super.super_onScrollChanged(l, t, oldl, oldt);

		float webcontent = getContentHeight() * getScale();// webview的内容区高度
		float webnow = getHeight() + t;// 当前webview的高度
		Log.i(TAG, String.format("webview content height: %f scrollY : %d scale: %f height: %d scroll top: %d scroll old top: %d current top: %d",
				webcontent, getScrollY(), getScale(), getHeight(), t, oldt, getTop()));
		if (Math.abs(webcontent - webnow) < 5) {
			// 滑动位置已经处于底端
            isScrollTop = false;
            isScrollBottom = true;
			if (null != listener) {
				listener.onPageEnd(l, t, oldl, oldt);
			}
			Log.i(TAG, String.format("webview onPageEnd scroll top: %d old top: %d", t, oldt));
		} else if (Math.abs(t) < 5) {
			//滑动位置已经处于顶端
            isScrollTop = false;
            isScrollBottom = true;
			if (null != listener) {
				listener.onPageTop(l, t, oldl, oldt);
			}
			Log.i(TAG, String.format("webview onPageTop scroll top: %d old top: %d", t, oldt));
		} else {
			//滑动变化中
            isScrollTop = false;
            isScrollBottom = false;
			if (null != listener) {
				listener.onScrollChanged(l, t, oldl, oldt);
			}
			Log.i(TAG, String.format("webview onScrollChanged scroll top: %d old top: %d", t, oldt));
		}
	}

    public boolean isScrollBottom() {
        return isScrollBottom;
    }

    public boolean isScrollTop() {
        return isScrollTop;
    }

    /**
	 * 设置Scroll事件监听器
	 * 
	 */
	public void setOnScrollChangeListener(OnScrollChangeListener listener) {
		this.listener = listener;
	}

}
