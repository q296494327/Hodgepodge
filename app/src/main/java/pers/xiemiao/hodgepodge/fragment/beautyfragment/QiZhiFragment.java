package pers.xiemiao.hodgepodge.fragment.beautyfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;

import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.adapter.BeautyRecycleAdapter;
import pers.xiemiao.hodgepodge.base.BaseBeautyFragment;
import pers.xiemiao.hodgepodge.views.LoaddingPager;
import pers.xiemiao.hodgepodge.bean.BaiduBeautyBean;
import pers.xiemiao.hodgepodge.bean.MessageEvent;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.protocol.BaiduImageProtocol;
import pers.xiemiao.hodgepodge.utils.LogUtils;
import pers.xiemiao.hodgepodge.utils.ToastUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;

/**
 * User: xiemiao
 * Date: 2016-10-28
 * Time: 16:00
 * Desc: 校花美女
 */
public class QiZhiFragment extends BaseBeautyFragment implements SHSwipeRefreshLayout
        .SHSOnRefreshListener {

    private BaiduImageProtocol mProtocol;
    private List<BaiduBeautyBean.BaiduBeautyData> mDatas = new ArrayList<BaiduBeautyBean
            .BaiduBeautyData>();
    private SHSwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;
    private RefreshTask mRefreshTask;
    private BeautyRecycleAdapter mBeautyRecycleAdapter;
    private BaiduBeautyBean mBaiduBeautyBean;

    @Override//这里在子线程,所以可以直接请求网络
    public LoaddingPager.LoadResult initData() {
        try {
            mProtocol = new BaiduImageProtocol();
            mBaiduBeautyBean = mProtocol.loadData("美女", "气质", 0);
            List<BaiduBeautyBean.BaiduBeautyData> datas = mBaiduBeautyBean.data;
            for (int i = 0; i < datas.size(); i++) {
                if (i != datas.size() - 1) {
                    mDatas.add(datas.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkState(mDatas);
    }

    @Override
    public View initSuccessView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.recycleview_qunzhuang, null);
        //获取到recycleview外层的刷新容器布局
        mRefreshLayout = (SHSwipeRefreshLayout) view.findViewById(R.id.refresh_view);
        mRecycleView = (RecyclerView) view.findViewById(R.id.recycle_view);
        //设置刷新监听
        mRefreshLayout.setOnRefreshListener(this);
        //确定recycle的瀑布流布局管家,2列
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        //创建专门的recycle适配器
        mBeautyRecycleAdapter = new BeautyRecycleAdapter(mDatas);
        mRecycleView.setAdapter(mBeautyRecycleAdapter);//设置适配器
        return view;
    }


    /*-------------------recycle的刷新监听--begin-------------------*/
    @Override
    public void onRefresh() {
        //下拉刷新的时候将网络协议里的isRefresh改为true
        //开启线程池，进行下拉刷新
        mRefreshTask = new RefreshTask();
        mProtocol.isRefresh = true;
        ThreadPoolFactory.getNormalThreadPool().execute(mRefreshTask);
    }

    @Override
    public void onLoading() {
        mRefreshTask = new RefreshTask();
        mProtocol.isRefresh = true;
        ThreadPoolFactory.getNormalThreadPool().execute(mRefreshTask);
        mRefreshLayout.finishLoadmore();
        mRecycleView.scrollToPosition(0);
    }

    @Override
    public void onRefreshPulStateChange(float percent, int state) {

    }

    @Override
    public void onLoadmorePullStateChange(float percent, int state) {

    }

    class RefreshTask implements Runnable {
        @Override
        public void run() {
            //请求网络，请求最新的数据
            try {
                //随机取加载一页数据
                int totalNum = mBaiduBeautyBean.totalNum;
                int pageNum = totalNum / 20;
                Random random = new Random();
                int index = random.nextInt(pageNum + 1);
                final List<BaiduBeautyBean.BaiduBeautyData>
                        baiduBeautyDatas = mProtocol.loadData("美女", "气质", index * 20).data;
                LogUtils.sf("下拉刷新中");
                mDatas.clear();//清空集合所有数据
                for (int i = 0; i < baiduBeautyDatas.size(); i++) {
                    if (i != baiduBeautyDatas.size() - 1) {
                        mDatas.add(baiduBeautyDatas.get(i));
                    }
                }
                //然后将集合原来的数据都清空,再将数据添加给集合，在UI线程去刷新适配器
                UIUtils.postSafeTask(new Runnable() {
                    @Override
                    public void run() {
                        mBeautyRecycleAdapter.notifyDataSetChanged();
                        mRefreshLayout.finishRefresh();
                        ToastUtils.showSafeToast("一大波萌妹来袭", Gravity.TOP);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                UIUtils.postSafeTask(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.finishRefresh();
                        ToastUtils.showSafeToast("网络异常,请重试", Gravity.TOP);
                    }
                });
            }
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

    /*================接收EventBus传过来的消息,做出相应的操作==(已在基类注册了eventbus)===============*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        mRecycleView.scrollToPosition(event.position);
    }
}
