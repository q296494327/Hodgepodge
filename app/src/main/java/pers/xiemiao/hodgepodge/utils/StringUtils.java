package pers.xiemiao.hodgepodge.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class StringUtils {
    public final static String UTF_8 = "utf-8";

    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isEmpty(String value) {
        if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value
                .trim())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true
     */
    public static boolean isEquals(String... agrs) {
        String last = null;
        for (int i = 0; i < agrs.length; i++) {
            String str = agrs[i];
            if (isEmpty(str)) {
                return false;
            }
            if (last != null && !str.equalsIgnoreCase(last)) {
                return false;
            }
            last = str;
        }
        return true;
    }

    /**
     * 返回一个高亮spannable
     *
     * @param content 文本内容
     * @param color   高亮颜色
     * @param start   起始位置
     * @param end     结束位置
     * @return 高亮spannable
     */
    public static CharSequence getHighLightText(String content, int color, int start, int end) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        start = start >= 0 ? start : 0;
        end = end <= content.length() ? end : content.length();
        SpannableString spannable = new SpannableString(content);
        CharacterStyle span = new ForegroundColorSpan(color);
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 获取链接样式的字符串，即字符串下面有下划线
     *
     * @param resId 文字资源
     * @return 返回链接样式的字符串
     */
    public static Spanned getHtmlStyleString(int resId) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href=\"\"><u><b>").append(UIUtils.getString(resId)).append(" </b></u></a>");
        return Html.fromHtml(sb.toString());
    }

    /**
     * 格式化文件大小，不保留末尾的0
     */
    public static String formatFileSize(long len) {
        return formatFileSize(len, false);
    }

    /**
     * 格式化文件大小，保留末尾的0，达到长度一致
     */
    public static String formatFileSize(long len, boolean keepZero) {
        String size;
        DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
        DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
        if (len < 1024) {
            size = String.valueOf(len + "B");
        } else if (len < 10 * 1024) {
            // [0, 10KB)，保留两位小数
            size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
        } else if (len < 100 * 1024) {
            // [10KB, 100KB)，保留一位小数
            size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
        } else if (len < 1024 * 1024) {
            // [100KB, 1MB)，个位四舍五入
            size = String.valueOf(len / 1024) + "KB";
        } else if (len < 10 * 1024 * 1024) {
            // [1MB, 10MB)，保留两位小数
            if (keepZero) {
                size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024 / 1024 / (float)
                        100)) + "MB";
            } else {
                size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100) + "MB";
            }
        } else if (len < 100 * 1024 * 1024) {
            // [10MB, 100MB)，保留一位小数
            if (keepZero) {
                size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024 / 1024 / (float)
                        10)) + "MB";
            } else {
                size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10) + "MB";
            }
        } else if (len < 1024 * 1024 * 1024) {
            // [100MB, 1GB)，个位四舍五入
            size = String.valueOf(len / 1024 / 1024) + "MB";
        } else {
            // [1GB, ...)，保留两位小数
            size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100) + "GB";
        }
        return size;
    }


    /**
     * 将字符串转成图片到存储卡
     *
     * @param path 文件保存路径
     * @param str  要转换的文字
     * @param leng 一行要几个字
     */
    public static Bitmap stringToImage(String path, String str, int leng) {
        ArrayList<String> data = new ArrayList<String>();
        String substring;
        for (int i = 0; i <= Math.ceil(str.length() / leng); i++) {
            if (i == Math.ceil(str.length() / leng)) {
                //当是i为最后一位时，直接截取到最后
                substring = str.substring(i * leng, str.length());
            } else {
                //否则就截取为leng的长度
                substring = str.substring(i * leng, (i + 1) * leng);
            }
            data.add(substring);
        }
        try {
            int height = data.size() * 24;     //图片高
            Bitmap bitmap = Bitmap.createBitmap(leng * 20, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);   //背景颜色

            Paint p = new Paint();
            p.setAntiAlias(true);//设置抗锯齿
            p.setColor(Color.BLACK);   //画笔颜色
            p.setTextSize(15);         //画笔粗细
            for (int i = 0; i < data.size(); i++) {
                canvas.drawText(data.get(i), 20, (i + 1) * 20, p);
            }

            Log.e("path", path);
            //将Bitmap保存为png图片
            FileOutputStream out = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            Log.e("done", "done");
            return bitmap;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }
}
