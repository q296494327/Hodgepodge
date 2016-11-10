package pers.xiemiao.hodgepodge.factory;

import android.support.v4.util.SparseArrayCompat;

import pers.xiemiao.hodgepodge.base.BaseGhostFragment;
import pers.xiemiao.hodgepodge.fragment.ghostfragment.ChangPianGhostFragment;
import pers.xiemiao.hodgepodge.fragment.ghostfragment.DuanPianGhostFragment;
import pers.xiemiao.hodgepodge.fragment.ghostfragment.JiaLiGhostFragment;
import pers.xiemiao.hodgepodge.fragment.ghostfragment.LingYiGhostFragment;
import pers.xiemiao.hodgepodge.fragment.ghostfragment.MingJianGhostFragment;
import pers.xiemiao.hodgepodge.fragment.ghostfragment.NeiHanGhostFragment;
import pers.xiemiao.hodgepodge.fragment.ghostfragment.XiaoYuanGhostFragment;
import pers.xiemiao.hodgepodge.fragment.ghostfragment.YiYuanGhostFragment;
import pers.xiemiao.hodgepodge.fragment.ghostfragment.YuanChuangGhostFragment;

/**
 * User: xiemiao
 * Date: 2016-10-05
 * Time: 16:34
 * Desc: 新闻5分钟fragment创建工厂
 */
public class GhostFragmentFactory {

    //创建SparseArrayCompat用来内存缓存fragment(相比于一般的集合，有部分优化)
    public static SparseArrayCompat<BaseGhostFragment> fragmentArray = new
            SparseArrayCompat<BaseGhostFragment>();

    /**
     * 通过索引创建fragment
     *
     * @param position 索引
     * @return fragment
     */
    public static BaseGhostFragment getFragment(int position) {
        BaseGhostFragment fragment = fragmentArray.get(position);
        //1线从集合获取fragment
        if (fragment == null) {
            //2如果集合里取出的是空的，就根据索引去创建不同的fragment
            switch (position) {
                case 0:
                    fragment = new DuanPianGhostFragment();
                    break;
                case 1:
                    fragment = new ChangPianGhostFragment();
                    break;
                case 2:
                    fragment = new XiaoYuanGhostFragment();
                    break;
                case 3:
                    fragment = new YiYuanGhostFragment();
                    break;
                case 4:
                    fragment = new JiaLiGhostFragment();
                    break;
                case 5:
                    fragment = new MingJianGhostFragment();
                    break;
                case 6:
                    fragment = new LingYiGhostFragment();
                    break;
                case 7:
                    fragment = new YuanChuangGhostFragment();
                    break;
                case 8:
                    fragment = new NeiHanGhostFragment();
                    break;
            }
            //3将fragment存进数组
            fragmentArray.put(position, fragment);
        }
        return fragment;
    }

    /**
     * 返回fragment的数量
     */
    public static int getFragmentCount() {
        return 9;
    }
}
