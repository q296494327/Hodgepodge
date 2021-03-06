package pers.xiemiao.hodgepodge.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import cn.sharesdk.framework.ShareSDK;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.base.BaseActivity;
import pers.xiemiao.hodgepodge.dialog.QuickOptionDialog;
import pers.xiemiao.hodgepodge.utils.StringUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;
import pers.xiemiao.hodgepodge.views.MyFragmentTabHost;

public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private FrameLayout mTabContent;
    private MyFragmentTabHost mTabHost;
    private ImageView mQuickOptionIv;
    private FrameLayout mLeftMenu;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private ImageView mIvMainTitle;
    private ImageView mImageView;

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        ShareSDK.initSDK(this);//初始化ShareSDK
        mIvMainTitle = (ImageView) findViewById(R.id.iv_mian_title);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTabHost = (MyFragmentTabHost) findViewById(R.id.tabHost);
        mQuickOptionIv = (ImageView) findViewById(R.id.quick_option_iv);
        mLeftMenu = (FrameLayout) findViewById(R.id.left_menu);
        //1将tabhost和帧布局容器绑定
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tabContent);
        mTabHost.getTabWidget().setShowDividers(0);//设置无分割线
        initTabSpec();//初始化tab标签
    }

    @Override
    public void initActionBar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);//将toolbar作为actionbar使用
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string
                .open, R.string.close);
        mDrawerLayout.setDrawerListener(mToggle);//设置拖拽监听给开关
        mToggle.setDrawerIndicatorEnabled(true);//暂时隐藏掉控制侧边栏的开关
        mToggle.syncState();//同步状态
    }


    @Override
    public void initData() {
        mImageView = new ImageView(this);
        mImageView.setImageResource(R.mipmap.m2);
        mLeftMenu.addView(mImageView);
    }

    @Override
    public void initListener() {
        //快速选择图标点击监听
        mQuickOptionIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuickDialog();
            }
        });
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (StringUtils.isEquals(tabId, "轻松一刻")) {
                    mIvMainTitle.setBackgroundResource(R.mipmap.qsyk);
                } else if (StringUtils.isEquals(tabId, "三更有鬼")) {
                    mIvMainTitle.setBackgroundResource(R.mipmap.sgyg);
                } else if (StringUtils.isEquals(tabId, "动感萌妹")) {
                    mIvMainTitle.setBackgroundResource(R.mipmap.dgmm);
                } else if (StringUtils.isEquals(tabId, "动感漫画")) {
                    mIvMainTitle.setBackgroundResource(R.mipmap.dgmh);
                }
            }
        });
    }

    /**
     * 展示快速选择对话框
     */
    private void showQuickDialog() {
        final QuickOptionDialog dialog = new QuickOptionDialog(this);
        dialog.setCancelable(true);//设置可取消
        dialog.setCanceledOnTouchOutside(true);//设置触碰到dialog之外可取消
        dialog.show();
        //设置选择对话框里面的控件的点击监听
        dialog.setOnQuickOptionformClickListener(new QuickOptionDialog.OnQuickOptionformClick() {
            @Override
            public void onQuickOptionClick(int id) {
                switch (id) {
                    case R.id.iv_close:
                        dialog.dismiss();
                        break;
                    case R.id.ly_quick_option_star:
                        //跳到星座今日运势的activity
                        startActivity(new Intent(MainActivity.this, StarActivity.class));
                        break;
                }
            }
        });
    }


    /**
     * 初始化选项卡
     */
    private void initTabSpec() {
        //1获取枚举的所有值数组
        MainTab[] mainTabs = MainTab.values();
        //2遍历枚举,去添加枚举里的标签
        for (int i = 0; i < mainTabs.length; i++) {
            MainTab mainTab = mainTabs[i];//获取单个枚举对象
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mainTab.name);//以标签名做标记
            /*-------------------自定义选项卡-----begin----------------*/
            //1使用打气筒初始化选项卡布局
            View indicatorView = View.inflate(this, R.layout.tab_indicator, null);
            //2获取控件
            TextView tabTitle = (TextView) indicatorView.findViewById(R.id.tab_title);
            //3初始化控件显示的标题以及图片
            Drawable drawable = UIUtils.getResources().getDrawable(mainTab.icon);
            tabTitle.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);//设置图标
            tabTitle.setText(mainTab.name);//设置文本
            tabSpec.setIndicator(indicatorView);//设置指示器布局,即填充的indicatorView
            /*-------------------自定义选项卡-----end----------------*/

            if (i == 2) {
                //如果i是2的话,说明是中间的快速选项卡,将其占位隐藏
                indicatorView.setVisibility(View.INVISIBLE);
                mTabHost.setNoTabChangedTag(mainTab.name);
            }

            /*-------------------选项卡初始化完毕,添加到mTabHost---------------------*/
            Bundle bundle = new Bundle();
            bundle.putString("text", mainTab.name);//传值过去
            mTabHost.addTab(tabSpec, mainTab.clz, bundle);
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    /*-------------------友盟统计---------------------*/
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainActivity"); //统计页面
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainActivity");
        MobclickAgent.onPause(this);
        JCVideoPlayer.releaseAllVideos();
    }

}
