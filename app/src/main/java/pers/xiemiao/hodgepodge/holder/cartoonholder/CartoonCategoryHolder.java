package pers.xiemiao.hodgepodge.holder.cartoonholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.bean.CartoonCategoryBean;
import pers.xiemiao.hodgepodge.utils.DensityUtils;
import pers.xiemiao.hodgepodge.utils.SpUtil;
import pers.xiemiao.hodgepodge.utils.UIUtils;

/**
 * User: xiemiao
 * Date: 2016-10-18
 * Time: 20:45
 * Desc: 卡通分类itemholder
 */
public class CartoonCategoryHolder extends BaseHolder<CartoonCategoryBean.ShowapiResBodyEntity
        .PagebeanEntity.CartoonCategoryData> {

    private TextView mTvTitle;
    private TextView mTvTime;
    private LinearLayout mLLContainer;
    private TextView mTvBookmark;

    @Override
    protected View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_cartoon, null);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvTime = (TextView) view.findViewById(R.id.tv_time);
        mTvBookmark = (TextView) view.findViewById(R.id.tv_bookemark);
        mLLContainer = (LinearLayout) view.findViewById(R.id.ll_container);
        return view;
    }

    @Override
    protected void refreshHolderView(CartoonCategoryBean.ShowapiResBodyEntity.PagebeanEntity
                                             .CartoonCategoryData data) {
        //书签从SP里取数据,有数据,以ID+book为key
        String bookmark = SpUtil.getString(UIUtils.getContext(), data.id.replace("/", "") + "book",
                "");
        mTvBookmark.setText(bookmark);
        mTvTitle.setText(data.title.replace("-黑白漫话", ""));
        mTvTime.setText("更新时间:" + data.time);
        //根据数据里的缩略图集合,动态添加imageview
        List<String> thumbnailList = data.thumbnailList;
        //每次加载先清空一下容器
        mLLContainer.removeAllViews();
        for (String thumbnail : thumbnailList) {
            //创建图片控件,将weight比重设为1
            ImageView imageView = new ImageView(UIUtils.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
                    .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            params.leftMargin = DensityUtils.dp2px(UIUtils.getContext(), 1);
            params.rightMargin = DensityUtils.dp2px(UIUtils.getContext(), 1);
            imageView.setLayoutParams(params);
            Glide.with(UIUtils.getContext()).load(thumbnail).crossFade(1000).into(imageView);
            mLLContainer.addView(imageView);
        }

        //刷新视图的时候根据sp里存的状态,去改变已读颜色
        String cartoonState = SpUtil.getString(UIUtils.getContext(), "cartoonState", "");
        String cartoonId = data.id.replace(":", "").replace("/", "").replace("?", "");
        if (cartoonState.contains(cartoonId)) {
            mTvTitle.setTextColor(0XFF787171);
            mTvTime.setTextColor(0XFF787171);
        } else {
            mTvTitle.setTextColor(0XFF000000);
            mTvTime.setTextColor(0XFF000000);
        }

    }
}
