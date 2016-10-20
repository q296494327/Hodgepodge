package pers.xiemiao.hodgepodge.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.shizhefei.view.largeimage.LongImageView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.InputStream;

import okhttp3.Response;
import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.bean.NeiHanDetailBean;
import pers.xiemiao.hodgepodge.factory.ThreadPoolFactory;
import pers.xiemiao.hodgepodge.protocol.NeiHanDetailProtocol;
import pers.xiemiao.hodgepodge.utils.FileUtils;
import pers.xiemiao.hodgepodge.utils.ToastUtils;
import pers.xiemiao.hodgepodge.views.MyScrollView;

/**
 * User: xiemiao
 * Date: 2016-10-20
 * Time: 17:09
 * Desc: 内涵漫画详情activity
 */
public class NeiHanDetailActivity extends AppCompatActivity {

    private MyScrollView mScrollView;
    private LongImageView mIvNeihan;
    private String mLinkid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neihan_detail);
        mScrollView = (MyScrollView) findViewById(R.id.scrollView);
        mScrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);//设置边界阴影无
        mIvNeihan = (LongImageView) findViewById(R.id.iv_neihan);
        //获取传递过来的链接地址
        mLinkid = getIntent().getStringExtra("linkid");
        //判断文件存不存在,如果存在就直接加载
        String name = mLinkid.replace("/", "");
        String manhuaPath = FileUtils.getDir("manhua") + name + ".jpg";
        File file = new File(manhuaPath);
        if (file.exists()) {
            mIvNeihan.setImage(file.getAbsolutePath());
        } else {
            initData();//在子线程去请求网络获取数据
        }
    }

    private void initData() {


        ThreadPoolFactory.getNormalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    NeiHanDetailProtocol mProtocol = new NeiHanDetailProtocol();
                    final NeiHanDetailBean neiHanDetailBean = mProtocol.loadData(mLinkid);
                    String imgUrl = neiHanDetailBean.showapi_res_body.img;
                    //获取到图片链接的输入流
                    Response response = OkHttpUtils.get().url(imgUrl).build().execute();
                    final InputStream is = response.body().byteStream();
                    String name = mLinkid.replace("/", "").replace(":", "").replace("?", "");
                    final String manhuaPath = FileUtils.getDir("manhua") + name + ".jpg";
                    File file = new File(manhuaPath);
                    if (!file.exists()) {
                        //将得到的输入流写到缓存
                        boolean b = FileUtils.writeFile(is, manhuaPath, false);
//                        if (b) {
//                            ToastUtils.showSafeToast(UIUtils.getContext(), "写入成功");
//                        } else {
//                            ToastUtils.showSafeToast(UIUtils.getContext(), "写入失败");
//                        }
                    }
                    //得到了详情bean后,到子线程去更新UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mIvNeihan.setImage(manhuaPath);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showSafeToast(getApplicationContext(), "网络异常");
                }
            }
        });
    }


}
