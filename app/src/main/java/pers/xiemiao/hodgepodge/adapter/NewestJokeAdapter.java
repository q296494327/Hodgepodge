package pers.xiemiao.hodgepodge.adapter;

import java.util.List;

import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.base.SuperBaseAdapter;
import pers.xiemiao.hodgepodge.bean.NewestJokeBean;
import pers.xiemiao.hodgepodge.holder.jokeholder.NewestJokeHolder;

/**
 * User: xiemiao
 * Date: 2016-10-10
 * Time: 22:15
 * Desc: 最新笑话listview适配器
 */
public class NewestJokeAdapter extends SuperBaseAdapter<NewestJokeBean.ResultEntity.JokeDate> {

    public NewestJokeAdapter(List<NewestJokeBean.ResultEntity.JokeDate> dataSources) {
        super(dataSources);
    }


    @Override
    protected BaseHolder getSpecialHolder(int position) {
        return new NewestJokeHolder();
    }
}
