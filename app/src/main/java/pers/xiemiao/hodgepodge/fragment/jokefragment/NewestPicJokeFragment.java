package pers.xiemiao.hodgepodge.fragment.jokefragment;

import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;

import com.markmao.pulltorefresh.widget.XListView;

import java.util.List;

import pers.xiemiao.hodgepodge.adapter.NewestPicAdapter;
import pers.xiemiao.hodgepodge.base.BaseJokeFragment;
import pers.xiemiao.hodgepodge.views.LoaddingPager;
import pers.xiemiao.hodgepodge.bean.NewestPicBean;
import pers.xiemiao.hodgepodge.factory.ListViewFactory;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.protocol.NewestPicJokeProtocol;
import pers.xiemiao.hodgepodge.utils.LogUtils;
import pers.xiemiao.hodgepodge.utils.TimeUtils;
import pers.xiemiao.hodgepodge.utils.ToastUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;

/**
 * User: xiemiao
 * Date: 2016-10-10
 * Time: 23:04
 * Desc: 笑话选项卡---最新趣图fragment
 */
public class NewestPicJokeFragment extends BaseJokeFragment implements XListView.IXListViewListener {

    private NewestPicJokeProtocol mProtocol;
    private List<NewestPicBean.ResultEntity.NewestPicData> mDatas;
    private LoadMoreTask mLoadMoreTask;
    private RefreshTask mRefreshTask;
    private NewestPicAdapter mPicAdapter;
    private XListView mXListView;

    @Override
    public LoaddingPager.LoadResult initData() {
        try {
            mProtocol = new NewestPicJokeProtocol();
            NewestPicBean newestPicBean = mProtocol.loadData(1);
            mDatas = newestPicBean.result.data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkState(mDatas);
    }

    @Override
    public View initSuccessView() {
        mXListView = ListViewFactory.createXListView();
        mXListView.setXListViewListener(this);//设置刷新监听
        mXListView.setRefreshTime(TimeUtils.getCurrentTimeInString());
        //设置适配器
        mPicAdapter = new NewestPicAdapter(mDatas);
        mXListView.setAdapter(mPicAdapter);
        return mXListView;
    }

    @Override
    public String getTitle() {
        return "最新趣图";
    }

    /*================listview的下拉刷新监听=================*/

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
                final List<NewestPicBean.ResultEntity.NewestPicData> picDateList = mProtocol
                        .loadData(1).result.data;
                LogUtils.sf("下拉刷新中");
                mDatas.clear();//清空集合所有数据
                mDatas.addAll(0, picDateList);//重新添加
                //然后将集合原来的数据都清空,再将数据添加给集合，在UI线程去刷新适配器
                UIUtils.postSafeTask(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(UIUtils.getContext(), "数据已是最新", Gravity.TOP);
                        mPicAdapter.notifyDataSetChanged();
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


    /*================listview的上拉加载更多监听=================*/
    @Override//上拉加载更多
    public void onLoadMore() {
        //开启线程池，去加载更多数据
        if (mLoadMoreTask == null) {//判断任务是否为空，只有为空的时候才去加载更多
            mLoadMoreTask = new LoadMoreTask();
            ThreadPoolFactory.getNormalThreadPool().execute(mLoadMoreTask);
        }
    }

    /**
     * 加载更多的任务
     */
    class LoadMoreTask implements Runnable {
        @Override
        public void run() {
            //请求网络，加载下一页的数据
            try {
                final List<NewestPicBean.ResultEntity.NewestPicData> jokeDateList = mProtocol
                        .loadData(mDatas.size() / 20 + 1).result.data;
                //然后将数据添加给集合，在UI线程去刷新适配器
                UIUtils.postSafeTask(new Runnable() {
                    @Override
                    public void run() {
                        if (jokeDateList.size() > 0) {
                            mDatas.addAll(jokeDateList);
                            mPicAdapter.notifyDataSetChanged();
                        } else {
                            mXListView.stopLoadMore();//停止加载更多
                            ToastUtils.showToast(getContext(), "没有更多数据了");
                        }
                    }
                });
                //当一个任务执行完了之后，就将它置为空
                mLoadMoreTask = null;
            } catch (Exception e) {
                e.printStackTrace();
                SystemClock.sleep(2000);
                ToastUtils.showSafeToast(UIUtils.getContext(), "网络异常,并且缓存期限已到,请检查网络连接");
                UIUtils.postSafeTask(new Runnable() {
                    @Override
                    public void run() {
                        mXListView.stopLoadMore();
                    }
                });
            }
        }
    }
}
