package pers.xiemiao.hodgepodge.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shizhefei.view.largeimage.LongImageView;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import okhttp3.Response;
import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.bean.MessageEvent;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.protocol.CartoonDetailProtocol;
import pers.xiemiao.hodgepodge.utils.FileUtils;
import pers.xiemiao.hodgepodge.utils.SpUtil;
import pers.xiemiao.hodgepodge.views.MyScrollView;
import pers.xiemiao.hodgepodge.views.VerticalViewPager;

/**
 * User: xiemiao
 * Date: 2016-10-18
 * Time: 22:44
 * Desc: 卡通漫画详情页
 */
public class CartoonDetailActivity extends AppCompatActivity implements VerticalViewPager
        .OnPageChangeListener {
    private String mLinkId;
    private VerticalViewPager mViewPager;
    private CartoonDetailProtocol mProtocol;
    private TextView mTvCount;
    private List<String> mImgList;
    private LongImageView mIvCartoon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartoon_detail);
        mLinkId = getIntent().getStringExtra("linkid");
        mTvCount = (TextView) findViewById(R.id.tv_count);
        mViewPager = (VerticalViewPager) findViewById(R.id.cartoon_viewpager);
        mViewPager.setOnPageChangeListener(this);
        initData();
    }

    /**
     * 初始化网络图片的数据
     */
    private void initData() {
        //创建线程任务,执行网络请求图片集
        ThreadPoolFactory.getNormalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mProtocol = new CartoonDetailProtocol();
                    mImgList = mProtocol.loadData(mLinkId, 0).showapi_res_body.item.imgList;
                    //在获取到图片集合后,到主线程去给viewpager去设置适配器
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //在图片集合获取完之后,才去创建适配器
                            MyAdapter myAdapter = new MyAdapter(mImgList);
                            mViewPager.setAdapter(myAdapter);
                            //在适配器创建完之后,获取存着的历史记录,设置为当前条目,达到存档的效果
                            int currentItem = SpUtil.getInt(CartoonDetailActivity.this,
                                    "currentItem" + mLinkId, 0);
                            mViewPager.setCurrentItem(currentItem);
                            //设置底部翻页文本显示
                            mTvCount.setText("↓     " + (currentItem + 1) + "/" + mImgList.size());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 图片viewpager适配器
     */
    class MyAdapter extends PagerAdapter {

        private List<String> mImgList;

        public MyAdapter(List<String> imgList) {
            mImgList = imgList;
        }

        @Override
        public int getCount() {
            return mImgList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            String cartoonUrl = mImgList.get(position);
            //填充布局
            View view = View.inflate(CartoonDetailActivity.this, R.layout.viewpager_cartoon, null);
            MyScrollView scrollView = (MyScrollView) view.findViewById(R.id.scrollView);
            scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);//设置去掉滚动到底时的蓝边阴影
            mIvCartoon = (LongImageView) view.findViewById(R.id.iv_cartoon);
            TextView tv_bug = (TextView) view.findViewById(R.id.tv_bug);
            if (position == 0) {
                tv_bug.setVisibility(View.VISIBLE);
            } else {
                tv_bug.setVisibility(View.GONE);
            }

            String name = cartoonUrl.replace("/", "").replace(":", "").replace("?", "");
            final String manhuaPath = FileUtils.getDir("manhua") + name + ".jpg";
            File file = new File(manhuaPath);//判断文件存不存在
            if (file.exists()) {
                mIvCartoon.setImage(manhuaPath);
            } else {
                initCartoon(cartoonUrl);
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void initCartoon(final String cartoonUrl) {
        ThreadPoolFactory.getNormalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取到图片链接的输入流
                    Response response = OkHttpUtils.get().url(cartoonUrl).build().execute();
                    final InputStream is = response.body().byteStream();
                    //剔除特殊符号作为缓存的文件名
                    String name = cartoonUrl.replace("/", "").replace(":", "").replace("?", "");
                    final String manhuaPath = FileUtils.getDir("manhua") + name + ".jpg";
                    File file = new File(manhuaPath);//判断文件存不存在,不存在才去写缓存
                    if (!file.exists()) {
                        //将得到的输入流写到缓存
                        FileUtils.writeFile(is, manhuaPath, false);
                    }
                    //缓存做好后,到子线程去更新UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mIvCartoon.setImage(manhuaPath);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*-------------------viewpager页面选择状态监听---------------------*/

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //页面改变时,动态设置底部页数
        mTvCount.setText("↓     " + (position + 1) + "/" + mImgList.size());
        //将页面选择状况存到sp里
        SpUtil.putInt(CartoonDetailActivity.this, "currentItem" + mLinkId, position);
        //将外面的书签所要设置的内容也存到sp里
        String bookmark = mLinkId.replace("/", "") + "book";
        SpUtil.putString(CartoonDetailActivity.this, bookmark, "已读至" + (position + 1) + "/" +
                mImgList.size());

        //创建消息对象,发送event指令给fragment,让它刷新适配器去更新书签进度
        MessageEvent event = new MessageEvent();
        event.msg = "refreshmark";
        EventBus.getDefault().post(event);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
