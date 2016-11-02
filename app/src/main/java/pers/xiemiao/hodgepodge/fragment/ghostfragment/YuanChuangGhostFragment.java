package pers.xiemiao.hodgepodge.fragment.ghostfragment;

import android.content.Intent;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.markmao.pulltorefresh.widget.XListView;

import java.util.List;
import java.util.Random;

import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.activity.GhostDetailActivity;
import pers.xiemiao.hodgepodge.adapter.GhostCategoryAdapter;
import pers.xiemiao.hodgepodge.base.BaseGhostFragment;
import pers.xiemiao.hodgepodge.bean.GhostCategoryBean;
import pers.xiemiao.hodgepodge.factory.ListViewFactory;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.protocol.GhostCategoryProtocol;
import pers.xiemiao.hodgepodge.utils.LogUtils;
import pers.xiemiao.hodgepodge.utils.SpUtil;
import pers.xiemiao.hodgepodge.utils.TimeUtils;
import pers.xiemiao.hodgepodge.utils.ToastUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;
import pers.xiemiao.hodgepodge.views.LoaddingPager;

/**
 * User: xiemiao
 * Date: 2016-10-13
 * Time: 23:43
 * Desc: 短篇鬼故事fragment
 */
public class YuanChuangGhostFragment extends BaseGhostFragment implements XListView
        .IXListViewListener,
        AdapterView.OnItemClickListener {

    private GhostCategoryProtocol mProtocol;
    private List<GhostCategoryBean.ShowapiResBodyEntity.PagebeanEntity.GhostCategoryData> mDatas;
    private XListView mXListView;
    private GhostCategoryAdapter mGhostCategoryAdapter;
    private RefreshTask mRefreshTask;
    private GhostCategoryBean mGhostCategoryBean;
    private final String NAMEKEY = getClass().getSimpleName();

    @Override
    public LoaddingPager.LoadResult initData() {
        try {
            int page = SpUtil.getInt(UIUtils.getContext(), NAMEKEY + "page", 1);
            mProtocol = new GhostCategoryProtocol();
            mGhostCategoryBean = mProtocol.loadData("yc", page);
            mDatas = mGhostCategoryBean.showapi_res_body.pagebean.contentlist;
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
        mGhostCategoryAdapter = new GhostCategoryAdapter(mDatas);
        mXListView.setAdapter(mGhostCategoryAdapter);
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
                //刷新随机来一页数据
                Random random = new Random();
                int page = 1 + random.nextInt(Integer.parseInt(mGhostCategoryBean.showapi_res_body
                        .pagebean.allPages) + 1);
                //将随机数存到sp里，保存下次进来时的位置
                SpUtil.putInt(UIUtils.getContext(), NAMEKEY + "page", page);
                final List<GhostCategoryBean.ShowapiResBodyEntity.PagebeanEntity
                        .GhostCategoryData> newsDataList = mProtocol
                        .loadData("yc", page).showapi_res_body.pagebean.contentlist;
                LogUtils.sf("下拉刷新中");
                mDatas.clear();//清空集合所有数据
                mDatas.addAll(0, newsDataList);//重新添加
                //然后将集合原来的数据都清空,再将数据添加给集合，在UI线程去刷新适配器
                UIUtils.postSafeTask(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(UIUtils.getContext(), "随机更新10篇鬼故事", Gravity.TOP);
                        mGhostCategoryAdapter.notifyDataSetChanged();
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
        GhostCategoryBean.ShowapiResBodyEntity.PagebeanEntity.GhostCategoryData ghostCategoryData
                = mDatas.get(position);
        //点击记录阅读状态，以id为依据
        //1先从sp里取出readState，判断所点击的item的url是否被包含在readState
        String readState = SpUtil.getString(UIUtils.getContext(), "readState", "");
        String gId = ghostCategoryData.id.replace(":", "").replace("/", "").replace("?", "");
        if (!readState.contains(gId)) {
            //2不包含就拼接进去
            readState += gId + ",";
            SpUtil.putString(UIUtils.getContext(), "readState", readState);
        }
        //3然后将文本改为灰色
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_desc = (TextView) view.findViewById(R.id.tv_desc);
        tv_title.setTextColor(0XFF787171);
        tv_desc.setTextColor(0XFF787171);

        /*-------------------跳转到鬼故事详情页---------------------*/
        Intent intent = new Intent(UIUtils.getContext(), GhostDetailActivity.class);
        intent.putExtra("id", ghostCategoryData.id);//将id地址携带过去
        intent.putExtra("title", ghostCategoryData.title);//将title地址携带过去
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        UIUtils.getContext().startActivity(intent);
    }
}
