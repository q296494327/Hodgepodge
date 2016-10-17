package pers.xiemiao.hodgepodge.factory;

import android.support.v4.util.SparseArrayCompat;

import pers.xiemiao.hodgepodge.base.BaseJokeFragment;
import pers.xiemiao.hodgepodge.fragment.jokefragment.NewestJokeJokeFragment;
import pers.xiemiao.hodgepodge.fragment.jokefragment.NewestPicJokeFragment;
import pers.xiemiao.hodgepodge.fragment.jokefragment.RandomJokeJokeFragment;
import pers.xiemiao.hodgepodge.fragment.jokefragment.RandomPicJokeFragment;

/**
 * User: xiemiao
 * Date: 2016-10-05
 * Time: 16:34
 * Desc: 轻松一刻fragment创建工厂
 */
public class JokeFragmentFactory {

    //创建SparseArrayCompat用来内存缓存fragment(相比于一般的集合，有部分优化)
    public static SparseArrayCompat<BaseJokeFragment> fragmentArray = new
            SparseArrayCompat<BaseJokeFragment>();

    /**
     * 通过索引创建fragment
     *
     * @param position 索引
     * @return fragment
     */
    public static BaseJokeFragment getFragment(int position) {
        BaseJokeFragment fragment = fragmentArray.get(position);
        //1线从集合获取fragment
        if (fragment == null) {
            //2如果集合里取出的是空的，就根据索引去创建不同的fragment
            switch (position) {
//                case 0:
//                    fragment = new FunnyVideoFragment();
//                    break;
                case 0:
                    fragment = new RandomJokeJokeFragment();
                    break;
                case 1:
                    fragment = new RandomPicJokeFragment();
                    break;
                case 2:
                    fragment = new NewestJokeJokeFragment();
                    break;
                case 3:
                    fragment = new NewestPicJokeFragment();
                    break;
            }
            //3将fragment存进数组
            fragmentArray.put(position, fragment);
        }
        return fragment;
    }
}
