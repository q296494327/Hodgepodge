package pers.xiemiao.hodgepodge.holder.cartoonholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.List;

import okhttp3.Call;
import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.bean.CartoonCategoryBean;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.protocol.CartoonDetailProtocol;
import pers.xiemiao.hodgepodge.utils.DensityUtils;
import pers.xiemiao.hodgepodge.utils.FileUtils;
import pers.xiemiao.hodgepodge.utils.SpUtil;
import pers.xiemiao.hodgepodge.utils.StringUtils;
import pers.xiemiao.hodgepodge.utils.ToastUtils;
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
    private TextView mTvDownload;
    private ProgressBar mProgressbar;
    private TextView mTvProgress;
    private DownloadTask mDownloadTask;
    private TextView mTvDelete;
    private DeleteTask mDeleteTask;

    @Override
    protected View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_cartoon, null);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvTime = (TextView) view.findViewById(R.id.tv_time);
        mTvBookmark = (TextView) view.findViewById(R.id.tv_bookemark);
        mLLContainer = (LinearLayout) view.findViewById(R.id.ll_container);
        mTvDownload = (TextView) view.findViewById(R.id.tv_download);
        mProgressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        mTvProgress = (TextView) view.findViewById(R.id.tv_progress);
        mTvDelete = (TextView) view.findViewById(R.id.tv_delete);
        return view;
    }

    @Override
    protected void refreshHolderView(final CartoonCategoryBean.ShowapiResBodyEntity.PagebeanEntity
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

        mProgressbar.setTag(data.id);//设置标记,用来防止进度条显示错乱

        //刷新下载进度条的UI
        int spCount = SpUtil.getInt(UIUtils.getContext(), data.id + "Count", 0);
        int spSize = SpUtil.getInt(UIUtils.getContext(), data.id + "Size", 0);
        if (spCount == 0) {
            mTvProgress.setText("");
        } else {
            mTvProgress.setText(spCount + "/" + spSize);
        }
        mProgressbar.setMax(spSize);
        mProgressbar.setProgress(spCount);
        //点击下载按钮后
        mTvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int spCount = SpUtil.getInt(UIUtils.getContext(), data.id + "Count", 0);
                int spSize = SpUtil.getInt(UIUtils.getContext(), data.id + "Size", 0);
                if (spCount != 0 && spCount == spSize) {
                    ToastUtils.showToast("亲,已经下载完成了");
                } else {
                    //开启异步任务去执行下载漫画
                    if (mDownloadTask == null) {
                        count = 0;
                        mDownloadTask = new DownloadTask(data.id);
                        ThreadPoolFactory.getNormalThreadPool().execute(mDownloadTask);
                    }
                }
            }
        });

        //点击删除按钮后
        mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDownloadTask == null) {
                    //开启异步任务去删除漫画
                    mDeleteTask = new DeleteTask(data.id);
                    ThreadPoolFactory.getNormalThreadPool().execute(mDeleteTask);
                } else {
                    ToastUtils.showToast("亲,当前正在下载");
                }
            }
        });

    }


    /*================删除任务相关==begin===============*/

    /**
     * 删除任务
     */
    class DeleteTask implements Runnable {
        private String mLinkId;
        private List<String> mImgList;

        public DeleteTask(String linkId) {
            mLinkId = linkId;
        }

        @Override
        public void run() {
            try {
                //1请求网络去获取漫画图片集合
                CartoonDetailProtocol mProtocol = new CartoonDetailProtocol();
                mImgList = mProtocol.loadData(mLinkId, 0).showapi_res_body.item.imgList;
                //2遍历集合去开始删除漫画
                for (String img : mImgList) {
                    //剔除特殊符号作为缓存的文件名
                    String name = img.replace("/", "").replace(":", "").replace("?", "");
                    String manhuaPath = FileUtils.getDir("manhua") + name + ".jpg";
                    File file = new File(manhuaPath);//判断文件存不存在,存在才去删除
                    if (file.exists()) {
                        boolean deleteFile = file.delete();
                    }
                }
                UIUtils.postSafeTask(new Runnable() {
                    @Override
                    public void run() {
                        mTvProgress.setText("");
                        mProgressbar.setProgress(0);
                        //将count和size存到sp,供显示UI
                        SpUtil.putInt(UIUtils.getContext(), mLinkId + "Count", 0);
                        SpUtil.putInt(UIUtils.getContext(), mLinkId + "Size", 0);
                        mDownloadTask = null;
                        ToastUtils.showToast("删除缓存成功");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*================删除任务相关==end===============*/

    /*================下载任务相关==begin===============*/

    /**
     * 下载任务
     */
    class DownloadTask implements Runnable {
        private String mLinkId;
        private List<String> mImgList;

        public DownloadTask(String linkId) {
            mLinkId = linkId;
        }

        @Override
        public void run() {
            try {
                //1请求网络去获取漫画图片集合
                CartoonDetailProtocol mProtocol = new CartoonDetailProtocol();
                mImgList = mProtocol.loadData(mLinkId, 0).showapi_res_body.item.imgList;
                //2遍历集合去开始下载漫画图片
                for (String img : mImgList) {
                    //剔除特殊符号作为缓存的文件名
                    String name = img.replace("/", "").replace(":", "").replace("?", "");
                    //下载漫画到本地SD卡,然后到回调里去更新UI
                    OkHttpUtils.get().url(img).build().execute(new
                            DownloadFileCallBack(mLinkId, mImgList.size(), FileUtils.getDir
                            ("manhua"), name + "" +
                            ".jpg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showSafeToast(UIUtils.getContext(), "网络或服务器异常");
            }
        }
    }

    int count = 0;//记录下载的个数

    /**
     * 下载漫画文件到本地的回调
     */
    class DownloadFileCallBack extends FileCallBack {
        private String mLinkId;
        private int mSize;

        public DownloadFileCallBack(String linkId, int size, String destFileDir, String
                destFileName) {
            super(destFileDir, destFileName);
            mLinkId = linkId;
            mSize = size;
        }

        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(File response, int id) {
            count++;
            String tag = (String) mProgressbar.getTag();
            if (StringUtils.isEquals(tag, mLinkId)) {
                mTvProgress.setText(count + "/" + mSize);
                mProgressbar.setMax(mSize);
                mProgressbar.setProgress(count);
            }
            if (count == mSize) {
                //当下载完毕后将下载任务置空
                mDownloadTask = null;
            }
            //将count和size存到sp,供显示UI
            SpUtil.putInt(UIUtils.getContext(), mLinkId + "Count", count);
            SpUtil.putInt(UIUtils.getContext(), mLinkId + "Size", mSize);
        }
    }

    /*================下载任务相关==end===============*/
}
