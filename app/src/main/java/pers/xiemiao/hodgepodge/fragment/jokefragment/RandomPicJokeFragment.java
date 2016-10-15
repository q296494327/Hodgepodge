package pers.xiemiao.hodgepodge.fragment.jokefragment;

import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;

import com.markmao.pulltorefresh.widget.XListView;

import java.util.List;

import pers.xiemiao.hodgepodge.adapter.RandomPicAdapter;
import pers.xiemiao.hodgepodge.base.BaseJokeFragment;
import pers.xiemiao.hodgepodge.base.LoaddingPager;
import pers.xiemiao.hodgepodge.bean.RandomPicBean;
import pers.xiemiao.hodgepodge.factory.ListViewFactory;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.protocol.RandomPicJokeProtocol;
import pers.xiemiao.hodgepodge.utils.LogUtils;
import pers.xiemiao.hodgepodge.utils.TimeUtils;
import pers.xiemiao.hodgepodge.utils.ToastUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;

/**
 * User: xiemiao
 * Date: 2016-10-10
 * Time: 23:04
 * Desc: 笑话选项卡---随机趣图fragment
 */
public class RandomPicJokeFragment extends BaseJokeFragment implements XListView.IXListViewListener {

    private RandomPicJokeProtocol mProtocol;
    private List<RandomPicBean.RandomPicData> mDatas;
    private RefreshTask mRefreshTask;
    private RandomPicAdapter mRandomPicAdapter;
    private XListView mXListView;

    @Override
    public LoaddingPager.LoadResult initData() {
        try {
            mProtocol = new RandomPicJokeProtocol();
            RandomPicBean randomPicBean = mProtocol.loadData(0);
            mDatas = randomPicBean.result;
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
        //设置适配器
        mRandomPicAdapter = new RandomPicAdapter(mDatas);
        mXListView.setAdapter(mRandomPicAdapter);
        return mXListView;
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
                final List<RandomPicBean.RandomPicData> randomPicDatas = mProtocol
                        .loadData(0).result;
                LogUtils.sf("下拉刷新中");
                mDatas.clear();//清空集合所有数据
                mDatas.addAll(0, randomPicDatas);//重新添加
                //然后将集合原来的数据都清空,再将数据添加给集合，在UI线程去刷新适配器
                UIUtils.postSafeTask(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(UIUtils.getContext(), "随机更新10篇", Gravity.TOP);
                        mRandomPicAdapter.notifyDataSetChanged();
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
}
