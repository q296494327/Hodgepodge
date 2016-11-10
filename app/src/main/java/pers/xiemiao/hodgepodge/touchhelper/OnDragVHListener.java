package pers.xiemiao.hodgepodge.touchhelper;

/**
 * User: xiemiao
 * Date: 2016-11-10
 * Time: 18:25
 * Desc: item被选中和取消被选中时的监听接口
 */
public interface OnDragVHListener {
    /**
     * Item被选中时触发
     */
    void onItemSelected();


    /**
     * Item在拖拽结束/滑动结束后触发
     */
    void onItemFinish();
}
