package pers.xiemiao.hodgepodge.base;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * User: xiemiao
 * Date: 2016-10-06
 * Time: 14:40
 * Desc: BaseAdapter的常规抽取
 */
public abstract class SuperBaseAdapter<T> extends BaseAdapter {

    public List<T> mDataSources = new ArrayList<T>();

    public SuperBaseAdapter(List<T> dataSources) {
        mDataSources = dataSources;
    }

    @Override
    public int getCount() {
        if (mDataSources != null) {
            return mDataSources.size();
        }
        return 0;
    }

    @Override
    public T getItem(int position) {
        if (mDataSources != null) {
            return mDataSources.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        BaseHolder holder = null;
        if (convertView == null) {
            holder = getSpecialHolder(position);//基类的holder由子类去实现
        } else {
            holder = (BaseHolder) convertView.getTag();
        }
        holder.setDataAndRefreshHolderView(getItem(position));

        //先缩小view
        ViewHelper.setScaleX(holder.mHolderView, 0.3f);
        ViewHelper.setScaleY(holder.mHolderView, 0.3f);
        //以属性动画放大
        ViewPropertyAnimator.animate(holder.mHolderView).setInterpolator(new
                OvershootInterpolator(2)).scaleX(1).setDuration(600).start();
        ViewPropertyAnimator.animate(holder.mHolderView).setInterpolator(new
                OvershootInterpolator(2)).scaleY(1).setDuration(600).start();
        return holder.mHolderView;
    }

    /**
     * 此处是基类，所以holder不能写死，获取专门的holder，子类必须实现
     */
    protected abstract BaseHolder getSpecialHolder(int position);


}
