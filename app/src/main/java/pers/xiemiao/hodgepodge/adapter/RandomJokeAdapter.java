package pers.xiemiao.hodgepodge.adapter;

import java.util.List;

import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.base.SuperBaseAdapter;
import pers.xiemiao.hodgepodge.bean.RandomJokeBean;
import pers.xiemiao.hodgepodge.holder.jokeholder.RandomJokeHolder;

/**
 * User: xiemiao
 * Date: 2016-10-10
 * Time: 22:15
 * Desc: 随机笑话listview适配器
 */
public class RandomJokeAdapter extends SuperBaseAdapter<RandomJokeBean.RandomJokeData> {

    public RandomJokeAdapter(List<RandomJokeBean.RandomJokeData> dataSources) {
        super(dataSources);
    }


    @Override
    protected BaseHolder getSpecialHolder(int position) {
        return new RandomJokeHolder();
    }
}
