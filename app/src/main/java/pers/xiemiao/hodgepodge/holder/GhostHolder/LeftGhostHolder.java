package pers.xiemiao.hodgepodge.holder.GhostHolder;

import android.view.View;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import lib.lhh.fiv.library.FrescoImageView;
import okhttp3.Call;
import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.bean.GhostCategoryBean;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.utils.FileUtils;
import pers.xiemiao.hodgepodge.utils.SpUtil;
import pers.xiemiao.hodgepodge.utils.UIUtils;

/**
 * User: xiemiao
 * Date: 2016-10-14
 * Time: 00:29
 * Desc: 头条新闻holder
 */
public class LeftGhostHolder extends BaseHolder<GhostCategoryBean.ShowapiResBodyEntity
        .PagebeanEntity.GhostCategoryData> {

    private FrescoImageView mFivGhostpic;
    private TextView mTvTitle;
    private TextView mTvDesc;

    @Override
    protected View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_left_ghost, null);
        mFivGhostpic = (FrescoImageView) view.findViewById(R.id.fiv_ghostpic);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvDesc = (TextView) view.findViewById(R.id.tv_desc);
        return view;
    }

    @Override
    protected void refreshHolderView(final GhostCategoryBean.ShowapiResBodyEntity.PagebeanEntity
            .GhostCategoryData data) {
        mTvTitle.setText(data.title);
        mTvDesc.setText(data.desc);
        //服务器的链接加载不到图片，只有下载下来再设置图片内容了
        final String cacheDir = FileUtils.getDir("ghostPic");
        final String destName = data.img.replace("/", "").replace(":", "") + ".jpg";
        mFivGhostpic.loadLocalImage(cacheDir + "/" + destName, R.drawable
                .spinner_big_inner);
        ThreadPoolFactory.getDownloadThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get().url(data.img).build().execute(new FileCallBack(cacheDir,
                        destName) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(File response, int id) {
                        mFivGhostpic.loadLocalImage(response.getAbsolutePath(), R.drawable
                                .spinner_big_inner);
                    }
                });
            }
        });

        //刷新视图的时候根据sp里存的状态,去改变已读颜色
        String readState = SpUtil.getString(UIUtils.getContext(), "readState", "");
        String id = data.id.replace(":", "").replace("/", "").replace("?", "");
        if (readState.contains(id)) {
            mTvTitle.setTextColor(0XFF787171);
            mTvDesc.setTextColor(0XFF787171);
        } else {
            mTvTitle.setTextColor(0XFFE90808);
            mTvDesc.setTextColor(0XFF000000);
        }
    }


}
