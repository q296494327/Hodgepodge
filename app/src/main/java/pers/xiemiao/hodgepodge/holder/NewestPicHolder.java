package pers.xiemiao.hodgepodge.holder;

import android.view.View;

import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.bean.NewestPicBean;
import pers.xiemiao.hodgepodge.utils.UIUtils;

/**
 * User: xiemiao
 * Date: 2016-10-12
 * Time: 16:23
 * Desc: 最新趣图的item  holder
 */
public class NewestPicHolder extends BaseHolder<NewestPicBean.ResultEntity.NewestPicData> {
    @Override
    protected View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_newest_pic, null);

        return view;
    }

    @Override
    protected void refreshHolderView(NewestPicBean.ResultEntity.NewestPicData data) {

    }
}
