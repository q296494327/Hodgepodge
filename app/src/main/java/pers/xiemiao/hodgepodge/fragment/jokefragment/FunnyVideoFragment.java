package pers.xiemiao.hodgepodge.fragment.jokefragment;

import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.baidu.mobad.feeds.BaiduNative;
import com.baidu.mobad.feeds.NativeErrorCode;
import com.baidu.mobad.feeds.NativeResponse;
import com.baidu.mobad.feeds.RequestParameters;
import com.markmao.pulltorefresh.widget.XListView;

import java.util.ArrayList;
import java.util.List;

import pers.xiemiao.hodgepodge.adapter.FunnyVideoAdapter;
import pers.xiemiao.hodgepodge.base.BaseJokeFragment;
import pers.xiemiao.hodgepodge.bean.FunnyVideoBean;
import pers.xiemiao.hodgepodge.bean.NormalAndAdBean;
import pers.xiemiao.hodgepodge.conf.Constants;
import pers.xiemiao.hodgepodge.factory.ListViewFactory;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.protocol.FunnyVideoProtocol;
import pers.xiemiao.hodgepodge.utils.LogUtils;
import pers.xiemiao.hodgepodge.utils.TimeUtils;
import pers.xiemiao.hodgepodge.utils.ToastUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;
import pers.xiemiao.hodgepodge.views.LoaddingPager;

/**
 * User: xiemiao
 * Date: 2016-10-10
 * Time: 20:55
 * Desc: 笑话选项卡---搞笑视频
 */
public class FunnyVideoFragment extends BaseJokeFragment implements XListView.IXListViewListener {

    private List<FunnyVideoBean.ShowapiResBodyEntity.PagebeanEntity.FunnyVideoData> mDatas;
    private FunnyVideoProtocol mProtocol;
    private LoadMoreTask mLoadMoreTask;
    private RefreshTask mRefreshTask;
    private FunnyVideoAdapter mFunnyVideoAdapter;
    private XListView mXListView;

    /*-------------------百度广告----begin-----------------*/
    private List<NormalAndAdBean> mVideoAndAdList = new ArrayList<NormalAndAdBean>();
    List<NativeResponse> nrAdList = new ArrayList<NativeResponse>();
    private static String YOUR_AD_PLACE_ID = Constants.LISTVIEW_PLACE_ID; // 双引号中填写自己的广告位ID
    /*-------------------百度广告----end-----------------*/

    @Override//初始化数据在子线程,所以可以直接请求网络
    public LoaddingPager.LoadResult initData() {
        try {
            mProtocol = new FunnyVideoProtocol();
            FunnyVideoBean funnyVideoBean = mProtocol.loadData(1);
            mDatas = funnyVideoBean.showapi_res_body.pagebean.contentlist;
            //将集合的数据添加到总集合
            for (FunnyVideoBean.ShowapiResBodyEntity.PagebeanEntity.FunnyVideoData data : mDatas) {
                NormalAndAdBean bean = new NormalAndAdBean();
                bean.vData = data;
                bean.isAd = false;
                mVideoAndAdList.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkState(mVideoAndAdList);
    }

    /*-------------------百度广告----begin-----------------*/
    public void fetchAd(Activity activity) {
        /**
         * Step 1. 创建BaiduNative对象，参数分别为： 上下文context，广告位ID,
         * BaiduNativeNetworkListener监听（监听广告请求的成功与失败）
         * 注意：请将YOUR_AD_PALCE_ID替换为自己的广告位ID
         */
        BaiduNative baidu = new BaiduNative(activity, YOUR_AD_PLACE_ID, new
                BaiduNative.BaiduNativeNetworkListener() {
                    @Override
                    public void onNativeFail(NativeErrorCode arg0) {
                        Log.w("ListViewActivity", "onNativeFail reason:" + arg0.name());
                    }

                    @Override
                    public void onNativeLoad(List<NativeResponse> arg0) {
                        // 一个广告只允许展现一次，多次展现、点击只会计入一次
                        if (arg0 != null && arg0.size() > 0) {
                            nrAdList = arg0;
                            for (NativeResponse ad : nrAdList) {
                                NormalAndAdBean bean = new NormalAndAdBean();
                                bean.ad = ad;
                                bean.isAd = true;
                                mVideoAndAdList.add(bean);
                            }
                        }
                    }

                });

        /**
         * Step 2. 创建requestParameters对象，并将其传给baidu.makeRequest来请求广告
         */
        // 用户点击下载类广告时，是否弹出提示框让用户选择下载与否
        RequestParameters requestParameters =
                new RequestParameters.Builder()
                        .downloadAppConfirmPolicy(
                                RequestParameters.DOWNLOAD_APP_CONFIRM_ONLY_MOBILE).build();

        baidu.makeRequest(requestParameters);
    }

    /*-------------------百度广告----end-----------------*/


    @Override
    public View initSuccessView() {
        mXListView = ListViewFactory.createXListView();
        mXListView.setXListViewListener(this);//设置刷新监听
        mXListView.setRefreshTime(TimeUtils.getCurrentTimeInString());
        mXListView.setFastScrollEnabled(false);
        fetchAd(getActivity());//初始化百度广告
        mFunnyVideoAdapter = new FunnyVideoAdapter(getActivity(), mVideoAndAdList);
        mXListView.setAdapter(mFunnyVideoAdapter);
        return mXListView;
    }

    @Override
    public String getTitle() {
        return "搞笑视频";
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
                final List<FunnyVideoBean.ShowapiResBodyEntity.PagebeanEntity.FunnyVideoData>
                        funnyVideoDataList = mProtocol
                        .loadData(1).showapi_res_body.pagebean.contentlist;
                LogUtils.sf("下拉刷新中");
                mVideoAndAdList.clear();//清空集合所有数据
                for (FunnyVideoBean.ShowapiResBodyEntity.PagebeanEntity.FunnyVideoData data :
                        funnyVideoDataList) {
                    NormalAndAdBean bean = new NormalAndAdBean();
                    bean.vData = data;
                    bean.isAd = false;
                    mVideoAndAdList.add(bean);
                }
                fetchAd(getActivity());//重新初始化百度广告
                //然后将集合原来的数据都清空,再将数据添加给集合，在UI线程去刷新适配器
                UIUtils.postSafeTask(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(UIUtils.getContext(), "数据已是最新", Gravity.TOP);
                        mFunnyVideoAdapter.notifyDataSetChanged();
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
                final List<FunnyVideoBean.ShowapiResBodyEntity.PagebeanEntity.FunnyVideoData>
                        funnyVideoDataList = mProtocol
                        .loadData(mVideoAndAdList.size() / (20 + nrAdList.size()) + 1)
                        .showapi_res_body.pagebean.contentlist;
                //然后将数据添加给集合，在UI线程去刷新适配器
                UIUtils.postSafeTask(new Runnable() {
                    @Override
                    public void run() {
                        if (funnyVideoDataList.size() > 0) {
                            for (FunnyVideoBean.ShowapiResBodyEntity.PagebeanEntity
                                    .FunnyVideoData data :
                                    funnyVideoDataList) {
                                NormalAndAdBean bean = new NormalAndAdBean();
                                bean.vData = data;
                                bean.isAd = false;
                                mVideoAndAdList.add(bean);
                            }
                            fetchAd(getActivity());//重新初始化百度广告
                            mFunnyVideoAdapter.notifyDataSetChanged();
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
