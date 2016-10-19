package pers.xiemiao.hodgepodge.fragment.cartoonfragment;

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
import pers.xiemiao.hodgepodge.base.BaseCartoonFragment;
import pers.xiemiao.hodgepodge.factory.CartoonFragmentFactory;
import pers.xiemiao.hodgepodge.utils.LogUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;
import pers.xiemiao.hodgepodge.views.ScalePageTransformer;

/**
 * User: xiemiao
 * Date: 2016-10-10
 * Time: 18:21
 * Desc: 卡通漫画选项卡的fragment
 */
public class CartoonTabFragment extends Fragment {

    private PagerSlidingTabStrip mCartoonyTabs;
    private ViewPager mCartoonyViewpager;
    private String[] mTitleArr;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_cartoon, container, false);
            mCartoonyTabs = (PagerSlidingTabStrip) view.findViewById(R.id.cartoon_tabs);
            mCartoonyViewpager = (ViewPager) view.findViewById(R.id.cartoon_viewpager);
            //设置缩放旋转
            mCartoonyViewpager.setPageTransformer(true, new ScalePageTransformer());
            initData();
        }
        return view;
    }

    /**
     * 初始化展示的数据
     */
    private void initData() {
        //1初始化标题数组
        mTitleArr = UIUtils.getStringArray(R.array.cartoon_titles);
        //2创建适配器对象
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter
                (getFragmentManager());
        //3设置适配器
        mCartoonyViewpager.setAdapter(myFragmentPagerAdapter);
        //4将滑动选项卡与viewpager进行绑定
        mCartoonyTabs.setViewPager(mCartoonyViewpager);
        //设置viewpager页面改变监听
        mCartoonyTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.sf("onPageSelected加载数据");
                //当viewpager被选中的时候,触发加载数据
                BaseCartoonFragment fragment = CartoonFragmentFactory.getFragment(position);
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
        public BaseCartoonFragment getItem(int position) {
            //使用fragment工厂实现
            BaseCartoonFragment fragment = CartoonFragmentFactory.getFragment(position);
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
