package pers.xiemiao.hodgepodge.factory;

import android.support.v4.util.SparseArrayCompat;

import pers.xiemiao.hodgepodge.base.BaseBeautyFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.AVFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.BiJiNiFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.MeiNvFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.MeiTunFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.MoteFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.QingQuFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.QunZhuangFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.RuFangFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.SiWaFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.TPZPFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.XieZhenFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.XingGanFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.XingGeFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.ZhiFuFragment;

/**
 * User: xiemiao
 * Date: 2016-10-05
 * Time: 16:34
 * Desc: 美女图片fragment创建工厂
 */
public class BeautyFragmentFactory {

    //创建SparseArrayCompat用来内存缓存fragment(相比于一般的集合，有部分优化)
    public static SparseArrayCompat<BaseBeautyFragment> fragmentArray = new
            SparseArrayCompat<BaseBeautyFragment>();

    /**
     * 通过索引创建fragment
     *
     * @param position 索引
     * @return fragment
     */
    public static BaseBeautyFragment getFragment(int position) {
        BaseBeautyFragment fragment = fragmentArray.get(position);
        //1线从集合获取fragment
        if (fragment == null) {
            //2如果集合里取出的是空的，就根据索引去创建不同的fragment
            switch (position) {
                case 0:
                    fragment = new QunZhuangFragment();
                    break;
                case 1:
                    fragment = new XieZhenFragment();
                    break;
                case 2:
                    fragment = new XingGeFragment();
                    break;
                case 3:
                    fragment = new RuFangFragment();
                    break;
                case 4:
                    fragment = new TPZPFragment();
                    break;
                case 5:
                    fragment = new AVFragment();
                    break;
                case 6:
                    fragment = new ZhiFuFragment();
                    break;
                case 7:
                    fragment = new XingGanFragment();
                    break;
                case 8:
                    fragment = new QingQuFragment();
                    break;
                case 9:
                    fragment = new MeiNvFragment();
                    break;
                case 10:
                    fragment = new MeiTunFragment();
                    break;
                case 11:
                    fragment = new MoteFragment();
                    break;
                case 12:
                    fragment = new BiJiNiFragment();
                    break;
                case 13:
                    fragment = new SiWaFragment();
                    break;
            }
            //3将fragment存进数组
            fragmentArray.put(position, fragment);
        }
        return fragment;
    }
}
