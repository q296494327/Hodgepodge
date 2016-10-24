package pers.xiemiao.hodgepodge.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import cn.qqtheme.framework.picker.OptionPicker;
import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.bean.StarBean;
import pers.xiemiao.hodgepodge.protocol.StarProtocol;
import pers.xiemiao.hodgepodge.utils.PinyinUtil;
import pers.xiemiao.hodgepodge.utils.SpUtil;
import pers.xiemiao.hodgepodge.utils.StringUtils;
import pers.xiemiao.hodgepodge.utils.ToastUtils;
import pers.xiemiao.hodgepodge.views.StarPicker;

/**
 * User: xiemiao
 * Date: 2016-10-22
 * Time: 00:03
 * Desc: 今日星座运势的activity
 */
public class StarActivity extends AppCompatActivity {
    private Button mSelectStar;
    private ImageView mIvStarpic;
    private TextView mTvTime;
    private RatingBar mRbSummary;
    private RatingBar mRbLove;
    private RatingBar mRbMoney;
    private RatingBar mRbWork;
    private TextView mTvLuckyTime;
    private TextView mTvDirection;
    private TextView mTvNum;
    private TextView mTvColor;
    private TextView mTvGrxz;
    private TextView mTvDayNotice;
    private TextView mTvLoveText;
    private TextView mTvWorkText;
    private TextView mTvMoneyText;
    private TextView mTvGeneralText;
    private StarTask mTask;
    private StarProtocol mProtocol;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star);
        initView();
    }


    private void initView() {
        mIvStarpic = (ImageView) findViewById(R.id.iv_starpic);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mRbSummary = (RatingBar) findViewById(R.id.rb_summary);
        mRbLove = (RatingBar) findViewById(R.id.rb_love);
        mRbMoney = (RatingBar) findViewById(R.id.rb_money);
        mRbWork = (RatingBar) findViewById(R.id.rb_work);
        mTvLuckyTime = (TextView) findViewById(R.id.tv_lucky_time);
        mTvDirection = (TextView) findViewById(R.id.tv_direction);
        mTvNum = (TextView) findViewById(R.id.tv_num);
        mTvColor = (TextView) findViewById(R.id.tv_color);
        mTvGrxz = (TextView) findViewById(R.id.tv_grxz);
        mTvDayNotice = (TextView) findViewById(R.id.tv_day_notice);
        mTvLoveText = (TextView) findViewById(R.id.tv_love_text);
        mTvWorkText = (TextView) findViewById(R.id.tv_work_text);
        mTvMoneyText = (TextView) findViewById(R.id.tv_money_text);
        mTvGeneralText = (TextView) findViewById(R.id.tv_general_text);
        mSelectStar = (Button) findViewById(R.id.select_star);

        String name = SpUtil.getString(this, "starName", "天秤座");
        mSelectStar.setText(name);//按钮默认设置文本
        requestData(PinyinUtil.getPinyin(name));//请求星座数据
        mSelectStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectStar();
            }
        });
    }

    /**
     * 展示选择器
     */
    private void showSelectStar() {
        StarPicker picker = new StarPicker(this);
        picker.setOffset(2);
        String starName = SpUtil.getString(StarActivity.this, "starName", "天秤座");
        String[] starList = picker.starList;
        for (int i = 0; i < starList.length; i++) {
            if (StringUtils.isEquals(starList[i], starName)) {
                picker.setSelectedIndex(i);
            }
        }
        picker.setTextSize(11);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                mSelectStar.setText(option);
                SpUtil.putString(StarActivity.this, "starName", option);
                //设置被选中后,请求网络获取今日运势
                requestData(PinyinUtil.getPinyin(option));
            }
        });
        picker.show();
    }

    /**
     * 请求星座今日运势数据
     */
    private void requestData(String starName) {
        if (mTask == null) {
            mTask = new StarTask();
            mTask.execute(starName);
        }
    }

    /**
     * 星座今日运势请求
     */
    class StarTask extends AsyncTask<String, Void, StarBean> {

        @Override
        protected StarBean doInBackground(String... params) {
            try {
                if (mProtocol == null) {
                    mProtocol = new StarProtocol();
                }
                StarBean starBean = mProtocol.loadData(params[0]);
                return starBean;
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showSafeToast(StarActivity.this, "网络异常");
            }
            return null;
        }

        @Override
        protected void onPostExecute(StarBean starBean) {
            //获取今日运势数据
            StarBean.ShowapiResBodyEntity.TodayData data = starBean.showapi_res_body.day;
            //刷新UI
            String time = "";
            time = data.time;
            time = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8);
            mTvTime.setText(time);
            mRbSummary.setRating(data.summary_star);
            mRbLove.setRating(data.love_star);
            mRbMoney.setRating(data.money_star);
            mRbWork.setRating(data.work_star);
            mTvLuckyTime.setText(data.lucky_time);
            mTvDirection.setText(data.lucky_direction);
            mTvNum.setText(data.lucky_num);
            mTvColor.setText(data.lucky_color);
            mTvGrxz.setText(data.grxz);
            mTvDayNotice.setText("  " + data.day_notice);
            mTvLoveText.setText("  " + data.love_txt);
            mTvWorkText.setText("  " + data.work_txt);
            mTvMoneyText.setText("  " + data.money_txt);
            mTvGeneralText.setText("  " + data.general_txt);

            //更换星座图片
            String starName = starBean.showapi_res_body.star;
            changePic(starName);
            mTask = null;//UI更新完毕后将任务置空
        }
    }

    /**
     * 更换星座图片
     */
    private void changePic(String starName) {
        if (starName.equals("baiyang")) {
            mIvStarpic.setBackgroundResource(R.mipmap.baiyang);
        } else if (starName.equals("jinniu")) {
            mIvStarpic.setBackgroundResource(R.mipmap.jinniu);
        } else if (starName.equals("shuangzi")) {
            mIvStarpic.setBackgroundResource(R.mipmap.shuangzi);
        } else if (starName.equals("juxie")) {
            mIvStarpic.setBackgroundResource(R.mipmap.juxie);
        } else if (starName.equals("shizi")) {
            mIvStarpic.setBackgroundResource(R.mipmap.shizi);
        } else if (starName.equals("chunv")) {
            mIvStarpic.setBackgroundResource(R.mipmap.chunv);
        } else if (starName.equals("tiancheng")) {
            mIvStarpic.setBackgroundResource(R.mipmap.tiancheng);
        } else if (starName.equals("tianxie")) {
            mIvStarpic.setBackgroundResource(R.mipmap.tianxie);
        } else if (starName.equals("sheshou")) {
            mIvStarpic.setBackgroundResource(R.mipmap.sheshou);
        } else if (starName.equals("mojie")) {
            mIvStarpic.setBackgroundResource(R.mipmap.moxie);
        } else if (starName.equals("shuiping")) {
            mIvStarpic.setBackgroundResource(R.mipmap.shuiping);
        } else if (starName.equals("shuangyu")) {
            mIvStarpic.setBackgroundResource(R.mipmap.shuangyu);
        }
    }


    /*-------------------友盟统计---------------------*/
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("StarActivity"); //统计页面
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("StarActivity");
        MobclickAgent.onPause(this);
    }
}
