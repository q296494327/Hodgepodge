package pers.xiemiao.hodgepodge.fragment.ghostfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.activity.ChannelActivity;
import pers.xiemiao.hodgepodge.base.BaseGhostFragment;
import pers.xiemiao.hodgepodge.bean.MessageEvent;
import pers.xiemiao.hodgepodge.conf.Constants;
import pers.xiemiao.hodgepodge.factory.GhostFragmentFactory;
import pers.xiemiao.hodgepodge.utils.LogUtils;
import pers.xiemiao.hodgepodge.utils.SpUtil;
import pers.xiemiao.hodgepodge.utils.UIUtils;
import pers.xiemiao.hodgepodge.views.LoaddingPager;

/**
 * User: xiemiao
 * Date: 2016-10-10
 * Time: 18:21
 * Desc: 鬼故事选项卡的fragment
 */
public class GhostTabFragment extends Fragment {

    private PagerSlidingTabStrip mNewsTabs;
    private ViewPager mNewsViewpager;
    private String[] mTitleArr;
    private View view;
    private ImageView mMoreChannel;
    private MyFragmentPagerAdapter mFragmentPagerAdapter;
    private List<BaseGhostFragment> fragments = new ArrayList<>();
    public static final int TAG = 2;
    private String[] mDefValues;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_news, container, false);
            mMoreChannel = (ImageView) view.findViewById(R.id.more_channel);
            mNewsTabs = (PagerSlidingTabStrip) view.findViewById(R.id.news_tabs);
            mNewsViewpager = (ViewPager) view.findViewById(R.id.news_viewpager);
            //设置缩放旋转
//            mNewsViewpager.setPageTransformer(true, new ScalePageTransformer());
            initData();

            mMoreChannel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到频道排序列表，并将标题数据传递过去
                    Intent intent = new Intent(getActivity(), ChannelActivity.class);
                    intent.putStringArrayListExtra("TITLES", new ArrayList<String>
                            (Arrays.asList(mTitleArr)));
                    intent.putExtra("TAG", TAG);//传递一个标记过去用来区分
                    getActivity().startActivity(intent);
                }
            });
        }
        return view;
    }

    /**
     * 初始化展示的数据
     */
    private void initData() {
        //1初始化标题数组
        mDefValues = UIUtils.getStringArray(R.array.news_titles);
        mTitleArr = SpUtil.getStringArr(getActivity(), Constants.GHOST_TITLES, mDefValues);
        //2创建适配器对象
        initFragments();//初始化fragments集合
        mFragmentPagerAdapter = new MyFragmentPagerAdapter
                (getFragmentManager(),fragments);
        //3设置适配器
        mNewsViewpager.setAdapter(mFragmentPagerAdapter);
        //4将滑动选项卡与viewpager进行绑定
        mNewsTabs.setViewPager(mNewsViewpager);
        //设置viewpager页面改变监听
        mNewsTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.sf("onPageSelected加载数据");
                //当viewpager被选中的时候,触发加载数据
                BaseGhostFragment fragment = fragments.get(position);
                LoaddingPager loaddingPager = fragment.getLoaddingPager();
                if (loaddingPager != null) {
                    loaddingPager.loadData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化fragments，为了保证fragment的顺序和title的顺序一致
     */
    private List<BaseGhostFragment> initFragments() {
        fragments.clear();
        for (int i = 0; i < mTitleArr.length; i++) {
            for (int j = 0; j < GhostFragmentFactory.getFragmentCount(); j++) {
                BaseGhostFragment fragment = GhostFragmentFactory.getFragment(j);
                if (mTitleArr[i].equals(fragment.getTitle())) {
                    fragments.add(fragment);
                }
            }
        }
        return fragments;
    }

    /**
     * 主页面适配器
     */
    class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

        private List<BaseGhostFragment> fragmentdatas;

        public MyFragmentPagerAdapter(FragmentManager fm, List<BaseGhostFragment> fragments) {
            super(fm);
            fragmentdatas = fragments;
        }

        @Override//返回fragment对象
        public BaseGhostFragment getItem(int position) {
            return fragmentdatas.get(position);
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

        @Override//复写返回的页面索引
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    /*================在fragment创建和销毁时,注册和注销EventBus=================*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /*================接收EventBus传过来的消息,做出相应的操作=================*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.msg.equals(TAG + "")) {
            //收到更新频道的消息,就去更新适配器
            //重新获取标题数组
            mTitleArr = SpUtil.getStringArr(getActivity(), Constants.GHOST_TITLES, mDefValues);
            initFragments();
            mFragmentPagerAdapter.notifyDataSetChanged();
            mNewsTabs.setViewPager(mNewsViewpager);
        }
        if (event.msg.equals("click"+TAG)) {
            for (int i = 0; i < fragments.size(); i++) {
                if (fragments.get(i).getTitle().equals(event.value)) {
                    mNewsViewpager.setCurrentItem(i);
                }
            }
        }
    }

    /*-------------------fragment页面统计---------------------*/
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("GhostTab"); //统计页面
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("GhostTab");
    }


}
