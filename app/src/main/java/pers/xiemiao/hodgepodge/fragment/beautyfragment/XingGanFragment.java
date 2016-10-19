package pers.xiemiao.hodgepodge.fragment.beautyfragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;

import java.util.List;

import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.adapter.BeautyRecycleAdapter;
import pers.xiemiao.hodgepodge.base.BaseBeautyFragment;
import pers.xiemiao.hodgepodge.base.LoaddingPager;
import pers.xiemiao.hodgepodge.bean.GirlCategoryBean;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.protocol.GirlCategoryProtocol;
import pers.xiemiao.hodgepodge.utils.LogUtils;
import pers.xiemiao.hodgepodge.utils.ToastUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;

/**
 * User: xiemiao
 * Date: 2016-10-17
 * Time: 16:00
 * Desc: 性感美女
 */
public class XingGanFragment extends BaseBeautyFragment implements SwipeRefreshLayout
        .OnRefreshListener {

    private GirlCategoryProtocol mProtocol;
    private List<GirlCategoryBean.ShowapiResBodyEntity.PagebeanEntity.GirlCategoryData> mDatas;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecycleView;
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
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_view);
        mRecycleView = (RecyclerView) view.findViewById(R.id.recycle_view);
        //设置刷新的颜色替换数据
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R
                .color.holo_red_light, android.R.color.holo_orange_light, android.R.color
                .holo_green_light);
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
                        mRefreshLayout.setRefreshing(false);
                        ToastUtils.showSafeToast("刷新成功", Gravity.TOP);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                UIUtils.postSafeTask(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                        ToastUtils.showSafeToast("网络异常,请重试", Gravity.TOP);
                    }
                });
            }
        }
    }

}
