package pers.xiemiao.hodgepodge.factory;

import android.support.v4.util.SparseArrayCompat;

import pers.xiemiao.hodgepodge.base.BaseCartoonFragment;
import pers.xiemiao.hodgepodge.fragment.cartoonfragment.CoolFragment;
import pers.xiemiao.hodgepodge.fragment.cartoonfragment.HorrorFragment;
import pers.xiemiao.hodgepodge.fragment.cartoonfragment.NeiHanFragment;
import pers.xiemiao.hodgepodge.fragment.cartoonfragment.RandomHorrorFragment;

/**
 * User: xiemiao
 * Date: 2016-10-05
 * Time: 16:34
 * Desc: 卡通漫画fragment创建工厂
 */
public class CartoonFragmentFactory {

    //创建SparseArrayCompat用来内存缓存fragment(相比于一般的集合，有部分优化)
    public static SparseArrayCompat<BaseCartoonFragment> fragmentArray = new
            SparseArrayCompat<BaseCartoonFragment>();

    /**
     * 通过索引创建fragment
     *
     * @param position 索引
     * @return fragment
     */
    public static BaseCartoonFragment getFragment(int position) {
        BaseCartoonFragment fragment = fragmentArray.get(position);
        //1线从集合获取fragment
        if (fragment == null) {
            //2如果集合里取出的是空的，就根据索引去创建不同的fragment
            switch (position) {
                case 0:
                    fragment = new HorrorFragment();
                    break;
                case 1:
                    fragment = new RandomHorrorFragment();
                    break;
                case 2:
                    fragment = new NeiHanFragment();
                    break;
                case 3:
                    fragment = new CoolFragment();
                    break;
            }
            //3将fragment存进数组
            fragmentArray.put(position, fragment);
        }
        return fragment;
    }
}
