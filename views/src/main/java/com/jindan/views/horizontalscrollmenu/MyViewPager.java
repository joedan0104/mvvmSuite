package net.hmzs.views.horizontalscrollmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import android.support.v4.view.ViewPager;

/**
 * 可以禁止滑动的ViewPager
 * 
 * @author LynnChurch
 * @version 创建时间:2015年6月30日 下午5:27:20
 * 
 */
public class MyViewPager extends ViewPager
{

	private boolean mSwiped = true; // 是否可滑动

	public MyViewPager(Context context)
	{
		this(context, null);
	}

	public MyViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public void setSwiped(boolean swiped)
	{
		mSwiped = swiped;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		if (mSwiped)
		{
			return super.onInterceptTouchEvent(ev);
		} else
		{
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if ((mSwiped))
		{
			return super.onTouchEvent(event);
		} else
		{
			return true;
		}
	}
}
