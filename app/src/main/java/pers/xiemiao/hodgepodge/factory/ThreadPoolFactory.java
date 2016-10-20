package pers.xiemiao.hodgepodge.factory;

/**
 * User: xiemiao
 * Date: 2016-10-06
 * Time: 14:07
 * Desc: 线程池工厂，获取正常线程池、获取下载线程池
 */
public class ThreadPoolFactory {
    static ThreadPoolProxy mNormalThreadPool;
    static ThreadPoolProxy mDownloadThreadPool;

    /**
     * 获取正常的线程池
     */
    public static ThreadPoolProxy getNormalThreadPool() {
        //安全的单例模式
        if (mNormalThreadPool == null) {
            synchronized (ThreadPoolProxy.class) {
                if (mNormalThreadPool == null) {
                    mNormalThreadPool = new ThreadPoolProxy(10, 10, 3000);
                }
            }
        }
        return mNormalThreadPool;
    }

    /**
     * 获取下载的线程池
     */
    public static ThreadPoolProxy getDownloadThreadPool() {
        //安全的单例模式
        if (mDownloadThreadPool == null) {
            synchronized (ThreadPoolProxy.class) {
                if (mDownloadThreadPool == null) {
                    mDownloadThreadPool = new ThreadPoolProxy(3, 3, 3000);
                }
            }
        }
        return mDownloadThreadPool;
    }
}
