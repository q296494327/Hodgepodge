package pers.xiemiao.hodgepodge.fragment.beautyfragment;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableRecyclerView;

import java.util.List;

import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.adapter.BeautyRecycleAdapter;
import pers.xiemiao.hodgepodge.base.BaseBeautyFragment;
import pers.xiemiao.hodgepodge.base.LoaddingPager;
import pers.xiemiao.hodgepodge.bean.GirlCategoryBean;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.protocol.GirlCategoryProtocol;
import pers.xiemiao.hodgepodge.utils.LogUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;

/**
 * User: xiemiao
 * Date: 2016-10-17
 * Time: 16:00
 * Desc: 性感美女
 */
public class XingGanFragment extends BaseBeautyFragment implements PullToRefreshLayout
        .OnPullListener {

    private GirlCategoryProtocol mProtocol;
    private List<GirlCategoryBean.ShowapiResBodyEntity.PagebeanEntity.GirlCategoryData> mDatas;
    private PullToRefreshLayout mRefreshLayout;
    private PullableRecyclerView mRecycleView;
    private RefreshTask mRefreshTask;
    private BeautyRecycleAdapter mBeautyRecycleAdapter;

    @Override//这里在子线程,所以可以直接请求网络
    public LoaddingPager.LoadResult initData() {
        try {
            mProtocol = new GirlCategoryProtocol();
            GirlCategoryBean girlCategoryBean = mProtocol.loadData("xinggan");
            mDatas = girlCategoryBean.showapi_res_body.pagebean.contentlist;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return LoaddingPager.LoadResult.LOADSUCESS;
    }

    @Override
    public View initSuccessView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.recycleview_qunzhuang, null);
        //获取到recycleview外层的刷新容器布局
        mRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
        //设置下拉上拉监听
        mRefreshLayout.setOnPullListener(this);
        //取得刷新容器布局里套着的recycleview
        mRecycleView = (PullableRecyclerView) mRefreshLayout.getPullableView();
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
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        //下拉刷新的时候将网络协议里的isRefresh改为true
        //开启线程池，进行下拉刷新
        mRefreshTask = new RefreshTask();
        mProtocol.isRefresh = true;
        ThreadPoolFactory.getNormalThreadPool().execute(mRefreshTask);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }

    class RefreshTask implements Runnable {
        @Override
        public void run() {
            //请求网络，请求最新的数据
            try {
                final List<GirlCategoryBean.ShowapiResBodyEntity.PagebeanEntity.GirlCategoryData>
                        girlCategoryDatas = mProtocol.loadData("xinggan").showapi_res_body
                        .pagebean
                        .contentlist;
                LogUtils.sf("下拉刷新中");
                mDatas.clear();//清空集合所有数据
                mDatas.addAll(0, girlCategoryDatas);//重新添加
                //然后将集合原来的数据都清空,再将数据添加给集合，在UI线程去刷新适配器
                UIUtils.postSafeTask(new Runnable() {
                    @Override
                    public void run() {
                        mBeautyRecycleAdapter.notifyDataSetChanged();
                        mRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                UIUtils.postSafeTask(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    }
                });
            }
        }
    }

}
