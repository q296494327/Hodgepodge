package pers.xiemiao.hodgepodge.factory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * User: xiemiao
 * Date: 2016-10-06
 * Time: 13:40
 * Desc: 创建线程池、提交任务、执行任务
 */
public class ThreadPoolProxy {
    ThreadPoolExecutor mExecutor;

    //这些参数可以通过构造函数去赋值
    private int mCorePoolSize;//核心线程数
    private int mMaximumPoolSize;//最大线程数
    private long mKeepAliveTime;//保持时间

    public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
        mCorePoolSize = corePoolSize;
        mMaximumPoolSize = maximumPoolSize;
        mKeepAliveTime = keepAliveTime;
    }

    /**
     * 初始化线程池执行者
     */
    public ThreadPoolExecutor initThreadPoolExecutor() {
        //安全单例模式
        if (mExecutor == null) {
            synchronized (ThreadPoolProxy.class) {
                if (mExecutor == null) {
                    //这些参数可以写死
                    TimeUnit unit = TimeUnit.SECONDS;//时间单位类型
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

                    mExecutor = new ThreadPoolExecutor(mCorePoolSize, mMaximumPoolSize,
                            mKeepAliveTime, unit, workQueue, threadFactory, handler);
                }
            }
        }
        return mExecutor;
    }

    /**
     * 执行任务
     */
    public void execute(Runnable task) {
        initThreadPoolExecutor();
        mExecutor.execute(task);
    }

    /**
     * 提交任务（区别就是可以返回异常信息）
     */
    public Future<?> submit(Runnable task) {
        initThreadPoolExecutor();
        return mExecutor.submit(task);
    }

    /**
     * 移除任务
     */
    public void removeTask(Runnable task) {
        initThreadPoolExecutor();
        mExecutor.remove(task);
    }
}
