package pers.xiemiao.hodgepodge.views;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.qqtheme.framework.picker.OptionPicker;
import pers.xiemiao.hodgepodge.R;

/**
 * User: xiemiao
 * Date: 2016-10-22
 * Time: 00:31
 * Desc: 星座选择器
 */
public class StarPicker extends OptionPicker {

    public static final String[] starList = new String[]{
            "水瓶座",
            "双鱼座",
            "白羊座",
            "金牛座",
            "双子座",
            "巨蟹座",
            "狮子座",
            "处女座",
            "天秤座",
            "天蝎座",
            "射手座",
            "摩羯座",
    };

    /**
     * Instantiates a new Option picker.
     *
     * @param activity the activity
     */
    public StarPicker(Activity activity) {
        super(activity, starList);
        setTitleText("请选择你的星座");
        setSelectedItem("天秤座");
    }

    @Nullable
    @Override
    protected View makeHeaderView() {
        View view = LayoutInflater.from(activity).inflate(R.layout.picker_header, null);
        TextView titleView = (TextView) view.findViewById(R.id.picker_title);
        titleView.setText(titleText);
        return view;
    }

    @Nullable
    @Override
    protected View makeFooterView() {
        View view = LayoutInflater.from(activity).inflate(R.layout.picker_footer, null);
        ImageView submitView = (ImageView) view.findViewById(R.id.picker_submit);
//        submitView.setText(submitText);
        submitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onSubmit();
            }
        });
        ImageView cancelView = (ImageView) view.findViewById(R.id.picker_cancel);
//        cancelView.setText(cancelText);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onCancel();
            }
        });
        return view;
    }


}
