package pers.xiemiao.hodgepodge.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import java.util.Hashtable;

/**
 * User: xiemiao
 * Date: 2016-10-18
 * Time: 11:12
 * Desc: 视频缩略图异步加载类
 */
public class MyVideoThumbLoader {
    private ImageView imgView;
    private String path;
    //创建cache
    private LruCache<String, Bitmap> lruCache;

    //在构造方法里初始化lruCache缓存
    public MyVideoThumbLoader() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取最大的运行内存
        int maxSize = maxMemory / 4;//根据最大内存拿到缓存的内存大小
        //将最大缓存内存传给lruCache,它会根据最大值进行缓存的加载和移除
        lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override//重写sizeof方法,将每次存入的bitmap的大小返回,以便计算缓存的增删
            protected int sizeOf(String key, Bitmap value) {
                //这个方法会在每次存入缓存的时候调用
                return value.getByteCount();
            }
        };
    }

    /**
     * 添加视频到缓存
     *
     * @param path   视频的路径为key
     * @param bitmap 缩略图bitmap
     */
    public void addVideoThumbToCache(String path, Bitmap bitmap) {
        if (getVideoThumbToCache(path) == null) {
            //当前地址没有缓存时，就添加
            lruCache.put(path, bitmap);
        }
    }

    /**
     * 获取视频缓存缩略图
     *
     * @param path 根据path为key去获取
     */
    public Bitmap getVideoThumbToCache(String path) {
        return lruCache.get(path);
    }

    /**
     * 异步展示缩略图
     *
     * @param path    将视频的path传递进来
     * @param imgview 将缩略图imageview传递进来
     */
    public void showThumbByAsynctack(String path, ImageView imgview) {

        if (getVideoThumbToCache(path) == null) {
            //如果缓存不存在,就将缩略图控件和path去创建一个异步任务去执行
            new MyBobAsynctack(imgview, path).execute(path);
        } else {
            //缓存存在就直接从缓存区设置缩略图
            imgview.setImageBitmap(getVideoThumbToCache(path));
        }

    }

    class MyBobAsynctack extends AsyncTask<String, Void, Bitmap> {
        private ImageView imgView;
        private String path;

        public MyBobAsynctack(ImageView imageView, String path) {
            this.imgView = imageView;
            this.path = path;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //在子线程去获取缩略图
            Bitmap bitmap = createVideoThumbnail(params[0], MediaStore.Images.Thumbnails.MINI_KIND);
            //加入到缓存中
            if (getVideoThumbToCache(params[0]) == null) {
                addVideoThumbToCache(path, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //这里取出控件的tag,去和path对比,相等就去展示
            if (imgView.getTag().equals(path)) {
                // imageView，这是解决Listview加载图片错位的解决办法之一
                imgView.setImageBitmap(bitmap);
            }
        }
    }


    /**
     * 获取图片缩略图
     */
    public Bitmap createVideoThumbnail(String filePath, int kind) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (filePath.startsWith("http://")
                    || filePath.startsWith("https://")
                    || filePath.startsWith("widevine://")) {
                retriever.setDataSource(filePath, new Hashtable<String, String>());
            } else {
                retriever.setDataSource(filePath);
            }
            bitmap = retriever.getFrameAtTime(1000);
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
                ex.printStackTrace();
            }
        }

        if (bitmap == null)
            return null;

        if (kind == MediaStore.Images.Thumbnails.MINI_KIND) {
            // Scale down the bitmap if it's too large.
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int max = Math.max(width, height);
            if (max > 512) {
                float scale = 512f / max;
                int w = Math.round(scale * width);
                int h = Math.round(scale * height);
                bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
            }
        } else if (kind == MediaStore.Images.Thumbnails.MICRO_KIND) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap,
                    96,
                    96,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }
}
