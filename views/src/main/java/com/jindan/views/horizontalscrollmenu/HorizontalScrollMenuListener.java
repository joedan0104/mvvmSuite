package net.hmzs.views.horizontalscrollmenu;

/**
 * 水平滚动菜单事件监听
 *
 * @author Joe
 * @version 1.0 2018/06/30
 */
public interface HorizontalScrollMenuListener {
    /**
     * 切换到某一个菜单项位置
     *
     * @param position
     * @param visitStatus
     */
    public abstract void onPageChanged(int position, boolean visitStatus);
}
