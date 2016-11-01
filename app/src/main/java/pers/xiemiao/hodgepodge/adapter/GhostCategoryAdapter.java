package pers.xiemiao.hodgepodge.adapter;

import java.util.List;

import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.base.SuperBaseAdapter;
import pers.xiemiao.hodgepodge.bean.GhostCategoryBean;
import pers.xiemiao.hodgepodge.holder.GhostHolder.LeftGhostHolder;
import pers.xiemiao.hodgepodge.holder.GhostHolder.RightGhostHolder;

/**
 * User: xiemiao
 * Date: 2016-10-14
 * Time: 01:23
 * Desc: 鬼故事分类适配器
 */
public class GhostCategoryAdapter extends SuperBaseAdapter<GhostCategoryBean.ShowapiResBodyEntity
        .PagebeanEntity.GhostCategoryData> {
    public static final int LeftPicType = 0;
    public static final int RightPicType = 1;

    public GhostCategoryAdapter(List<GhostCategoryBean.ShowapiResBodyEntity
            .PagebeanEntity.GhostCategoryData> dataSources) {
        super(dataSources);
    }

    /*-------------------增加listview显示类型---begin------------------*/

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 4 == 0) {
            return LeftPicType;
        } else {
            return RightPicType;
        }
    }

    /*-------------------增加listview显示类型---end------------------*/

    @Override
    protected BaseHolder getSpecialHolder(int position) {
        if (getItemViewType(position) == RightPicType) {
            return new RightGhostHolder();
        } else {
            return new LeftGhostHolder();
        }
    }
}
