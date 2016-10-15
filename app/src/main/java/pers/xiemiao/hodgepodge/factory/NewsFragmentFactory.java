package pers.xiemiao.hodgepodge.factory;

import android.support.v4.util.SparseArrayCompat;

import pers.xiemiao.hodgepodge.base.BaseNewsFragment;
import pers.xiemiao.hodgepodge.fragment.newsfragment.CaiJingFragment;
import pers.xiemiao.hodgepodge.fragment.newsfragment.GuoJiFragment;
import pers.xiemiao.hodgepodge.fragment.newsfragment.GuoNeiFragment;
import pers.xiemiao.hodgepodge.fragment.newsfragment.JunShiFragment;
import pers.xiemiao.hodgepodge.fragment.newsfragment.KeJiFragment;
import pers.xiemiao.hodgepodge.fragment.newsfragment.SheHuiFragment;
import pers.xiemiao.hodgepodge.fragment.newsfragment.ShiShangFragment;
import pers.xiemiao.hodgepodge.fragment.newsfragment.TiYuFragment;
import pers.xiemiao.hodgepodge.fragment.newsfragment.TopFragment;
import pers.xiemiao.hodgepodge.fragment.newsfragment.YuLeFragment;

/**
 * User: xiemiao
 * Date: 2016-10-05
 * Time: 16:34
 * Desc: 新闻5分钟fragment创建工厂
 */
public class NewsFragmentFactory {

    //创建SparseArrayCompat用来内存缓存fragment(相比于一般的集合，有部分优化)
    public static SparseArrayCompat<BaseNewsFragment> fragmentArray = new
            SparseArrayCompat<BaseNewsFragment>();

    /**
     * 通过索引创建fragment
     *
     * @param position 索引
     * @return fragment
     */
    public static BaseNewsFragment getFragment(int position) {
        BaseNewsFragment fragment = fragmentArray.get(position);
        //1线从集合获取fragment
        if (fragment == null) {
            //2如果集合里取出的是空的，就根据索引去创建不同的fragment
            switch (position) {
                case 0:
                    fragment = new TopFragment();
                    break;
                case 1:
                    fragment = new SheHuiFragment();
                    break;
                case 2:
                    fragment = new YuLeFragment();
                    break;
                case 3:
                    fragment = new JunShiFragment();
                    break;
                case 4:
                    fragment = new GuoNeiFragment();
                    break;
                case 5:
                    fragment = new GuoJiFragment();
                    break;
                case 6:
                    fragment = new TiYuFragment();
                    break;
                case 7:
                    fragment = new KeJiFragment();
                    break;
                case 8:
                    fragment = new CaiJingFragment();
                    break;
                case 9:
                    fragment = new ShiShangFragment();
                    break;
            }
            //3将fragment存进数组
            fragmentArray.put(position, fragment);
        }
        return fragment;
    }
}
