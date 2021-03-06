package pers.xiemiao.hodgepodge.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.baidu.mobad.feeds.NativeResponse;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.socialization.QuickCommentBar;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.Call;
import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.bean.FunnyVideoBean;
import pers.xiemiao.hodgepodge.bean.NormalAndAdBean;
import pers.xiemiao.hodgepodge.conf.Constants;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.utils.FileUtils;
import pers.xiemiao.hodgepodge.utils.LogUtils;
import pers.xiemiao.hodgepodge.utils.MyVideoThumbLoader;
import pers.xiemiao.hodgepodge.utils.ToastUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;

/**
 * User: xiemiao
 * Date: 2016-10-15
 * Time: 15:39
 * Desc: 搞笑视频适配器
 */
public class FunnyVideoAdapter extends BaseAdapter {

    private FragmentActivity mActivity;
    private List<NormalAndAdBean> mDatas;
    private MyVideoThumbLoader mThumbLoader;
    private static final int AD = 0;
    private static final int VIDEO = 1;
    private OnekeyShare mOks;

    public FunnyVideoAdapter(FragmentActivity activity, List<NormalAndAdBean> datas) {
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
    public NormalAndAdBean getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        NormalAndAdBean bean = mDatas.get(position);
        if (bean.isAd) {
            return AD;
        } else {
            return VIDEO;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == VIDEO) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.item_funny_video, null);
                holder = new ViewHolder();
                holder.jcVideoPlayerStandard = (JCVideoPlayerStandard) convertView.findViewById(R.id
                        .custom_videoplayer_standard);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.qc_bar = (QuickCommentBar) convertView.findViewById(R.id.qcBar);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //获取单个item的数据
            final FunnyVideoBean.ShowapiResBodyEntity.PagebeanEntity.FunnyVideoData videoData =
                    getItem
                            (position).vData;
            //设置标题
            holder.tv_title.setText(videoData.text);
            //给video设置链接
            holder.jcVideoPlayerStandard.setUp(videoData.video_uri, JCVideoPlayer
                    .SCREEN_LAYOUT_LIST, "");
            //先设置默认图片为黑色
            holder.jcVideoPlayerStandard.thumbImageView.setImageDrawable(new ColorDrawable(0));
            holder.jcVideoPlayerStandard.thumbImageView.setScaleType(ImageView.ScaleType
                    .FIT_XY);

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
                mThumbLoader.showThumbByAsynctack(videoData.video_uri, holder
                        .jcVideoPlayerStandard
                        .thumbImageView);
            }

            //底部评论栏的初始化设置
            initOnekeyShare(videoData);//初始化一键分享
            holder.qc_bar.setTopic(videoData.id, videoData.text.trim().substring(0, 10) + "…",
                    videoData.create_time, videoData.name);
            holder.qc_bar.getBackButton().setVisibility(View.GONE);
            holder.qc_bar.setOnekeyShare(mOks);

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
        } else {
            final NativeResponse nrAd = getItem(position).ad;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.native_ad_row, null);
            }
            AQuery aq = new AQuery(convertView);
            aq.id(R.id.native_icon_image).image(nrAd.getIconUrl(), false, true);
            aq.id(R.id.native_main_image).image(nrAd.getImageUrl(), false, true);
            aq.id(R.id.native_text).text(nrAd.getDesc());
            aq.id(R.id.native_title).text(nrAd.getTitle());
            aq.id(R.id.native_brand_name).text(nrAd.getBrandName());
            aq.id(R.id.native_adlogo).image(nrAd.getAdLogoUrl(), false, true);
            aq.id(R.id.native_baidulogo).image(nrAd.getBaiduLogoUrl(), false, true);
            String text = nrAd.isDownloadApp() ? "下载" : "查看";
            aq.id(R.id.native_cta).text(text);
            nrAd.recordImpression(convertView);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nrAd.handleClick(v);
                }
            });
            return convertView;
        }
    }

    /**
     * 初始化一键分享的oks
     */
    private void initOnekeyShare(FunnyVideoBean.ShowapiResBodyEntity.PagebeanEntity.FunnyVideoData
                                         videoData) {
        ShareSDK.initSDK(mActivity);
        mOks = new OnekeyShare();
        //关闭sso授权
        mOks.disableSSOWhenAuthorize();

        //设置一个下载视频的logo
        Bitmap logo = BitmapFactory.decodeResource(UIUtils.getResources(), R.mipmap.save);
        final String url = videoData.video_uri;
        mOks.setCustomerLogo(logo, "保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //请求下载
                ThreadPoolFactory.getDownloadThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        String desFileDir = Environment.getExternalStorageDirectory()
                                .getAbsolutePath() + "/" + "shipin";
                        String fileName = url.replace(":", "").replace("?", "").replace("/",
                                "");
                        OkHttpUtils.get().url(url).build().execute(new DowmloadVideoCallback
                                (desFileDir, fileName));
                    }
                });
            }
        });

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        mOks.setTitle("分享一段视频请点击");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        mOks.setTitleUrl(videoData.weixin_url);
        // text是分享文本，所有平台都需要这个字段
        mOks.setText(videoData.text);
        //从缓存了缩略图的路径去读取作为图片
        File cacheFile = getCacheFile(videoData.video_uri);
        if (cacheFile.exists()) {
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            mOks.setImagePath(cacheFile.getAbsolutePath());
        }
        // site是分享此内容的网站名称，仅在QQ空间使用
        mOks.setSite(mActivity.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        mOks.setSiteUrl(Constants.URLS.WEIDOWNLOAD);
    }


    class ViewHolder {
        private JCVideoPlayerStandard jcVideoPlayerStandard;
        private TextView tv_title;
        private QuickCommentBar qc_bar;
    }

    /**
     * 获取缓存文件
     */
    private File getCacheFile(String path) {
        String cacheDir = FileUtils.getDir("thumbPic");
        String name = path.replace("/", "").replace(":", "") + ".jpg";
        return new File(cacheDir, name);
    }


    /**
     * 下载当前图片
     */
    class DowmloadVideoCallback extends FileCallBack {

        public DowmloadVideoCallback(String destFileDir, String destFileName) {
            super(destFileDir, destFileName);
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            ToastUtils.showToast("保存失败,请检查网络或SD卡挂载状态");
        }

        @Override
        public void onResponse(File response, int id) {
            ToastUtils.showToast("视频已保存至SD卡根目录的\"shipin\"文件夹下");
        }
    }

}
