package pers.xiemiao.hodgepodge.holder.jokeholder;

import android.view.View;

import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.bean.FunnyVideoBean;

/**
 * User: xiemiao
 * Date: 2016-10-15
 * Time: 15:40
 * Desc: 搞笑视频holder
 */
public class FunnyVideoHolder extends BaseHolder<FunnyVideoBean.ShowapiResBodyEntity
        .PagebeanEntity.FunnyVideoData> implements View.OnClickListener {

//    private TextureVideoView mVideoView;
//    private Timer mTimer;
//    private ImageView mPlayBtn;
//    private FrescoImageView mThumPic;
//    private ProgressBar mProgressBar;
//
//    @Override
    protected View initHolderView() {
//        View view = View.inflate(UIUtils.getContext(), R.layout.item_funny_video, null);
//        mVideoView = (TextureVideoView) view.findViewById(R.id.video_view);
//        mPlayBtn = (ImageView) view.findViewById(R.id.play);
//        mThumPic = (FrescoImageView) view.findViewById(R.id.thumPic);
//        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        return null;
    }
//
    @Override
    protected void refreshHolderView(final FunnyVideoBean.ShowapiResBodyEntity.PagebeanEntity
            .FunnyVideoData data) {
//        mVideoView.stop();
//        mVideoView.setVisibility(View.GONE);
//        mPlayBtn.setVisibility(View.VISIBLE);
//        mVideoView.setVideoPath(data.video_uri);
//        mPlayBtn.setOnClickListener(this);
//        mVideoView.setOnClickListener(this);
//        mVideoView.setMediaPlayerCallback(new MyMediaPlayerCallback());
//        ThreadPoolFactory.getNormalThreadPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                final Bitmap bitmap = UIUtils.createVideoThumbnail(data.video_uri, MediaStore
//                        .Images
//                        .Thumbnails
//                        .MINI_KIND);
//                UIUtils.postSafeTask(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (bitmap != null) {
//                            mThumPic.setImageBitmap(bitmap);
//                        } else {
//                            mProgressBar.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });
//            }
//        });
    }
//
//    /**
//     * 视频播放回调监听
//     */
//    class MyMediaPlayerCallback implements TextureVideoView.MediaPlayerCallback {
//        @Override
//        public void onPrepared(MediaPlayer mp) {
//            mProgressBar.setVisibility(View.GONE);
//            mThumPic.setVisibility(View.GONE);
//        }
//
//        @Override
//        public void onCompletion(MediaPlayer mp) {
//
//        }
//
//        @Override
//        public void onBufferingUpdate(MediaPlayer mp, int percent) {
//
//        }
//
//        @Override
//        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//
//        }
//
//        @Override
//        public boolean onInfo(MediaPlayer mp, int what, int extra) {
//            return false;
//        }
//
//        @Override
//        public boolean onError(MediaPlayer mp, int what, int extra) {
//            return false;
//        }
//    }
//
//
    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.play:
//                if (mVideoView.isPlaying()) {
//                    mVideoView.pause();
//                } else {
//                    mVideoView.resume();
//                    mVideoView.setVisibility(View.VISIBLE);
//                    mPlayBtn.setVisibility(View.GONE);
//                    mProgressBar.setVisibility(View.VISIBLE);
//                }
//                break;
//            case R.id.video_view:
//                mPlayBtn.setVisibility(View.VISIBLE);
//                mPlayBtn.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mPlayBtn.setVisibility(View.GONE);
//                    }
//                }, 2000);
//                break;
//        }
    }
//
//    /**
//     * 获取缓存文件
//     */
//    @NonNull
//    private File getCacheFile(String url) {
//        String cacheDir = FileUtils.getDir("thumpic");//创建以sd卡或者缓存文件夹为路径，thumpic为文件夹名称
//        url = url.replace("/", "").replace(":", "") + ".jpg";
//        return new File(cacheDir, url);
//    }
//
//    /**
//     * 保存方法
//     */
//    public void saveBitmap(Bitmap bm, File f) {
//        if (f.exists()) {
//            f.delete();
//        }
//        try {
//            FileOutputStream out = new FileOutputStream(f);
//            bm.compress(Bitmap.CompressFormat.PNG, 0, out);
//            out.flush();
//            out.close();
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }

}
