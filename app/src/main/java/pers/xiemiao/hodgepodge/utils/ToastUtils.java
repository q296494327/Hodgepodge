package pers.xiemiao.hodgepodge.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * ToastUtils
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-12-9
 */
public class ToastUtils {
    private static Toast toast = null; //Toast的对象！

    public static void showToast(Context mContext, String id) {
        if (toast == null) {
            toast = Toast.makeText(mContext, id, Toast.LENGTH_SHORT);
        } else {
            toast.setText(id);
        }
        toast.show();
    }

    public static void showToast(Context mContext, String id, int gravity) {
        if (toast == null) {
            toast = Toast.makeText(mContext, id, Toast.LENGTH_SHORT);
        } else {
            toast.setText(id);
        }
        toast.setGravity(gravity, 0, DensityUtils.dp2px(mContext, 80));
        toast.show();
    }

    public static void showSafeToast(final Context mContext, final String id) {
        UIUtils.postSafeTask(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(mContext, id, Toast.LENGTH_SHORT);
                } else {
                    toast.setText(id);
                }
                toast.show();
            }
        });
    }


    public static void showSafeToast(final String id, final int gravity) {
        UIUtils.postSafeTask(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(UIUtils.getContext(), id, Toast.LENGTH_SHORT);
                } else {
                    toast.setText(id);
                }
                toast.setGravity(gravity, 0, DensityUtils.dp2px(UIUtils.getContext(), 80));
                toast.show();
            }
        });
    }

    public static void showToast(String id) {
        if (toast == null) {
            toast = Toast.makeText(UIUtils.getContext(), id, Toast.LENGTH_SHORT);
        } else {
            toast.setText(id);
        }
        toast.show();
    }


}
