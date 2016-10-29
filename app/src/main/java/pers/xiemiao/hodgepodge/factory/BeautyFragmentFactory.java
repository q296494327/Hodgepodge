package pers.xiemiao.hodgepodge.factory;

import android.support.v4.util.SparseArrayCompat;

import pers.xiemiao.hodgepodge.base.BaseBeautyFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.ChangFaFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.CosplayFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.FeiZhuLiuFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.KeAiFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.LuoLiFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.QiZhiFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.WangLuoFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.WeiMeiFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.XiaoHuaFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.XiaoQingXinFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.SuYanFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.ZhaiNanNvShenFragment;

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
                    fragment = new XiaoHuaFragment();
                    break;
                case 1:
                    fragment = new XiaoQingXinFragment();
                    break;
                case 2:
                    fragment = new KeAiFragment();
                    break;
                case 3:
                    fragment = new SuYanFragment();
                    break;
                case 4:
                    fragment = new WangLuoFragment();
                    break;
                case 5:
                    fragment = new ZhaiNanNvShenFragment();
                    break;
                case 6:
                    fragment = new WeiMeiFragment();
                    break;
                case 7:
                    fragment = new ChangFaFragment();
                    break;
                case 8:
                    fragment = new QiZhiFragment();
                    break;
                case 9:
                    fragment = new LuoLiFragment();
                    break;
                case 10:
                    fragment = new CosplayFragment();
                    break;
                case 11:
                    fragment = new FeiZhuLiuFragment();
                    break;
            }
            //3将fragment存进数组
            fragmentArray.put(position, fragment);
        }
        return fragment;
    }
}
