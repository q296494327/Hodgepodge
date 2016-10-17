package pers.xiemiao.hodgepodge.adapter;

import com.markmao.pulltorefresh.widget.XListView;

import java.util.List;

import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.base.SuperBaseAdapter;
import pers.xiemiao.hodgepodge.bean.FunnyVideoBean;
import pers.xiemiao.hodgepodge.holder.jokeholder.FunnyVideoHolder;

/**
 * User: xiemiao
 * Date: 2016-10-15
 * Time: 15:39
 * Desc: 搞笑视频适配器
 */
public class FunnyVideoAdapter extends SuperBaseAdapter<FunnyVideoBean.ShowapiResBodyEntity
        .PagebeanEntity.FunnyVideoData> {

    private XListView mXListView;

    public FunnyVideoAdapter(List<FunnyVideoBean.ShowapiResBodyEntity
            .PagebeanEntity
            .FunnyVideoData> dataSources) {
        super(dataSources);
    }

    @Override
    protected BaseHolder getSpecialHolder(int position) {

        return new FunnyVideoHolder();
    }

}
