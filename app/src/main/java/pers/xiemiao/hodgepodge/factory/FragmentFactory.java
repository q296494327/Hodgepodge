package pers.xiemiao.hodgepodge.factory;

import android.support.v4.util.SparseArrayCompat;

import pers.xiemiao.hodgepodge.base.BaseFragment;
import pers.xiemiao.hodgepodge.fragment.jokefragment.NewestJokeFragment;
import pers.xiemiao.hodgepodge.fragment.jokefragment.NewestPicFragment;
import pers.xiemiao.hodgepodge.fragment.jokefragment.RandomJokeFragment;
import pers.xiemiao.hodgepodge.fragment.jokefragment.RandomPicFragment;

/**
 * User: xiemiao
 * Date: 2016-10-05
 * Time: 16:34
 * Desc: fragment创建工厂
 */
public class FragmentFactory {

    //创建SparseArrayCompat用来内存缓存fragment(相比于一般的集合，有部分优化)
    public static SparseArrayCompat<BaseFragment> fragmentArray = new
            SparseArrayCompat<BaseFragment>();

    /**
     * 通过索引创建fragment
     *
     * @param position 索引
     * @return fragment
     */
    public static BaseFragment getFragment(int position) {
        BaseFragment fragment = fragmentArray.get(position);
        //1线从集合获取fragment
        if (fragment == null) {
            //2如果集合里取出的是空的，就根据索引去创建不同的fragment
            switch (position) {
                case 0:
                    fragment = new NewestJokeFragment();
                    break;
                case 1:
                    fragment = new NewestPicFragment();
                    break;
                case 2:
                    fragment = new RandomJokeFragment();
                    break;
                case 3:
                    fragment = new RandomPicFragment();
                    break;
            }
            //3将fragment存进数组
            fragmentArray.put(position, fragment);
        }
        return fragment;
    }
}
