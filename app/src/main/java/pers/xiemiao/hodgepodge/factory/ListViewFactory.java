package pers.xiemiao.hodgepodge.factory;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import com.markmao.pulltorefresh.widget.XListView;

import pers.xiemiao.hodgepodge.utils.UIUtils;

/**
 * User: xiemiao
 * Date: 2016-10-07
 * Time: 17:30
 * Desc: listview的简单抽取
 */
public class ListViewFactory {
    public static XListView createXListView() {
        XListView listView = new XListView(UIUtils.getContext());
        listView.setPullRefreshEnable(true);
        listView.setPullLoadEnable(true);
        listView.setFastScrollEnabled(true);//右边的滚动条
        listView.setCacheColorHint(Color.TRANSPARENT);//缓存颜色
        listView.setAutoLoadEnable(true);//自动加载更多
        listView.setSelector(new BitmapDrawable());//选择器背景
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);//去除下拉时的蓝色阴影
        return listView;
    }
}
