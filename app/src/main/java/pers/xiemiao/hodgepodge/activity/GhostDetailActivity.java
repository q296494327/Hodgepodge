package pers.xiemiao.hodgepodge.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.bean.GhostDetailBean;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.protocol.GhostDetailProtocol;
import pers.xiemiao.hodgepodge.utils.SpUtil;
import pers.xiemiao.hodgepodge.views.MyScrollView;
import pers.xiemiao.hodgepodge.views.VerticalViewPager;

/**
 * User: xiemiao
 * Date: 2016-10-14
 * Time: 16:30
 * Desc: 鬼故事详情页
 */
public class GhostDetailActivity extends AppCompatActivity {

    private String mId;
    private VerticalViewPager mGhostViewpager;
    private GhostDetailProtocol mProtocol;
    private List<String> mTextList = new ArrayList<String>();
    private TextView mTvCount;
    private String mTitle;
    private TextView mTvTitle;
    private TextView mTvSmall;
    private TextView mTvSize;
    private TextView mTvBig;
    private GhostAdapter mGhostAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost_detail);
        init();
        initView();
        initData();
        initListener();
    }

    public void init() {
        mId = getIntent().getStringExtra("id");
        mTitle = getIntent().getStringExtra("title");
    }

    public void initView() {
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTitle.setText(mTitle);
        mGhostViewpager = (VerticalViewPager) findViewById(R.id.ghost_viewpager);
        mTvCount = (TextView) findViewById(R.id.tv_count);
        mTvSmall = (TextView) findViewById(R.id.tv_small);
        mTvBig = (TextView) findViewById(R.id.tv_big);
        mTvSize = (TextView) findViewById(R.id.tv_size);
    }

    private void initData() {
        ThreadPoolFactory.getNormalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mProtocol = new GhostDetailProtocol();
                    //先获取到所有页数
                    GhostDetailBean ghostDetailBean = mProtocol.loadData(mId, 1);
                    int allPages = ghostDetailBean.showapi_res_body.allPages;
                    //根据页数去请求每页的内容数据，封装每页的内容
                    for (int i = 1; i <= allPages; i++) {
                        GhostDetailBean bean = mProtocol.loadData(mId, i);
                        String text = bean.showapi_res_body.text;
                        mTextList.add(text);
                    }
                    //然后在主线程去更新UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //创建viewpager的适配器
                            mGhostAdapter = new GhostAdapter(mTextList);
                            mGhostViewpager.setAdapter(mGhostAdapter);
                            //在适配器创建完之后,获取存着的历史记录,设置为当前条目,达到存档的效果
                            int currentItem = SpUtil.getInt(GhostDetailActivity.this,
                                    "currentItem" + mId, 0);
                            mGhostViewpager.setCurrentItem(currentItem);
                            //设置底部翻页文本显示
                            mTvCount.setText("↓     " + (currentItem + 1) + "/" + mTextList.size());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initListener() {
        mGhostViewpager.setOnPageChangeListener(new VerticalViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //页面改变时,动态设置底部页数
                mTvCount.setText("↓     " + (position + 1) + "/" + mTextList.size());
                //将页面选择状况存到sp里
                SpUtil.putInt(GhostDetailActivity.this, "currentItem" + mId, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //字体变小监听
        mTvSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTvText != null) {
                    int fontSize = SpUtil.getInt(getApplicationContext(), "ghost_size", 18);
                    if (fontSize > 10) {
                        fontSize--;
                    }
                    mTvText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                    mTvSize.setText(fontSize + "");
                    if (mGhostAdapter != null) {
                        mGhostAdapter.notifyDataSetChanged();
                    }
                    SpUtil.putInt(getApplicationContext(), "ghost_size", fontSize);
                }
            }
        });
        //字体变大监听
        mTvBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTvText != null) {
                    int fontSize = SpUtil.getInt(getApplicationContext(), "ghost_size", 18);
                    if (fontSize < 40) {
                        fontSize++;
                    }
                    mTvText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                    mTvSize.setText(fontSize + "");
                    if (mGhostAdapter != null) {
                        mGhostAdapter.notifyDataSetChanged();
                    }
                    SpUtil.putInt(getApplicationContext(), "ghost_size", fontSize);
                }
            }
        });
    }

    private MyScrollView mScrollView;
    private TextView mTvText;

    class GhostAdapter extends PagerAdapter {
        private List<String> textList;

        public GhostAdapter(List<String> textList) {
            this.textList = textList;
        }

        @Override
        public int getCount() {
            return textList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(GhostDetailActivity.this, R.layout.viewpager_ghost, null);
            mScrollView = (MyScrollView) view.findViewById(R.id.scrollView);
            mScrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);//设置滚动无蓝阴影
            mTvText = (TextView) view.findViewById(R.id.tv_text);
            //从sp里取字体大小，设置进去
            int ghost_size = SpUtil.getInt(getApplicationContext(), "ghost_size", 18);
            mTvText.setTextSize(TypedValue.COMPLEX_UNIT_SP, ghost_size);
            mTvSize.setText(ghost_size + "");

            String text = textList.get(position);
            text = text.replace("shoye_336();", "").replace("&nbsp;", " ").replace("var cpro_id " +
                    "= \"u138765\";", "").replace("var cpro_id = \"u535693\";", "").replace
                    ("\r\n\r\n\r\n", "");
            mTvText.setText(text);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /*-------------------友盟统计---------------------*/
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("NewsDeatilActivity"); //统计页面
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("NewsDeatilActivity");
        MobclickAgent.onPause(this);
    }
}
