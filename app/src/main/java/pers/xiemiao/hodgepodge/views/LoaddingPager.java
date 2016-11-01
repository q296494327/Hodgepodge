package pers.xiemiao.hodgepodge.views;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.utils.LogUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;


/**
 * User: xiemiao
 * Date: 2016-10-05
 * Time: 18:08
 * Desc: 使用LoaddingPager类去控制fragment的共性内容
 * MVC模式 M:数据加载逻辑 V：布局展示逻辑 C：LoaddingPager作为控制器
 */
public abstract class LoaddingPager extends FrameLayout {

    //初始化几种状态常量
    private static final int STATE_NONE = -1;//未加载状态
    private static final int STATE_LOADDING = 0;
    private static final int STATE_EMPTY = 1;
    private static final int STATE_ERROR = 2;
    private static final int STATE_SUCCESS = 3;

    private int mCurState = STATE_NONE;//初始化当前状态为未加载状态
    private View mPagerLoading;
    private View mPagerEmpty;
    private View mPagerError;
    private View mPagerSuccess;

    public LoaddingPager(Context context) {
        super(context);
        //在构造方法时就初始化常规的几种页面展示
        //1加载中、2加载数据为空、3加载失败、4加载成功
        initCommonView();
    }

    /**
     * 初始化常规布局
     */
    private void initCommonView() {
        //加载中
        mPagerLoading = View.inflate(getContext(), R.layout.pager_loading, null);
        addView(mPagerLoading);
        //加载为空
        mPagerEmpty = View.inflate(getContext(), R.layout.pager_empty, null);
        addView(mPagerEmpty);
        //加载失败
        mPagerError = View.inflate(getContext(), R.layout.pager_error, null);
        addView(mPagerError);
        refreshUI();
    }

    /**
     * 根据当前状态去刷新UI页面展示
     */
    private void refreshUI() {
        //加载中的展示
        mPagerLoading.setVisibility((mCurState == STATE_LOADDING) || (mCurState == STATE_NONE) ?
                VISIBLE : GONE);
        //加载为空的展示
        mPagerEmpty.setVisibility((mCurState == STATE_EMPTY) ? VISIBLE : GONE);
        //加载失败的展示
        mPagerError.setVisibility((mCurState == STATE_ERROR) ? VISIBLE : GONE);
        mPagerError.findViewById(R.id.error_btn_retry).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        //在当前状态是加载成功时，初始化加载成功的布局，由子类去实现
        if (mCurState == STATE_SUCCESS && mPagerSuccess == null) {
            mPagerSuccess = initSuccessView();
            //将成功布局添加到容器
            if (mPagerSuccess != null) {
                addView(mPagerSuccess);
            }
        }

        //设置加载成功布局的显示或隐藏
        if (mPagerSuccess != null) {
            mPagerSuccess.setVisibility((mCurState == STATE_SUCCESS) ? VISIBLE : GONE);
        }
    }

    /**
     * 加载数据方法
     */
    public void loadData() {
        //如果加载成功之后，就不去加载数据了
        if (mCurState != STATE_SUCCESS && mCurState != STATE_LOADDING) {
            LogUtils.sf("开始加载啦");
            //每次加载之前都将当前状态置为加载中
            int state=STATE_LOADDING;
            mCurState = state;
            refreshUI();
            //加载数据是耗时操作，新建一个线程去进行
//            new Thread(new LoadDataTask()).start();
            //使用线程池
            ThreadPoolFactory.getNormalThreadPool().execute(new LoadDataTask());
        }
    }


    /**
     * 加载数据的异步任务
     */
    private class LoadDataTask implements Runnable {
        @Override
        public void run() {
            //1真正开始加载数据，由子线程必须去实现，并需要返回一个状态码，更新当前的状态
            LoadResult loadResult = initData();
            //2将当前状态更改为加载数据之后的状态
            mCurState = loadResult.getState();
            UIUtils.postSafeTask(new Runnable() {
                @Override
                public void run() {
                    //3刷新UI,在主线程刷新
                    refreshUI();
                }
            });
        }
    }

    /**
     * 初始化数据的抽象方法，由子类必须实现
     */
    public abstract LoadResult initData();

    /**
     * 初始化加载成功的布局的抽象方法，由子类必须实现
     */
    public abstract View initSuccessView();

    /**
     * 加载数据结果的枚举，为了使返回的加载结果只能是这3种状态
     */
    public enum LoadResult {
        LOADSUCESS(STATE_SUCCESS), LOADERROR(STATE_ERROR), LOADEMPTY(STATE_EMPTY);

        private int mState;

        public int getState() {
            return mState;
        }

        private LoadResult(int state) {
            mState = state;
        }
    }
}
