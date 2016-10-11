package pers.xiemiao.hodgepodge.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pers.xiemiao.hodgepodge.utils.UIUtils;

/**
 * User: xiemiao
 * Date: 2016-10-10
 * Time: 23:15
 * Desc:
 */
public class DefauleFragment extends Fragment {

    private String mText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mText = bundle.getString("text");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView=new TextView(UIUtils.getContext());
        textView.setTextColor(0XFF000000);
        textView.setText(mText);
        return textView;
    }
}
