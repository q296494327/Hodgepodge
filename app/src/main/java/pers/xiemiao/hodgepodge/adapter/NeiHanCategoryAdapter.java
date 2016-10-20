package pers.xiemiao.hodgepodge.adapter;

import java.util.List;

import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.base.SuperBaseAdapter;
import pers.xiemiao.hodgepodge.bean.NeiHanCategoryBean;
import pers.xiemiao.hodgepodge.holder.cartoonholder.NeiHanCategoryHolder;

/**
 * User: xiemiao
 * Date: 2016-10-20
 * Time: 16:27
 * Desc: 内涵漫画分类适配器
 */
public class NeiHanCategoryAdapter extends SuperBaseAdapter<NeiHanCategoryBean
        .ShowapiResBodyEntity.PagebeanEntity.NeiHanCategoryData> {

    public NeiHanCategoryAdapter(List<NeiHanCategoryBean.ShowapiResBodyEntity.PagebeanEntity
            .NeiHanCategoryData> dataSources) {
        super(dataSources);
    }

    @Override
    protected BaseHolder getSpecialHolder(int position) {
        return new NeiHanCategoryHolder();
    }
}
