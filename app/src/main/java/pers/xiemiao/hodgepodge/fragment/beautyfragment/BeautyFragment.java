package pers.xiemiao.hodgepodge.fragment.beautyfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.base.BaseBeautyFragment;
import pers.xiemiao.hodgepodge.factory.BeautyFragmentFactory;
import pers.xiemiao.hodgepodge.utils.LogUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;
import pers.xiemiao.hodgepodge.views.ScalePageTransformer;

/**
 * User: xiemiao
 * Date: 2016-10-10
 * Time: 18:21
 * Desc: 美女图片选项卡的fragment
 */
public class BeautyFragment extends Fragment {

    private PagerSlidingTabStrip mBeautyTabs;
    private ViewPager mBeautyViewpager;
    private String[] mTitleArr;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_beauty, container, false);
            mBeautyTabs = (PagerSlidingTabStrip) view.findViewById(R.id.beauty_tabs);
            mBeautyViewpager = (ViewPager) view.findViewById(R.id.beauty_viewpager);
            //设置缩放旋转
            mBeautyViewpager.setPageTransformer(true, new ScalePageTransformer());
            initData();
        }
        return view;
    }

    /**
     * 初始化展示的数据
     */
    private void initData() {
        //1初始化标题数组
        mTitleArr = UIUtils.getStringArray(R.array.beauty_titles);
        //2创建适配器对象
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter
                (getFragmentManager());
        //3设置适配器
        mBeautyViewpager.setAdapter(myFragmentPagerAdapter);
        //4将滑动选项卡与viewpager进行绑定
        mBeautyTabs.setViewPager(mBeautyViewpager);
        //设置viewpager页面改变监听
        mBeautyTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.sf("onPageSelected加载数据");
                //当viewpager被选中的时候,触发加载数据
                BaseBeautyFragment fragment = BeautyFragmentFactory.getFragment(position);
                fragment.getLoaddingPager().loadData();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 主页面适配器
     */
    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override//返回fragment对象
        public BaseBeautyFragment getItem(int position) {
            //使用fragment工厂实现
            BaseBeautyFragment fragment = BeautyFragmentFactory.getFragment(position);
            return fragment;
        }

        @Override
        public int getCount() {
            if (mTitleArr != null) {
                return mTitleArr.length;
            }
            return 0;
        }

        @Override//页面标题，必须实现
        public CharSequence getPageTitle(int position) {
            return mTitleArr[position];
        }
    }


}
