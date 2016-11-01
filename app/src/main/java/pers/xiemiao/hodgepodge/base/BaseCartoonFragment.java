package pers.xiemiao.hodgepodge.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pers.xiemiao.hodgepodge.views.LoaddingPager;

/**
 * User: xiemiao
 * Date: 2016-10-05
 * Time: 18:02
 * Desc: 基类卡通漫画fragment，封装共性内容
 */
public abstract class BaseCartoonFragment extends Fragment {

    public LoaddingPager mLoaddingPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        //使用LoaddingPager类去控制fragment的共性内容
        //MVC模式 M:数据加载逻辑 V：布局展示逻辑 C：LoaddingPager作为控制器
        if (mLoaddingPager == null) {//只有在mLoaddingPager是空的时候才去创建
            mLoaddingPager = new LoaddingPager(getContext()) {
                @Override
                public LoadResult initData() {
                    return BaseCartoonFragment.this.initData();
                }

                @Override
                public View initSuccessView() {
                    return BaseCartoonFragment.this.initSuccessView();
                }
            };
        }
        return mLoaddingPager;
    }

    /**
     * 提供一个get方法，方便取得LoaddingPager控制器，去触发加载数据方法
     */
    public LoaddingPager getLoaddingPager() {
        return mLoaddingPager;
    }

    /**
     * 初始化数据的抽象方法，由子类必须实现
     */
    public abstract LoaddingPager.LoadResult initData();

    /**
     * 初始化加载成功的布局，由子类必须实现
     */
    public abstract View initSuccessView();


    /**
     * 检测网络请求的数据状态
     */
    public LoaddingPager.LoadResult checkState(Object obj) {
        if (obj != null) {
            if (obj instanceof List) {
                if (((List) obj).isEmpty()) {
                    return LoaddingPager.LoadResult.LOADEMPTY;
                } else {
                    return LoaddingPager.LoadResult.LOADSUCESS;
                }
            }
        }
        return LoaddingPager.LoadResult.LOADERROR;
    }
}
