package pers.xiemiao.hodgepodge.holder.jokeholder;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;

import java.util.Random;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.socialization.QuickCommentBar;
import lib.lhh.fiv.library.FrescoZoomImageView;
import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.base.BaseHolder;
import pers.xiemiao.hodgepodge.bean.RandomPicBean;
import pers.xiemiao.hodgepodge.conf.Constants;
import pers.xiemiao.hodgepodge.utils.DensityUtils;
import pers.xiemiao.hodgepodge.utils.DrawableUtils;
import pers.xiemiao.hodgepodge.utils.ScreenUtil;
import pers.xiemiao.hodgepodge.utils.TimeUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;
import pers.xiemiao.hodgepodge.views.CircleImageView;

/**
 * User: xiemiao
 * Date: 2016-10-12
 * Time: 23:03
 * Desc:
 */
public class RandomPicHolder extends BaseHolder<RandomPicBean.RandomPicData> {
    private RelativeLayout mRlItemRoot;
    private LinearLayout mLlHead;
    private CircleImageView mIvHead;
    private TextView mTvTime;
    private TextView mTvContent;
    private FrescoZoomImageView mFrescoImageView;
    private QuickCommentBar mQcBar;
    private OnekeyShare mOks;

    @Override
    protected View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_newest_pic, null);
        mQcBar = (QuickCommentBar) view.findViewById(R.id.qcBar);
        mRlItemRoot = (RelativeLayout) view.findViewById(R.id.rl_item_root);
        mLlHead = (LinearLayout) view.findViewById(R.id.ll_head);
        mIvHead = (CircleImageView) view.findViewById(R.id.iv_head);
        mTvTime = (TextView) view.findViewById(R.id.tv_time);
        mTvContent = (TextView) view.findViewById(R.id.tv_content);
        mFrescoImageView = (FrescoZoomImageView) view.findViewById(R.id.fiv);
        initHeadIcon();
        showItemRandomBg();
        return view;
    }

    @Override
    protected void refreshHolderView(RandomPicBean.RandomPicData data) {
        mTvContent.setText(data.content);
        long unixtime = Long.parseLong(data.unixtime);
        mTvTime.setText("更新时间: " + TimeUtils.getTime(unixtime * 1000));
        if (data.content.equals("石油管道爆裂") || data.content.equals("小伙子，让开点")) {
            mFrescoImageView.setImageResource(R.mipmap.m5);
        } else {
            //加载网络图片mSdvPic
            Uri uri = Uri.parse(data.url);
            DraweeController mDraweeController = Fresco.newDraweeControllerBuilder()
                    .setAutoPlayAnimations(true).setUri(uri).build();
            mFrescoImageView.setController(mDraweeController);
            //设置点击监听
            mFrescoImageView.setOnDraweeClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击展示完整图片
                    showFullPic();
                }
            });
        }

        //底部评论栏的初始化设置
        initOnekeyShare(data);//初始化一键分享
        //关闭sso授权
        mOks.disableSSOWhenAuthorize();
        mQcBar.setTopic(data.hashId, data.content + "…",
                TimeUtils.getTime(unixtime * 1000), "佚名");
        mQcBar.getBackButton().setVisibility(View.GONE);
        mQcBar.setOnekeyShare(mOks);
    }

    /**
     * 初始化一键分享
     *
     * @param data
     */
    private void initOnekeyShare(final RandomPicBean.RandomPicData data) {
        ShareSDK.initSDK(UIUtils.getContext());
        mOks = new OnekeyShare();
        //关闭sso授权
        mOks.disableSSOWhenAuthorize();
        mOks.setImageUrl(data.url);
        mOks.setSite(UIUtils.getContext().getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        mOks.setSiteUrl(Constants.URLS.WEIDOWNLOAD);
    }

    /**
     * 展示完整图片
     */
    private void showFullPic() {
        //1获取屏幕宽高
        int screenWidth = ScreenUtil.getScreenWidth(UIUtils.getContext());
        int screenHeight = ScreenUtil.getScreenHeight(UIUtils.getContext());
        //2填充popwindow根部局,找到控件
        View layout = View.inflate(UIUtils.getContext(), R.layout.popup_pic, null);
        FrescoZoomImageView fivScreen = (FrescoZoomImageView) layout.findViewById(R.id.fiv_screen);
        //3设置网络图片到pop的布局上
        DraweeController mDraweeController = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true).setUri(mData.url).build();
        fivScreen.setController(mDraweeController);
        //4将控件宽高改为和屏幕一样大小
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.width = screenWidth;
        params.height = screenHeight * 2;
        fivScreen.setLayoutParams(params);
        //5将布局设置给全屏的popwindow
        final PopupWindow pop = new PopupWindow(layout,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                true);
        pop.setBackgroundDrawable(new ColorDrawable(0xffffff));//支持点击Back虚拟键退出
        pop.showAtLocation(mFrescoImageView, Gravity.NO_GRAVITY, 0, ScreenUtil.getStatusHeight
                (UIUtils.getContext()));
        //设置再点一下就关闭pop
        fivScreen.setOnDraweeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
    }

    /**
     * 初始化头图标
     */
    private void initHeadIcon() {
        int[] headIcons = {R.mipmap.m1, R.mipmap.m2, R.mipmap.m3, R.mipmap.m4, R.mipmap.m5, R
                .mipmap.m6, R.mipmap.m7, R.mipmap.m8, R.mipmap.m9, R.mipmap.m10, R.mipmap.m11, R
                .mipmap.m12, R.mipmap.m13, R.mipmap.m14, R.mipmap.m15, R.mipmap.m16, R.mipmap.m17, R
                .mipmap.m18, R.mipmap.m19, R.mipmap.m20, R.mipmap.m21, R.mipmap.m22, R.mipmap.m23, R
                .mipmap.m24, R.mipmap.m25, R.mipmap.m26, R.mipmap.m27, R.mipmap.m28, R.mipmap.m29, R
                .mipmap.m30};
        Random random = new Random();
        mIvHead.setImageResource(headIcons[random.nextInt(headIcons.length)]);
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
        //5通过颜色生成shpe
        GradientDrawable gradientDrawable = DrawableUtils.getGradientDrawable(normalBg,
                DensityUtils.dp2px(UIUtils.getContext(), 10));
        //6给控件设置shape背景
        mRlItemRoot.setBackgroundDrawable(gradientDrawable);
    }
}
