package pers.xiemiao.hodgepodge.fragment.jokefragment;

import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.markmao.pulltorefresh.widget.XListView;

import java.util.List;

import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.adapter.RandomJokeAdapter;
import pers.xiemiao.hodgepodge.base.BaseFragment;
import pers.xiemiao.hodgepodge.base.LoaddingPager;
import pers.xiemiao.hodgepodge.bean.RandomJokeBean;
import pers.xiemiao.hodgepodge.factory.ListViewFactory;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.protocol.RandomJokeProtocol;
import pers.xiemiao.hodgepodge.utils.DensityUtils;
import pers.xiemiao.hodgepodge.utils.LogUtils;
import pers.xiemiao.hodgepodge.utils.SpUtil;
import pers.xiemiao.hodgepodge.utils.TimeUtils;
import pers.xiemiao.hodgepodge.utils.ToastUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;

/**
 * User: xiemiao
 * Date: 2016-10-10
 * Time: 23:04
 * Desc: 笑话选项卡---随机趣图fragment
 */
public class RandomJokeFragment extends BaseFragment implements XListView.IXListViewListener,
        View.OnClickListener {

    private RandomJokeProtocol mProtocol;
    private List<RandomJokeBean.RandomJokeData> mDatas;
    private RefreshTask mRefreshTask;
    private RandomJokeAdapter mRandomJokeAdapter;
    private XListView mXListView;
    private Button mBtnAudio;
    private PopupWindow mPopupWindow;
    private Button mBtnXiaoxin;
    private Button mBtnXiaoya;
    private Button mBtnXiaoyu;
    private Button mBtnXiaoyue;
    private Button mBtnXiaosi;

    @Override
    public LoaddingPager.LoadResult initData() {
        try {
            mProtocol = new RandomJokeProtocol();
            mDatas = mProtocol.loadData(0).result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkState(mDatas);
    }

    @Override
    public View initSuccessView() {
        mXListView = ListViewFactory.createXListView();
        mXListView.setXListViewListener(this);//设置刷新监听
        mXListView.setPullLoadEnable(false);//设置不能加载更多
        mXListView.setRefreshTime(TimeUtils.getCurrentTimeInString());
        //添加一个设置笑话语音的按钮
        View audioView = View.inflate(UIUtils.getContext(), R.layout.head_audio_setting, null);
        mBtnAudio = (Button) audioView.findViewById(R.id.btn_audio_setting);//语音设置按钮
        mBtnAudio.setOnClickListener(this);//点击监听
        mLoaddingPager.addView(audioView);//添加到父容器(即和listview一个爹)
        //将listview的参数设置margin值,
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = DensityUtils.dp2px(UIUtils.getContext(), 60);
        mXListView.setLayoutParams(params);
        //设置适配器
        mRandomJokeAdapter = new RandomJokeAdapter(mDatas);
        mXListView.setAdapter(mRandomJokeAdapter);
        return mXListView;
    }

    /*================父类的按钮点击事件=================*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_audio_setting:
                showPopupWindow(v);//弹出选择语音类型的窗口
                break;
            case R.id.btn_xiaoxin:
                //将语音类型改为对应的值
                SpUtil.putString(UIUtils.getContext(), "speaker", "vixx");
                ToastUtils.showToast(UIUtils.getContext(), "设置成功,我是蜡笔小新", Gravity.TOP);
                mPopupWindow.dismiss();
                break;
            case R.id.btn_xiaoya:
                //将语音类型改为对应的值
                SpUtil.putString(UIUtils.getContext(), "speaker", "xiaoyan");
                ToastUtils.showToast(UIUtils.getContext(), "设置成功,我是水笔小雅", Gravity.TOP);
                mPopupWindow.dismiss();
                break;
            case R.id.btn_xiaoyu:
                //将语音类型改为对应的值
                SpUtil.putString(UIUtils.getContext(), "speaker", "xiaoyu");
                ToastUtils.showToast(UIUtils.getContext(), "设置成功,我是铅笔小宇", Gravity.TOP);
                mPopupWindow.dismiss();
                break;
            case R.id.btn_xiaoyue:
                //将语音类型改为对应的值
                SpUtil.putString(UIUtils.getContext(), "speaker", "vixm");
                ToastUtils.showToast(UIUtils.getContext(), "设置成功,我是彩笔小粤", Gravity.TOP);
                mPopupWindow.dismiss();
                break;
            case R.id.btn_xiaosi:
                //将语音类型改为对应的值
                SpUtil.putString(UIUtils.getContext(), "speaker", "xiaorong");
                ToastUtils.showToast(UIUtils.getContext(), "设置成功,我是钢笔小四", Gravity.TOP);
                mPopupWindow.dismiss();
                break;
        }
    }

    /**
     * 展示popupWindows
     */
    private void showPopupWindow(View v) {
        mPopupWindow = new PopupWindow(UIUtils.getContext());
        View view = View.inflate(UIUtils.getContext(), R.layout.popup_audio_setting, null);
        //找到popupWindows布局上的孩子,设置点击事件
        mBtnXiaoxin = (Button) view.findViewById(R.id.btn_xiaoxin);
        mBtnXiaoya = (Button) view.findViewById(R.id.btn_xiaoya);
        mBtnXiaoyu = (Button) view.findViewById(R.id.btn_xiaoyu);
        mBtnXiaoyue = (Button) view.findViewById(R.id.btn_xiaoyue);
        mBtnXiaosi = (Button) view.findViewById(R.id.btn_xiaosi);
        mBtnXiaoxin.setOnClickListener(this);
        mBtnXiaoya.setOnClickListener(this);
        mBtnXiaoyu.setOnClickListener(this);
        mBtnXiaoyue.setOnClickListener(this);
        mBtnXiaosi.setOnClickListener(this);
        mPopupWindow.setContentView(view);
        //设置动画样式
        mPopupWindow.setAnimationStyle(R.style.style_popupwindow_audio);
        //一定要设置宽高,不然无法显示
        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置外部可点击消失
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAsDropDown(v, 0, 0);
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

    @Override
    public void onLoadMore() {
        //没有下拉
    }

    class RefreshTask implements Runnable {
        @Override
        public void run() {
            //请求网络，请求最新的数据
            try {
                final List<RandomJokeBean.RandomJokeData> jokeDateList = mProtocol
                        .loadData(0).result;
                LogUtils.sf("随机笑话下拉刷新中");
                mDatas.clear();//清空集合所有数据
                mDatas.addAll(0, jokeDateList);//重新添加
                //然后将集合原来的数据都清空,再将数据添加给集合，在UI线程去刷新适配器
                UIUtils.postSafeTask(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(UIUtils.getContext(), "拉我刷新下一批", Gravity.TOP);
                        mRandomJokeAdapter.notifyDataSetChanged();
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

}
