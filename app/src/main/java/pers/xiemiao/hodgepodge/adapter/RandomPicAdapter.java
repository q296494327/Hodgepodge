package pers.xiemiao.hodgepodge.adapter;

import java.util.List;

import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.base.SuperBaseAdapter;
import pers.xiemiao.hodgepodge.bean.RandomPicBean;
import pers.xiemiao.hodgepodge.holder.jokeholder.RandomPicHolder;

/**
 * User: xiemiao
 * Date: 2016-10-12
 * Time: 23:07
 * Desc: 随机趣图适配器
 */
public class RandomPicAdapter extends SuperBaseAdapter<RandomPicBean.RandomPicData> {
    public RandomPicAdapter(List<RandomPicBean.RandomPicData> dataSources) {
        super(dataSources);
    }

    @Override
    protected BaseHolder getSpecialHolder(int position) {
        return new RandomPicHolder();
    }
}
