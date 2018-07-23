package net.hmzs.views.horizontalscrollmenu;

import android.view.View;

import java.util.List;

/**
 * 进行内容处理的抽象类
 *
 * @author LynnChurch
 * @version 创建时间:2015年6月25日 下午3:34:43
 */
public abstract class BaseAdapter {
    private HorizontalScrollMenu mHorizontalScrollMenu;

    public abstract List<String> getMenuItems();

    public abstract List<View> getContentViews();

    /**
     * 切换到某一个菜单项位置
     *
     * @param position
     * @param visitStatus
     */
    public abstract void onPageChanged(int position, boolean visitStatus);

    public void setHorizontalScrollMenu(HorizontalScrollMenu horizontalScrollMenu) {
        mHorizontalScrollMenu = horizontalScrollMenu;
    }

    public void notifyDataSetChanged() {
        mHorizontalScrollMenu.notifyDataSetChanged(this);
    }
}