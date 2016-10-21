package pers.xiemiao.hodgepodge.fragment.cartoonfragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.markmao.pulltorefresh.widget.XListView;

import java.util.List;
import java.util.Random;

import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.activity.NeiHanDetailActivity;
import pers.xiemiao.hodgepodge.adapter.NeiHanCategoryAdapter;
import pers.xiemiao.hodgepodge.base.BaseCartoonFragment;
import pers.xiemiao.hodgepodge.base.LoaddingPager;
import pers.xiemiao.hodgepodge.bean.NeiHanCategoryBean;
import pers.xiemiao.hodgepodge.factory.ListViewFactory;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.protocol.NeiHanCategoryProtocol;
import pers.xiemiao.hodgepodge.utils.LogUtils;
import pers.xiemiao.hodgepodge.utils.SpUtil;
import pers.xiemiao.hodgepodge.utils.TimeUtils;
import pers.xiemiao.hodgepodge.utils.ToastUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;

/**
 * User: xiemiao
 * Date: 2016-10-18
 * Time: 18:38
 * Desc:内涵漫画fragment
 */
public class NeiHanFragment extends BaseCartoonFragment implements AdapterView
        .OnItemClickListener, XListView.IXListViewListener {

    private NeiHanCategoryProtocol mProtocol;
    private List<NeiHanCategoryBean.ShowapiResBodyEntity.PagebeanEntity.NeiHanCategoryData>
            mDatas;
    private XListView mXListView;
    private NeiHanCategoryAdapter mNeiHanCategoryAdapter;
    private RefreshTask mRefreshTask;
    private LoadMoreTask mLoadMoreTask;
    private String mAllPages;

    @Override
    public LoaddingPager.LoadResult initData() {
        try {
            mProtocol = new NeiHanCategoryProtocol();
            NeiHanCategoryBean neiHanCategoryBean = mProtocol.loadData(1);
            mAllPages = neiHanCategoryBean.showapi_res_body.pagebean.allPages;//获取所有页数
            mDatas = neiHanCategoryBean.showapi_res_body.pagebean.contentlist;
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
        mXListView.setDividerHeight(0);
        //设置适配器
        mNeiHanCategoryAdapter = new NeiHanCategoryAdapter(mDatas);
        mXListView.setAdapter(mNeiHanCategoryAdapter);
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
                //下拉随机加载
                Random random = new Random();
                final List<NeiHanCategoryBean.ShowapiResBodyEntity.PagebeanEntity
                        .NeiHanCategoryData> neiHanCategoryDataList = mProtocol.loadData
                        (random.nextInt(Integer.parseInt(mAllPages))).showapi_res_body.pagebean
                        .contentlist;
                LogUtils.sf("下拉刷新中");
                mDatas.clear();//清空集合所有数据
                mDatas.addAll(0, neiHanCategoryDataList);//重新添加
                //然后将集合原来的数据都清空,再将数据添加给集合，在UI线程去刷新适配器
                UIUtils.postSafeTask(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(UIUtils.getContext(), "随机刷新50篇", Gravity.TOP);
                        mNeiHanCategoryAdapter.notifyDataSetChanged();
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
    @Override
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
                final List<NeiHanCategoryBean.ShowapiResBodyEntity.PagebeanEntity
                        .NeiHanCategoryData> neiHanCategoryDataList = mProtocol
                        .loadData(mDatas.size() / 50 + 1).showapi_res_body
                        .pagebean.contentlist;
                //然后将数据添加给集合，在UI线程去刷新适配器
                UIUtils.postSafeTask(new Runnable() {
                    @Override
                    public void run() {
                        if (neiHanCategoryDataList.size() > 0) {
                            mDatas.addAll(neiHanCategoryDataList);
                            mNeiHanCategoryAdapter.notifyDataSetChanged();
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


    /*-------------------listview点击事件处理---------------------*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //因为有头布局，所以这里的position要减去头布局的位置
        position = position - mXListView.getHeaderViewsCount();
        NeiHanCategoryBean.ShowapiResBodyEntity.PagebeanEntity.NeiHanCategoryData
                neiHanCategoryData = mDatas.get(position);
        //点击记录阅读状态，以id为依据
        //1先从sp里取出cartoonState，判断所点击的item的id是否被包含在cartoonState
        String cartoonState = SpUtil.getString(UIUtils.getContext(), "cartoonState", "");
        String cartoonId = neiHanCategoryData.id.replace(":", "").replace("/", "");
        if (!cartoonState.contains(cartoonId)) {
            //2不包含就拼接进去
            cartoonState += cartoonId + ",";
            SpUtil.putString(UIUtils.getContext(), "cartoonState", cartoonState);
        }
        //3然后将文本改为灰色.卡片改为白色
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);
        tv_title.setTextColor(0XFF787171);
        cardView.setCardBackgroundColor(Color.WHITE);

                /*-------------------跳转到漫画详情页---------------------*/
        Intent intent = new Intent(UIUtils.getContext(), NeiHanDetailActivity.class);
        intent.putExtra("linkid", neiHanCategoryData.id);//将链接地址携带过去
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

}
