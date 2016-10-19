package pers.xiemiao.hodgepodge.adapter;

import java.util.List;

import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.base.SuperBaseAdapter;
import pers.xiemiao.hodgepodge.bean.CartoonCategoryBean;
import pers.xiemiao.hodgepodge.holder.cartoonholder.CartoonCategoryHolder;

/**
 * User: xiemiao
 * Date: 2016-10-18
 * Time: 20:40
 * Desc: 卡通漫画分类适配器
 */
public class CartoonCategoryAdapter extends SuperBaseAdapter<CartoonCategoryBean
        .ShowapiResBodyEntity.PagebeanEntity.CartoonCategoryData> {

    public CartoonCategoryAdapter(List<CartoonCategoryBean.ShowapiResBodyEntity.PagebeanEntity
            .CartoonCategoryData> dataSources) {
        super(dataSources);
    }

    @Override
    protected BaseHolder getSpecialHolder(int position) {
        return new CartoonCategoryHolder();
    }
}
