package pers.xiemiao.hodgepodge.adapter;

import java.util.List;

import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.base.SuperBaseAdapter;
import pers.xiemiao.hodgepodge.bean.NewestPicBean;
import pers.xiemiao.hodgepodge.holder.jokeholder.NewestPicHolder;

/**
 * User: xiemiao
 * Date: 2016-10-12
 * Time: 17:03
 * Desc: 最新图片适配器
 */
public class NewestPicAdapter extends SuperBaseAdapter<NewestPicBean.ResultEntity.NewestPicData> {
    public NewestPicAdapter(List<NewestPicBean.ResultEntity.NewestPicData> dataSources) {
        super(dataSources);
    }

    @Override
    protected BaseHolder getSpecialHolder(int position) {
        return new NewestPicHolder();
    }
}
