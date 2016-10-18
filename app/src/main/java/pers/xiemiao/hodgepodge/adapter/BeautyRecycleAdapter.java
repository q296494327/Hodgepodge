package pers.xiemiao.hodgepodge.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import lib.lhh.fiv.library.FrescoImageView;
import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.activity.BeautyDetailActivity;
import pers.xiemiao.hodgepodge.bean.GirlCategoryBean;
import pers.xiemiao.hodgepodge.utils.UIUtils;

/**
 * User: xiemiao
 * Date: 2016-10-17
 * Time: 18:22
 * Desc: 美女recycle适配器
 */
public class BeautyRecycleAdapter extends RecyclerView.Adapter<BeautyRecycleAdapter.BeautyHolder> {

    private List<GirlCategoryBean.ShowapiResBodyEntity.PagebeanEntity.GirlCategoryData> mDatas;

    public BeautyRecycleAdapter(List<GirlCategoryBean.ShowapiResBodyEntity.PagebeanEntity
            .GirlCategoryData> datas) {
        mDatas = datas;
    }

    @Override//创建viewholder布局,初始化item根部局
    public BeautyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //填充item根部局
        View itemview = View.inflate(UIUtils.getContext(), R.layout.item_beauty_pic, null);
        //将根部局传递给viewholder
        return new BeautyHolder(itemview);
    }

    @Override//数据和holder绑定
    public void onBindViewHolder(BeautyHolder holder, final int position) {
        holder.setDataAndRefreshUI(mDatas.get(position));
        //设置图片的点击事件,并将数据传递给activity去展示图片
        holder.mFivBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UIUtils.getContext(), BeautyDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("imgurl", mDatas.get(position).link);
                UIUtils.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    /**
     * recycle专门的viewholder
     */
    class BeautyHolder extends RecyclerView.ViewHolder {
        private TextView mTvCount;
        private FrescoImageView mFivBeauty;

        public BeautyHolder(View itemView) {
            super(itemView);
            //找到itemview上的控件
            mFivBeauty = (FrescoImageView) itemView.findViewById(R.id.fiv_beauty);
        }

        //设置数据和刷新UI
        public void setDataAndRefreshUI(GirlCategoryBean.ShowapiResBodyEntity.PagebeanEntity
                                                .GirlCategoryData data) {
            mFivBeauty.setImageURI(data.img);
        }
    }
}