package pers.xiemiao.hodgepodge.views;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;

/**
 * 自定义的MyFragmentTabHost
 * 可以设置指定的tab不显示
 */
public class MyFragmentTabHost extends FragmentTabHost {

    private String mCurrentTag;

    private String mNoTabChangedTag;

    public MyFragmentTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override//处理选项卡改变
    public void onTabChanged(String tag) {

        if (tag.equals(mNoTabChangedTag)) {
            //当选项卡切换到调用者设置的无效tag时,就将选项卡设置到记录的mCurrentTag
            setCurrentTabByTag(mCurrentTag);
        } else {
            //否则就按父类正常切换,并记录选项卡标记
            super.onTabChanged(tag);
            mCurrentTag = tag;
        }
    }

    public void setNoTabChangedTag(String tag) {
        this.mNoTabChangedTag = tag;
    }
}
