package pers.xiemiao.hodgepodge.dialog;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import pers.xiemiao.hodgepodge.R;

/**
 * 自定义快速选择对话框
 *
 * @author xiemiao
 */
public class QuickOptionDialog extends Dialog implements
        View.OnClickListener {

    private ImageView mClose;

    public QuickOptionDialog(Context context) {
        // 在创建一个参数的构造函数时，调用两个参数的构造函数，并将继承了对话框的style传递过去
        this(context, R.style.quick_option_dialog);
    }

    @SuppressLint("InflateParams")
    private QuickOptionDialog(Context context, int defStyle) {
        super(context, defStyle);
        //1填充对话框上的布局
        View contentView = getLayoutInflater().inflate(
                R.layout.dialog_quick_option, null);
        //2获取布局上的控件
        contentView.findViewById(R.id.ly_quick_option_text).setOnClickListener(
                this);
        View starView = contentView.findViewById(R.id.ly_quick_option_star);
        starView.setOnClickListener(this);
        ViewHelper.setTranslationX(starView,-100);
        ViewPropertyAnimator.animate(starView).translationX(0)
                .setDuration(1000).setInterpolator(new OvershootInterpolator(3)).start();
        contentView.findViewById(R.id.ly_quick_option_photo)
                .setOnClickListener(this);
        mClose = (ImageView) contentView.findViewById(R.id.iv_close);
        //3从xml加载动画，将关闭的控件设置旋转动画
        Animation operatingAnim = AnimationUtils.loadAnimation(getContext(),
                R.anim.quick_option_close);
        operatingAnim.setInterpolator(new LinearInterpolator());
        mClose.startAnimation(operatingAnim);
        //4设置控件的点击事件
        mClose.setOnClickListener(this);
        //设置默认对话框无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //5设置对话框触屏事件，触屏就关闭
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                QuickOptionDialog.this.dismiss();
                return true;
            }
        });
        //将布局设置为对话框的内容布局
        super.setContentView(contentView);

    }

    private QuickOptionDialog(Context context, boolean flag,
                              OnCancelListener listener) {
        super(context, flag, listener);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        // 将对话框设置在屏幕底部
        getWindow().setGravity(Gravity.BOTTOM);
        // 获取对话框的参数
        LayoutParams attributes = getWindow().getAttributes();
        // 将对话框的宽度设置和屏幕一样宽
        attributes.width = getWindow().getWindowManager().getDefaultDisplay()
                .getWidth();
        getWindow().setAttributes(attributes);// 将修改的参数设置
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.ly_quick_option_text:
                //                ToastUtils.showToast(getContext(), "文字被点击了");
                break;
            case R.id.ly_quick_option_star:
                //                ToastUtils.showToast(getContext(), "今日运势被点击了");
                break;
            case R.id.ly_quick_option_photo:
                //                ToastUtils.showToast(getContext(), "相机被点击了");
                break;
            default:
                break;
        }
        // 设置点击监听，将被点击id传给调用者
        if (mListener != null) {
            mListener.onQuickOptionClick(id);
        }
        dismiss();// 点击完后，将对话框消失
    }

    // 设置接口监听回调
    private OnQuickOptionformClick mListener;

    public void setOnQuickOptionformClickListener(
            OnQuickOptionformClick listener) {
        mListener = listener;
    }

    public interface OnQuickOptionformClick {
        void onQuickOptionClick(int id);
    }
}
