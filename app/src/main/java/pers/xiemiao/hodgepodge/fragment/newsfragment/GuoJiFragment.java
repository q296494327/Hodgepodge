package pers.xiemiao.hodgepodge.fragment.newsfragment;

import android.content.Intent;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.markmao.pulltorefresh.widget.XListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.activity.NewsDetailActivity;
import pers.xiemiao.hodgepodge.adapter.TopNewsAdapter;
import pers.xiemiao.hodgepodge.base.BaseNewsFragment;
import pers.xiemiao.hodgepodge.base.LoaddingPager;
import pers.xiemiao.hodgepodge.bean.NewsBean;
import pers.xiemiao.hodgepodge.factory.ListViewFactory;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.holder.newsHolder.TopNewsPicturesHolder;
import pers.xiemiao.hodgepodge.protocol.TopNewsProtocol;
import pers.xiemiao.hodgepodge.utils.LogUtils;
import pers.xiemiao.hodgepodge.utils.SpUtil;
import pers.xiemiao.hodgepodge.utils.TimeUtils;
import pers.xiemiao.hodgepodge.utils.ToastUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;

/**
 * User: xiemiao
 * Date: 2016-10-13
 * Time: 23:43
 * Desc: 头条新闻fragment
 */
public class GuoJiFragment extends BaseNewsFragment  implements XListView.IXListViewListener,
        AdapterView.OnItemClickListener {

    private TopNewsProtocol mProtocol;
    private List<NewsBean.ResultEntity.NewsData> mDatas;
    private XListView mXListView;
    private TopNewsAdapter mTopNewsAdapter;
    private RefreshTask mRefreshTask;

    @Override
    public LoaddingPager.LoadResult initData() {
        try {
            mProtocol = new TopNewsProtocol();
            NewsBean newsBean = mProtocol.loadData("guoji");
            mDatas = newsBean.result.data;
            Collections.sort(mDatas);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkState(mDatas);
    }

    @Override
    public View initSuccessView() {
        mXListView = ListViewFactory.createXListView();
        mXListView.setXListViewListener(this);//设置刷新监听
        mXListView.setAutoLoadEnable(false);
        mXListView.setRefreshTime(TimeUtils.getCurrentTimeInString());
        //添加一个头布局,用来展示头条轮播图
        TopNewsPicturesHolder picturesHolder = new TopNewsPicturesHolder();
        List<NewsBean.ResultEntity.NewsData> topNewsData = new ArrayList<NewsBean.ResultEntity
                .NewsData>();
        Random random = new Random();//随机取5个轮播图
        topNewsData.add(mDatas.get(random.nextInt(mDatas.size())));
        topNewsData.add(mDatas.get(random.nextInt(mDatas.size())));
        topNewsData.add(mDatas.get(random.nextInt(mDatas.size())));
        topNewsData.add(mDatas.get(random.nextInt(mDatas.size())));
        topNewsData.add(mDatas.get(random.nextInt(mDatas.size())));
        picturesHolder.setDataAndRefreshHolderView(topNewsData);//设置轮播展示的网络图片及链接数据
        mXListView.addHeaderView(picturesHolder.getHolderView());
        //设置适配器
        mTopNewsAdapter = new TopNewsAdapter(mDatas);
        mXListView.setAdapter(mTopNewsAdapter);
        mXListView.setOnItemClickListener(this);
        return mXListView;
    }

    /*================listview的下拉刷新监听=begin================*/

    @Override//下拉刷新
    public void onRefresh() {
        //下拉刷新的时候将网络协议里的isRefresh改为true
        //开启线程池，进行下拉刷新
        mRefreshTask = new RefreshTask();
        mProtocol.isRefresh = true;
        ThreadPoolFactory.getNormalThreadPool().execute(mRefreshTask);
    }

    class RefreshTask implements Runnable {
        @Override
        public void run() {
            //请求网络，请求最新的数据
            try {
                final List<NewsBean.ResultEntity.NewsData> newsDataList = mProtocol.loadData
                        ("guoji").result.data;
                LogUtils.sf("下拉刷新中");
                mDatas.clear();//清空集合所有数据
                mDatas.addAll(0, newsDataList);//重新添加
                Collections.sort(mDatas);//对集合进行排序
                //然后将集合原来的数据都清空,再将数据添加给集合，在UI线程去刷新适配器
                UIUtils.postSafeTask(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(UIUtils.getContext(), "数据已是最新", Gravity.TOP);
                        mTopNewsAdapter.notifyDataSetChanged();
                        mXListView.setRefreshTime(TimeUtils.getCurrentTimeInString());
                        mXListView.stopRefresh();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                SystemClock.sleep(2000);
                ToastUtils.showSafeToast("网络异常,请检查网络连接", Gravity.TOP);
                UIUtils.postSafeTask(new Runnable() {
                    @Override
                    public void run() {
                        mXListView.stopRefresh();
                    }
                });
            }
        }
    }

    @Override
    public void onLoadMore() {
        mRefreshTask = new RefreshTask();
        mProtocol.isRefresh = true;
        ThreadPoolFactory.getNormalThreadPool().execute(mRefreshTask);
        mXListView.setSelection(0);
        mXListView.stopLoadMore();
    }

    /*================listview的下拉刷新监听=end================*/


    /*-------------------listview点击事件处理---------------------*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //因为有头布局，所以这里的position要减去头布局的位置
        position = position - mXListView.getHeaderViewsCount();
        NewsBean.ResultEntity.NewsData newsData = mDatas.get(position);
        //点击记录阅读状态，以url为依据
        //1先从sp里取出readState，判断所点击的item的url是否被包含在readState
        String readState = SpUtil.getString(UIUtils.getContext(), "readState", "");
        String url = newsData.url.replace(":", "").replace("/", "").replace("?", "");
        if (!readState.contains(url)) {
            //2不包含就拼接进去
            readState += url + ",";
            SpUtil.putString(UIUtils.getContext(), "readState", readState);
        }
        //3然后将文本改为灰色
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_date = (TextView) view.findViewById(R.id.tv_date);
        TextView tv_author = (TextView) view.findViewById(R.id.tv_author);
        TextView tv_timeago = (TextView) view.findViewById(R.id.tv_timeago);
        tv_title.setTextColor(0XFF787171);
        tv_date.setTextColor(0XFF787171);
        tv_author.setTextColor(0XFF787171);
        tv_timeago.setTextColor(0XFF787171);

        /*-------------------跳转到新闻详情页---------------------*/
        Intent intent = new Intent(UIUtils.getContext(), NewsDetailActivity.class);
        intent.putExtra("url", newsData.url);//将链接地址携带过去
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        UIUtils.getContext().startActivity(intent);
    }
}
