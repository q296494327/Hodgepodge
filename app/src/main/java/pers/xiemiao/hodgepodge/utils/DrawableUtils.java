package pers.xiemiao.hodgepodge.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * User: xiemiao
 * Date: 2016-10-07
 * Time: 21:46
 * Desc: 选择器工具
 */
public class DrawableUtils {
    /**
     * 获取一个圆角矩形Drawable
     *
     * @param color  背景颜色
     * @param radius 圆角半径
     */
    public static GradientDrawable getGradientDrawable(int color, float radius) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);//设置形状为矩形
        gradientDrawable.setColor(color);//设置颜色
        gradientDrawable.setCornerRadius(radius);//设置圆角
        return gradientDrawable;
    }

    /**
     * 获取一个状态选择器
     *
     * @param normalBg 默认图片
     * @param pressBg  按下图片
     */
    public static StateListDrawable getStateListDrawable(Drawable normalBg, Drawable pressBg) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressBg);//添加按下状态的图片
        stateListDrawable.addState(new int[]{}, normalBg);//添加正常状态的图片
        return stateListDrawable;
    }

    /**
     * 获取一个圆角矩形状态选择器
     *
     * @param normalColor 默认颜色
     * @param pressColor  按下颜色
     * @param radius      圆角半径
     */
    public static StateListDrawable getStateListDrawable(int normalColor, int pressColor, float radius) {
        GradientDrawable normalBg = getGradientDrawable(normalColor, radius);//颜色转为默认背景
        GradientDrawable pressBg = getGradientDrawable(pressColor, radius);//颜色转为按下背景
        StateListDrawable stateListDrawable = getStateListDrawable(normalBg, pressBg);
        return stateListDrawable;
    }


}
