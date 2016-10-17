package pers.xiemiao.hodgepodge.base;

import android.view.View;

/**
 * User: xiemiao
 * Date: 2016-10-06
 * Time: 15:38
 * Desc: 基类holder，抽取适配器getView里的共性，分为两类去抽取
 * 1界面的展示
 * 2界面数据的刷新
 */
public abstract class BaseHolder<T> {
    public View mHolderView;
    public T mData;
    /*================1界面的展示=================*/
    public BaseHolder() {
        //通过构造方法，去初始化界面的根部局，需要子类去实现
        mHolderView = initHolderView();
        //有了根部局后，为了复用根部局里的控件，需要设置tag
        mHolderView.setTag(this);
    }

    public View getHolderView() {
        return mHolderView;
    }

    /**
     * 初始化根部局，子类必须实现
     */
    protected abstract View initHolderView();

    /*================2界面数据的刷新=================*/


    /**
     * 设置数据与刷新根部局UI
     *
     * @param data 将所需的数据传递进来
     */
    public void setDataAndRefreshHolderView(T data) {
        mData = data;//保存数据
        //刷新根部局UI，需要子类去具体实现
        refreshHolderView(data);
    }

    /**
     * 刷新根部局UI数据，子类必须实现
     */
    protected abstract void refreshHolderView(T data);

}
