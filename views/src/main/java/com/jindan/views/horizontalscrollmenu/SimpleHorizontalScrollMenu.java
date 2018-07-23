package net.hmzs.views.horizontalscrollmenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import net.hmzs.tools.utils.ListUtils;
import net.hmzs.views.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单的水平滑动菜单
 *
 * @author Joe
 * @version 1.0 2018/06/30
 */
public class SimpleHorizontalScrollMenu extends LinearLayout {

    /**
     * 菜单项点击事件监听
     */
    private HorizontalScrollMenuListener menuListener;
    private RadioGroup rg_items;
    private List<RadioButton> rb_items = new ArrayList<RadioButton>();
    private Context mContext;
    private ColorStateList mColors;
    private int mBackgroundResId;
    private int mPaddingLeft = 20;
    private int mPaddingTop = 20;
    private int mPaddingRight = 20;
    private int mPaddingBottom = 20;
    private HorizontalScrollView hsv_menu;
    private boolean[] mVisitStatus; // 菜单访问状态
    private List<String> mItems; // 菜单名
    private boolean mSwiped = true; // 是否可滑动

    public SimpleHorizontalScrollMenu(Context context)
    {
        this(context, null);
    }

    public SimpleHorizontalScrollMenu(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    @SuppressLint("ResourceType")
    public SimpleHorizontalScrollMenu(Context context, AttributeSet attrs,
                                int defStyle)
    {
        super(context, attrs, defStyle);
        mContext = context;

        View v = LayoutInflater.from(context).inflate(
                R.layout.simple_horizontal_scroll_menu, this, true);
        rg_items = (RadioGroup) v.findViewById(R.id.rg_items);
        mColors = getResources().getColorStateList(
                R.drawable.selector_menu_item_text);
        hsv_menu = (HorizontalScrollView) v.findViewById(R.id.hsv_menu);
        mBackgroundResId = R.drawable.bg_rb_checked;
    }

    /**
     * 设置菜单项内容
     *
     * @param names
     */
    public void setMenu(List<String> names)
    {
        this.mItems = names;
    }

    public HorizontalScrollMenuListener getHorizontalScrollMenuListener() {
        return menuListener;
    }

    public void setHorizontalScrollMenuListener(HorizontalScrollMenuListener menuListener) {
        this.menuListener = menuListener;
    }

    /**
     * 初始化视图
     *
     * @param menuNames
     */
    private void initView(List<String> menuNames)
    {
        if (ListUtils.isEmpty(menuNames))
        {
            return;
        }
        mItems = menuNames;
        mVisitStatus = new boolean[mItems.size()];
        initMenuItems(mItems);
    }

    /**
     * 当数据集改变通知视图重绘
     *
     */
    public void notifyDataSetChanged()
    {
        rg_items.removeAllViews();
        rb_items.clear();
        initView(mItems);
    }

    /**
     * 初始化菜单项
     *
     * @param items
     */
    private void initMenuItems(List<String> items)
    {
        if (null != items && 0 != items.size())
        {
            rg_items.setOnCheckedChangeListener(mItemListener);
            for (String str : items)
            {
                RadioButton rb_item = (RadioButton) LayoutInflater.from(
                        mContext).inflate(R.layout.menu_item, null);
                rb_item.setTextColor(mColors);
                rb_item.setText(str);
                rb_item.setGravity(Gravity.CENTER);
                rb_item.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight,
                        mPaddingBottom);
                rg_items.addView(rb_item);
                rb_items.add(rb_item);
            }
            rb_items.get(0).setChecked(true);
        }

    }

    /**
     * 将菜单项尽量移至中央位置
     *
     * @param rb
     */
    private void moveItemToCenter(RadioButton rb)
    {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int[] locations = new int[2];
        rb.getLocationInWindow(locations);
        int rbWidth = rb.getWidth();
        hsv_menu.smoothScrollBy((locations[0] + rbWidth / 2 - screenWidth / 2),
                0);
    }

    /**
     * 设置颜色变化列表
     *
     * @param colorListId
     */
    public void setColorList(int colorListId)
    {
        mColors = getResources().getColorStateList(colorListId);
    }

    /**
     * 设置菜单项状态背景
     *
     * @param resId
     */
    public void setCheckedBackground(int resId)
    {
        mBackgroundResId = resId;
    }

    /**
     * 菜单项切换监听器
     */
    private RadioGroup.OnCheckedChangeListener mItemListener = new RadioGroup.OnCheckedChangeListener()
    {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            RadioButton btn = (RadioButton) group.findViewById(checkedId);
            setMenuItemsNullBackground();
            btn.setBackgroundResource(mBackgroundResId);
            btn.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight,
                    mPaddingBottom);
            int position = 0;
            for (int i = 0; i < rb_items.size(); i++)
            {
                if (rb_items.get(i) == btn)
                {
                    position = i;
                }
            }
            moveItemToCenter(btn);
            if(null != menuListener) {
                menuListener.onPageChanged(position, mVisitStatus[position]);
            }
            mVisitStatus[position] = true;
        }

    };

    /**
     * 设置所有菜单项的背景为空
     */
    private void setMenuItemsNullBackground()
    {
        if (null != rg_items)
            for (int i = 0; i < rg_items.getChildCount(); i++)
            {
                View v = rg_items.getChildAt(i);
                v.setBackgroundResource(android.R.color.transparent);
            }
    }
}
