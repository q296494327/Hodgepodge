package pers.xiemiao.hodgepodge.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.io.File;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.bean.FunnyVideoBean;
import pers.xiemiao.hodgepodge.utils.FileUtils;
import pers.xiemiao.hodgepodge.utils.LogUtils;
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
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //获取单个item的数据
        FunnyVideoBean.ShowapiResBodyEntity.PagebeanEntity.FunnyVideoData videoData = getItem
                (position);
        //设置标题
        holder.tv_title.setText(videoData.text);
        //给video设置链接
        holder.jcVideoPlayerStandard.setUp(videoData.video_uri, JCVideoPlayer.SCREEN_LAYOUT_LIST,
                "");
        //先设置默认图片为黑色
        holder.jcVideoPlayerStandard.thumbImageView.setImageDrawable(new ColorDrawable(0));
        holder.jcVideoPlayerStandard.thumbImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //给缩略图控件设置tag,异步展示图片时通过tag解决图片错乱问题
        holder.jcVideoPlayerStandard.thumbImageView.setTag(videoData.video_uri);
        //设置缩略图
        File cacheFile = getCacheFile(videoData.video_uri);
        if (cacheFile.exists()) {
            LogUtils.sf("从缓存读取缩略图");
            //1从SD卡去获取缓存的缩略图
            Bitmap bitmap = BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
            holder.jcVideoPlayerStandard.thumbImageView.setImageBitmap(bitmap);
        } else {
            //2SD卡里没有的话,就去异步请求缩略图
            mThumbLoader.showThumbByAsynctack(videoData.video_uri, holder.jcVideoPlayerStandard
                    .thumbImageView);
        }

        //设置动画效果
        //先缩小view
        ViewHelper.setScaleX(convertView, 0.3f);
        ViewHelper.setScaleY(convertView, 0.3f);
        //以属性动画放大
        ViewPropertyAnimator.animate(convertView).setInterpolator(new
                OvershootInterpolator(1)).scaleX(1).setDuration(800).start();
        ViewPropertyAnimator.animate(convertView).setInterpolator(new
                OvershootInterpolator(1)).scaleY(1).setDuration(800).start();

        return convertView;
    }

    class ViewHolder {
        private JCVideoPlayerStandard jcVideoPlayerStandard;
        private TextView tv_title;
    }

    /**
     * 获取缓存文件
     */
    private File getCacheFile(String path) {
        String cacheDir = FileUtils.getDir("thumbPic");
        String name = path.replace("/", "").replace(":", "") + ".jpg";
        return new File(cacheDir, name);
    }

}
