package pers.xiemiao.hodgepodge.adapter;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.bean.FunnyVideoBean;
import pers.xiemiao.hodgepodge.utils.MyVideoThumbLoader;

/**
 * User: xiemiao
 * Date: 2016-10-15
 * Time: 15:39
 * Desc: 搞笑视频适配器
 */
public class FunnyVideoAdapter extends BaseAdapter {

    private FragmentActivity mActivity;
    private List<FunnyVideoBean.ShowapiResBodyEntity.PagebeanEntity.FunnyVideoData> mDatas;
    private MyVideoThumbLoader mThumbLoader;

    public FunnyVideoAdapter(FragmentActivity activity, List<FunnyVideoBean.ShowapiResBodyEntity
            .PagebeanEntity.FunnyVideoData> datas) {
        mActivity = activity;
        mDatas = datas;
        //初始化缩略图加载类
        mThumbLoader = new MyVideoThumbLoader();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public FunnyVideoBean.ShowapiResBodyEntity.PagebeanEntity.FunnyVideoData getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.item_funny_video, null);
            holder = new ViewHolder();
            holder.jcVideoPlayerStandard = (JCVideoPlayerStandard) convertView.findViewById(R.id
                    .custom_videoplayer_standard);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //获取单个item的数据
        FunnyVideoBean.ShowapiResBodyEntity.PagebeanEntity.FunnyVideoData videoData = getItem
                (position);
        //给video设置链接\名称
        holder.jcVideoPlayerStandard.setUp(videoData.video_uri, JCVideoPlayer.SCREEN_LAYOUT_LIST,
                videoData.text);
        //先设置默认图片为黑色
        holder.jcVideoPlayerStandard.thumbImageView.setImageDrawable(new ColorDrawable(0));
        //给缩略图控件设置tag,异步展示图片时通过tag解决图片错乱问题
        holder.jcVideoPlayerStandard.thumbImageView.setTag(videoData.video_uri);
        mThumbLoader.showThumbByAsynctack(videoData.video_uri, holder.jcVideoPlayerStandard
                .thumbImageView);

        return convertView;
    }

    class ViewHolder {
        private JCVideoPlayerStandard jcVideoPlayerStandard;
    }

}
