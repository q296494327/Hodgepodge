package pers.xiemiao.hodgepodge.holder.cartoonholder;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.bean.NeiHanCategoryBean;
import pers.xiemiao.hodgepodge.utils.SpUtil;
import pers.xiemiao.hodgepodge.utils.UIUtils;

/**
 * User: xiemiao
 * Date: 2016-10-18
 * Time: 20:45
 * Desc: 内涵分类itemholder
 */
public class NeiHanCategoryHolder extends BaseHolder<NeiHanCategoryBean.ShowapiResBodyEntity
        .PagebeanEntity.NeiHanCategoryData> {

    private TextView mTvTitle;
    private CardView mCardView;

    @Override
    protected View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_neihan, null);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mCardView = (CardView) view.findViewById(R.id.cardView);
        showItemRandomBg();//展示条目随机背景
        return view;
    }

    /**
     * 展示item随机背景颜色
     */
    private void showItemRandomBg() {
        //4生成一个随机默认背景颜色
        Random random = new Random();
        int red = 30 + random.nextInt(190);
        int green = 30 + random.nextInt(190);
        int blue = 30 + random.nextInt(190);
        int normalBg = Color.rgb(red, green, blue);
        mCardView.setCardBackgroundColor(normalBg);
    }

    @Override
    protected void refreshHolderView(NeiHanCategoryBean.ShowapiResBodyEntity.PagebeanEntity
                                             .NeiHanCategoryData data) {
        mTvTitle.setText(data.title.replace("内涵漫画：", ""));

        //刷新视图的时候根据sp里存的状态,去改变已读颜色
        String cartoonState = SpUtil.getString(UIUtils.getContext(), "cartoonState", "");
        String cartoonId = data.id.replace(":", "").replace("/", "").replace("?", "");
        if (cartoonState.contains(cartoonId)) {
            mTvTitle.setTextColor(0XFF787171);
            mCardView.setCardBackgroundColor(Color.WHITE);
        } else {
            mTvTitle.setTextColor(0XFF000000);
            showItemRandomBg();
        }

    }
}
