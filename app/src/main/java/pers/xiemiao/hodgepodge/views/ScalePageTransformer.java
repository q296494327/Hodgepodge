package pers.xiemiao.hodgepodge.views;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.animation.FloatEvaluator;
import com.nineoldandroids.view.ViewHelper;

/**
 * pager缩放旋转
 */
public class ScalePageTransformer implements ViewPager.PageTransformer {
    public void transformPage(View view, float position) {
        FloatEvaluator fl = new FloatEvaluator();
        if (position < -1) {
            ViewHelper.setRotation(view, 0);

        } else if (position <= 1) {
            ViewHelper.setRotationY(view, -30 * position);
            //直接传一个比率，从1缩放到0.7
            ViewHelper.setScaleY(view, fl.evaluate(position, 1f, 0.7f));
            ViewHelper.setScaleX(view, fl.evaluate(position, 1f, 0.7f));
        } else {

            ViewHelper.setRotation(view, 0);
        }
    }
}