package pers.xiemiao.hodgepodge.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import pers.xiemiao.hodgepodge.activity.MainActivity;

/**
 * User: xiemiao
 * Date: 2016-10-09
 * Time: 10:11
 * Desc: 基类activity,规范流程,定义公共方法
 */
public abstract class BaseActivity extends AppCompatActivity {
    //创建一个链表集合,用来存所有的子类activity,方便统一处理
    List<AppCompatActivity> mActivities = new LinkedList<AppCompatActivity>();
    private Activity mCurActivity;
    private long mPreTimeMillis;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();//最先的初始化
        initView();//初始化布局控件等
        initActionBar();//初始化actionbar
        initData();//初始化数据
        initListener();//初始化监听器
        mActivities.add(this);//子类一创建,就将它添加到链表集合
    }

    public void init() {

    }

    /**
     * 初始化布局,子类必须实现
     */
    public abstract void initView();

    public void initActionBar() {

    }

    public void initData() {

    }

    public void initListener() {

    }

    @Override
    protected void onDestroy() {
        //子类activity销毁时,就将它从链表移除
        mActivities.remove(this);
        super.onDestroy();
    }


    /**
     * 得到最上层activity
     */
    public Activity getCurActivity() {
        return mCurActivity;
    }

    /**
     * 完全退出程序
     */
    public void exit() {
        for (AppCompatActivity activity : mActivities) {
            activity.finish();
        }
    }

    @Override//统一退出控制
    public void onBackPressed() {
        //如果是入口,就去执行2秒内按返回键执行销毁
        if (this instanceof MainActivity) {
            if (System.currentTimeMillis() - mPreTimeMillis > 2000) {
                Toast.makeText(BaseActivity.this, "再按一次,退出大杂烩", Toast.LENGTH_SHORT).show();
                mPreTimeMillis = System.currentTimeMillis();
                return;//间隔在2秒之外就直接return,不执行下面的销毁
            }else {
                System.exit(0);
            }
        }
        //不是入口,就直接执行销毁
        super.onBackPressed();//finish
    }
}
