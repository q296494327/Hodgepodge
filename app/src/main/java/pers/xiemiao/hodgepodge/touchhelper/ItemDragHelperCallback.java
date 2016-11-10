package pers.xiemiao.hodgepodge.touchhelper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;


/**
 * User: xiemiao
 * Date: 2016-11-10
 * Time: 18:25
 * Desc: item拖拽监听回调
 */
public class ItemDragHelperCallback extends ItemTouchHelper.Callback {

    @Override//控制移动方向的回调
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags;//初始化拖拽Flags
        //获取到recycleview的布局管家
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        //如果布局管家属于网格或者瀑布流，就设置可以上下左右拖拽
        if (manager instanceof GridLayoutManager || manager instanceof StaggeredGridLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        } else {
            //否则就只能上下拖拽
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        }
        // 如果想支持滑动(删除)操作, swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override//控制item从哪个位置移动到了哪个位置的回调
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        // 不同Type之间不可移动
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        //如果recyclerView的适配器实现了条目移动监听器，就在移动时回调给适配器知晓
        if (recyclerView.getAdapter() instanceof OnItemMoveListener) {
            OnItemMoveListener listener = ((OnItemMoveListener) recyclerView.getAdapter());
            listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        return true;
    }

    @Override//处理滑动操作的回调
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override//item被选中时的回调
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // 不在闲置状态
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            //如果viewholder实现了拖拽监听，就在此处回调给viewholder知晓
            if (viewHolder instanceof OnDragVHListener) {
                OnDragVHListener itemViewHolder = (OnDragVHListener) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override//item被取消选中时的回调
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //如果viewholder实现了拖拽监听，就在此处回调给viewholder知晓
        if (viewHolder instanceof OnDragVHListener) {
            OnDragVHListener itemViewHolder = (OnDragVHListener) viewHolder;
            itemViewHolder.onItemFinish();
        }
        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        // 不支持长按拖拽功能 手动控制
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        // 不支持滑动功能
        return false;
    }
}
