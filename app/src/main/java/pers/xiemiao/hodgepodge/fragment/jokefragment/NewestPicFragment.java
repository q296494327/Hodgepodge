package pers.xiemiao.hodgepodge.fragment.jokefragment;

import android.view.View;
import android.widget.TextView;

import pers.xiemiao.hodgepodge.base.BaseFragment;
import pers.xiemiao.hodgepodge.base.LoaddingPager;
import pers.xiemiao.hodgepodge.utils.UIUtils;

/**
 * User: xiemiao
 * Date: 2016-10-10
 * Time: 23:04
 * Desc: 笑话选项卡---最新趣图fragment
 */
public class NewestPicFragment extends BaseFragment {
    @Override
    public LoaddingPager.LoadResult initData() {

        return LoaddingPager.LoadResult.LOADSUCESS;
    }

    @Override
    public View initSuccessView() {
        TextView textView = new TextView(UIUtils.getContext());
        textView.setTextColor(0XFF000000);
        textView.setText(getClass().getSimpleName());
        return textView;
    }
}
