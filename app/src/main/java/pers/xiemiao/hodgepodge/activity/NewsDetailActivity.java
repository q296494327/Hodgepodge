package pers.xiemiao.hodgepodge.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.umeng.analytics.MobclickAgent;

import pers.xiemiao.hodgepodge.R;

/**
 * User: xiemiao
 * Date: 2016-10-14
 * Time: 16:30
 * Desc: 新闻详情页
 */
public class NewsDetailActivity extends AppCompatActivity {

    private String mUrl;
    private WebView mWebView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        init();
        initView();
        initActionBar();
    }

    public void init() {
        mUrl = getIntent().getStringExtra("url");
    }

    public void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.loadUrl(mUrl);//加载网页
        WebSettings webSettings = mWebView.getSettings();//获取web设置
        webSettings.setBuiltInZoomControls(true);//设置可双击缩放
        webSettings.setJavaScriptEnabled(true);//设置支持js
        //设置允许在webview里去加载网页
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }
        });
    }

    private void initActionBar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //如果webview网页可以后退就后退,否则就父类销毁activity
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        //如果webview网页可以后退就后退,否则就父类销毁activity
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }


    /*-------------------友盟统计---------------------*/
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("NewsDeatilActivity"); //统计页面
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("NewsDeatilActivity");
        MobclickAgent.onPause(this);
    }
}
