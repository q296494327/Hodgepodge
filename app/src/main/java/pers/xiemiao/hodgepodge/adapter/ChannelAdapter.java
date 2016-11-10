package pers.xiemiao.hodgepodge.adapter;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.activity.ChannelActivity;
import pers.xiemiao.hodgepodge.bean.ChannelEntity;
import pers.xiemiao.hodgepodge.bean.MessageEvent;
import pers.xiemiao.hodgepodge.conf.Constants;
import pers.xiemiao.hodgepodge.touchhelper.OnDragVHListener;
import pers.xiemiao.hodgepodge.touchhelper.OnItemMoveListener;
import pers.xiemiao.hodgepodge.utils.SpUtil;
import pers.xiemiao.hodgepodge.utils.ToastUtils;
import pers.xiemiao.hodgepodge.utils.UIUtils;


/**
 * User: xiemiao
 * Date: 2016-11-10
 * Time: 18:25
 * Desc: 频道排序适配器
 */
public class ChannelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        OnItemMoveListener {
    // 我的频道 标题部分
    public static final int TYPE_MY_CHANNEL_HEADER = 0;
    // 我的频道
    public static final int TYPE_MY = 1;
    // 其他频道 标题部分
    public static final int TYPE_OTHER_CHANNEL_HEADER = 2;
    // 其他频道
    public static final int TYPE_OTHER = 3;

    // 我的频道之前的header数量  该demo中 即标题部分 为 1
    private static final int COUNT_PRE_MY_HEADER = 1;
    // 其他频道之前的header数量  该demo中 即标题部分 为 COUNT_PRE_MY_HEADER + 1
    private static final int COUNT_PRE_OTHER_HEADER = COUNT_PRE_MY_HEADER + 1;

    private static final long ANIM_TIME = 360L;

    // touch 点击开始时间
    private long startTime;
    // touch 间隔时间  用于分辨编辑模式下是否是 "点击"
    private static final long SPACE_TIME = 100;

    private LayoutInflater mInflater;
    private ItemTouchHelper mItemTouchHelper;

    // 是否为 编辑 模式
    private boolean isEditMode;

    private List<ChannelEntity> mMyChannelItems, mOtherChannelItems;
    private ChannelActivity mActivity;
    private int mTag;//标记是哪一个页面的频道

    // 我的频道点击事件
    private OnMyChannelItemClickListener mChannelItemClickListener;

    public ChannelAdapter(ChannelActivity activity, int tag, ItemTouchHelper helper,
                          List<ChannelEntity>
            mMyChannelItems, List<ChannelEntity> mOtherChannelItems) {
        mActivity = activity;
        mTag = tag;
        this.mInflater = LayoutInflater.from(activity);
        this.mItemTouchHelper = helper;
        this.mMyChannelItems = mMyChannelItems;
        this.mOtherChannelItems = mOtherChannelItems;
    }

    @Override//根据position的位置，返回不同类型的item
    public int getItemViewType(int position) {
        if (position == 0) {    // 我的频道 标题部分
            return TYPE_MY_CHANNEL_HEADER;
        } else if (position == mMyChannelItems.size() + 1) {    // 其他频道 标题部分
            return TYPE_OTHER_CHANNEL_HEADER;
        } else if (position > 0 && position < mMyChannelItems.size() + 1) {
            return TYPE_MY;
        } else {
            return TYPE_OTHER;
        }
    }

    @Override//根据类型去创建viewholder
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view;
        switch (viewType) {
            case TYPE_MY_CHANNEL_HEADER:
                view = mInflater.inflate(R.layout.item_my_channel_header, parent, false);
                final MyChannelHeaderViewHolder holder = new MyChannelHeaderViewHolder(view);
                holder.tvBtnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isEditMode) {
                            startEditMode((RecyclerView) parent);
                            holder.tvBtnEdit.setText(R.string.finish);
                        } else {
                            cancelEditMode((RecyclerView) parent);
                            holder.tvBtnEdit.setText(R.string.edit);
                            //同时将集合的结果存到sp里
                            saveChannelTitleToSp();
                            //点保存的时候发送通知去更新标题适配器等
                            MessageEvent event = new MessageEvent();
                            event.msg = mTag + "";
                            EventBus.getDefault().post(event);
                            mActivity.finish();
                        }
                    }
                });
                return holder;

            case TYPE_MY:
                view = mInflater.inflate(R.layout.item_my, parent, false);
                final MyViewHolder myHolder = new MyViewHolder(view);

                //设置我的频道textview点击事件，如果是编辑模式，点击后就移动到其他频道
                myHolder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        int position = myHolder.getAdapterPosition();
                        if (isEditMode) {
                            RecyclerView recyclerView = ((RecyclerView) parent);
                            //获取目标view，只用得到其他频道的第一个position就行了，即我的频道的总数+两个标题header数
                            View targetView = recyclerView.getLayoutManager().findViewByPosition
                                    (mMyChannelItems.size() + COUNT_PRE_OTHER_HEADER);
                            //获取当前view
                            View currentView = recyclerView.getLayoutManager().findViewByPosition
                                    (position);
                            // 如果targetView不在屏幕内,则indexOfChild为-1  此时不需要添加动画,
                            // 因为此时notifyItemMoved自带一个向目标移动的动画
                            // 如果targetView在屏幕内,则添加一个位移动画
                            if (recyclerView.indexOfChild(targetView) >= 0) {
                                int targetX, targetY;

                                RecyclerView.LayoutManager manager = recyclerView
                                        .getLayoutManager();
                                int spanCount = ((GridLayoutManager) manager).getSpanCount();

                                // 移动后 高度将变化 (我的频道Grid 最后一个item在新的一行第一个)
                                if ((mMyChannelItems.size() - COUNT_PRE_MY_HEADER) % spanCount ==
                                        0) {
                                    View preTargetView = recyclerView.getLayoutManager()
                                            .findViewByPosition(mMyChannelItems.size() +
                                                    COUNT_PRE_OTHER_HEADER - 1);
                                    targetX = preTargetView.getLeft();
                                    targetY = preTargetView.getTop();
                                } else {
                                    targetX = targetView.getLeft();
                                    targetY = targetView.getTop();
                                }

                                moveMyToOther(myHolder);
                                //开启一个位移动画
                                startAnimation(recyclerView, currentView, targetX, targetY);

                            } else {
                                moveMyToOther(myHolder);
                            }
                        } else {
                            //注意position要减去header数量
                            mChannelItemClickListener.onItemClick(v, position -
                                    COUNT_PRE_MY_HEADER);
                        }
                    }
                });

                //设置item长按得点击事件
                myHolder.textView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View v) {
                        if (!isEditMode) {
                            RecyclerView recyclerView = ((RecyclerView) parent);
                            startEditMode(recyclerView);

                            // header 按钮文字 改成 "完成"
                            View view = recyclerView.getChildAt(0);
                            if (view == recyclerView.getLayoutManager().findViewByPosition(0)) {
                                TextView tvBtnEdit = (TextView) view.findViewById(R.id.tv_btn_edit);
                                tvBtnEdit.setText(R.string.finish);
                            }
                        }
                        mItemTouchHelper.startDrag(myHolder);//开启拖动模式
                        return true;
                    }
                });

                //设置触摸事件监听
                myHolder.textView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (isEditMode) {
                            switch (MotionEventCompat.getActionMasked(event)) {
                                case MotionEvent.ACTION_DOWN:
                                    startTime = System.currentTimeMillis();//获取按下时间
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    //当开始移动时和按下时的间隔时间大于100毫秒时，才开启拖拽
                                    //这样是为了不冲突item的点击事件、RecyclerView的滚动事件
                                    if (System.currentTimeMillis() - startTime > SPACE_TIME) {
                                        mItemTouchHelper.startDrag(myHolder);
                                    }
                                    break;
                                case MotionEvent.ACTION_CANCEL:
                                case MotionEvent.ACTION_UP:
                                    startTime = 0;//抬起时归零
                                    break;
                            }

                        }
                        return false;
                    }
                });
                return myHolder;

            case TYPE_OTHER_CHANNEL_HEADER:
                view = mInflater.inflate(R.layout.item_other_channel_header, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };

            case TYPE_OTHER:
                view = mInflater.inflate(R.layout.item_other, parent, false);
                final OtherViewHolder otherHolder = new OtherViewHolder(view);
                otherHolder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecyclerView recyclerView = ((RecyclerView) parent);
                        //点击其他频道就开启编辑模式
                        startEditMode(recyclerView);
                        // header 按钮文字 改成 "完成"
                        View view = recyclerView.getChildAt(0);
                        if (view == recyclerView.getLayoutManager().findViewByPosition(0)) {
                            TextView tvBtnEdit = (TextView) view.findViewById(R.id.tv_btn_edit);
                            tvBtnEdit.setText(R.string.finish);
                        }

                        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                        int currentPiosition = otherHolder.getAdapterPosition();
                        // 如果RecyclerView滑动到底部,移动的目标位置的y轴 - height
                        View currentView = manager.findViewByPosition(currentPiosition);
                        // 目标位置的前一个item  即当前MyChannel的最后一个
                        View preTargetView = manager.findViewByPosition(mMyChannelItems.size() -
                                1 + COUNT_PRE_MY_HEADER);

                        // 如果targetView不在屏幕内,则为-1  此时不需要添加动画,因为此时notifyItemMoved自带一个向目标移动的动画
                        // 如果在屏幕内,则添加一个位移动画
                        if (recyclerView.indexOfChild(preTargetView) >= 0) {
                            int targetX = preTargetView.getLeft();
                            int targetY = preTargetView.getTop();

                            //
                            int targetPosition = mMyChannelItems.size() - 1 +
                                    COUNT_PRE_OTHER_HEADER;

                            GridLayoutManager gridLayoutManager = ((GridLayoutManager) manager);
                            int spanCount = gridLayoutManager.getSpanCount();
                            // target 在最后一行第一个
                            if ((targetPosition - COUNT_PRE_MY_HEADER) % spanCount == 0) {
                                View targetView = manager.findViewByPosition(targetPosition);
                                targetX = targetView.getLeft();
                                targetY = targetView.getTop();
                            } else {
                                targetX += preTargetView.getWidth();

                                // 最后一个item可见
                                if (gridLayoutManager.findLastVisibleItemPosition() ==
                                        getItemCount() - 1) {
                                    // 最后的item在最后一行第一个位置
                                    if ((getItemCount() - 1 - mMyChannelItems.size() -
                                            COUNT_PRE_OTHER_HEADER) % spanCount == 0) {
                                        // RecyclerView实际高度 > 屏幕高度 && RecyclerView实际高度 < 屏幕高度 +
                                        // item.height
                                        int firstVisiblePostion = gridLayoutManager
                                                .findFirstVisibleItemPosition();
                                        if (firstVisiblePostion == 0) {
                                            // FirstCompletelyVisibleItemPosition == 0 即 内容不满一屏幕
                                            // , targetY值不需要变化
                                            // // FirstCompletelyVisibleItemPosition != 0 即
                                            // 内容满一屏幕 并且 可滑动 , targetY值 + firstItem.getTop
                                            if (gridLayoutManager
                                                    .findFirstCompletelyVisibleItemPosition() !=
                                                    0) {
                                                int offset = (-recyclerView.getChildAt(0).getTop
                                                        ()) - recyclerView.getPaddingTop();
                                                targetY += offset;
                                            }
                                        } else { // 在这种情况下 并且 RecyclerView高度变化时(即可见第一个item的
                                            // position != 0),
                                            // 移动后, targetY值  + 一个item的高度
                                            targetY += preTargetView.getHeight();
                                        }
                                    }
                                } else {
                                    System.out.println("current--No");
                                }
                            }

                            // 如果当前位置是otherChannel可见的最后一个
                            // 并且 当前位置不在grid的第一个位置
                            // 并且 目标位置不在grid的第一个位置

                            // 则 需要延迟250秒 notifyItemMove , 这是因为这种情况 , 并不触发ItemAnimator , 会直接刷新界面
                            // 导致我们的位移动画刚开始,就已经notify完毕,引起不同步问题
                            if (currentPiosition == gridLayoutManager.findLastVisibleItemPosition()
                                    && (currentPiosition - mMyChannelItems.size() -
                                    COUNT_PRE_OTHER_HEADER) % spanCount != 0
                                    && (targetPosition - COUNT_PRE_MY_HEADER) % spanCount != 0) {
                                moveOtherToMyWithDelay(otherHolder);
                            } else {
                                moveOtherToMy(otherHolder);
                            }
                            startAnimation(recyclerView, currentView, targetX, targetY);

                        } else {
                            moveOtherToMy(otherHolder);
                        }
                    }
                });
                return otherHolder;
        }
        return null;
    }

    /**
     * 保存我的频道和其他频道的数据到SP里
     */
    private void saveChannelTitleToSp() {
        //1初始化我的频道字符串数组
        if (mMyChannelItems.size() > 0) {
            String[] myChannelArr = new String[mMyChannelItems.size()];
            for (int i = 0; i < mMyChannelItems.size(); i++) {
                myChannelArr[i] = mMyChannelItems.get(i).getName();
            }
            saveMyChannelForTag(myChannelArr);

        } else {
            ToastUtils.showToast("我的频道不能为空");
        }
        //2初始化其他频道字符串数组
        if (mMyChannelItems.size() > 0) {//只有当我的频道数量大于0的时候才去保存
            String[] otherChannelArr = new String[mOtherChannelItems.size()];
            for (int i = 0; i < mOtherChannelItems.size(); i++) {
                otherChannelArr[i] = mOtherChannelItems.get(i).getName();
            }
            saveOtherChannelForTag(otherChannelArr);
        }

    }

    /**
     * 保存其他频道通过Tag
     *
     * @param otherChannelArr
     */
    private void saveOtherChannelForTag(String[] otherChannelArr) {
        switch (mTag) {
            case 1:
                SpUtil.putStringArr(UIUtils.getContext(), Constants.JOKE_OTHER_TITLES,
                        otherChannelArr);
                break;
            case 2:
                SpUtil.putStringArr(UIUtils.getContext(), Constants.GHOST_OTHER_TITLES,
                        otherChannelArr);
                break;
            case 3:
                SpUtil.putStringArr(UIUtils.getContext(), Constants.GRIL_OTHER_TITLES,
                        otherChannelArr);
                break;
            case 4:
                SpUtil.putStringArr(UIUtils.getContext(), Constants.CARTOON_OTHER_TITLES,
                        otherChannelArr);
                break;
        }
    }

    /**
     * 保存我的频道通过Tag
     *
     * @param myChannelArr
     */
    private void saveMyChannelForTag(String[] myChannelArr) {
        switch (mTag) {
            case 1:
                SpUtil.putStringArr(UIUtils.getContext(), Constants.JOKE_TITLES, myChannelArr);
                break;
            case 2:
                SpUtil.putStringArr(UIUtils.getContext(), Constants.GHOST_TITLES, myChannelArr);
                break;
            case 3:
                SpUtil.putStringArr(UIUtils.getContext(), Constants.GRIL_TITLES, myChannelArr);
                break;
            case 4:
                SpUtil.putStringArr(UIUtils.getContext(), Constants.CARTOON_TITLES, myChannelArr);
                break;
        }
    }

    @Override//绑定viewholder
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //每次刷新UI都更新显示的文本以及编辑模式的隐藏显示
        if (holder instanceof MyViewHolder) {//如果holder属于我的频道

            MyViewHolder myHolder = (MyViewHolder) holder;
            myHolder.textView.setText(mMyChannelItems.get(position - COUNT_PRE_MY_HEADER).getName
                    ());
            if (isEditMode) {
                myHolder.imgEdit.setVisibility(View.VISIBLE);
            } else {
                myHolder.imgEdit.setVisibility(View.INVISIBLE);
            }

        } else if (holder instanceof OtherViewHolder) {

            ((OtherViewHolder) holder).textView.setText(mOtherChannelItems.get(position -
                    mMyChannelItems.size() - COUNT_PRE_OTHER_HEADER).getName());

        } else if (holder instanceof MyChannelHeaderViewHolder) {

            MyChannelHeaderViewHolder headerHolder = (MyChannelHeaderViewHolder) holder;
            if (isEditMode) {
                headerHolder.tvBtnEdit.setText(R.string.finish);
            } else {
                headerHolder.tvBtnEdit.setText(R.string.edit);
            }
        }
    }

    @Override
    public int getItemCount() {
        // 我的频道  标题 + 我的频道.size + 其他频道 标题 + 其他频道.size
        return mMyChannelItems.size() + mOtherChannelItems.size() + COUNT_PRE_OTHER_HEADER;
    }

    /**
     * 开始增删动画
     */
    private void startAnimation(RecyclerView recyclerView, final View currentView, float targetX,
                                float targetY) {
        final ViewGroup viewGroup = (ViewGroup) recyclerView.getParent();
        final ImageView mirrorView = addMirrorView(viewGroup, recyclerView, currentView);

        //获取一个当前view到目标view的位移动画
        Animation animation = getTranslateAnimator(
                targetX - currentView.getLeft(), targetY - currentView.getTop());
        currentView.setVisibility(View.INVISIBLE);//设置当前view隐藏
        mirrorView.startAnimation(animation);//使用镜像view开启动画

        //设置动画监听
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //当动画结束时，父容器就将镜像移除掉
                viewGroup.removeView(mirrorView);
                if (currentView.getVisibility() == View.INVISIBLE) {
                    //将当前view显示出来
                    currentView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 我的频道 移动到 其他频道
     *
     * @param myHolder
     */
    private void moveMyToOther(MyViewHolder myHolder) {
        int position = myHolder.getAdapterPosition();//获取当前被点击的item索引

        int startPosition = position - COUNT_PRE_MY_HEADER;//索引要减去头布局，才能正确在集合取值
        if (startPosition > mMyChannelItems.size() - 1) {
            return;
        }
        ChannelEntity item = mMyChannelItems.get(startPosition);//获取item实体
        mMyChannelItems.remove(startPosition);//从集合移除它
        mOtherChannelItems.add(0, item);//将它添加到其他频道的集合

        //刷新条目移动UI（移动到其他频道的第一位）
        notifyItemMoved(position, mMyChannelItems.size() + COUNT_PRE_OTHER_HEADER);
    }

    /**
     * 其他频道 移动到 我的频道
     *
     * @param otherHolder
     */
    private void moveOtherToMy(OtherViewHolder otherHolder) {
        int position = processItemRemoveAdd(otherHolder);
        if (position == -1) {
            return;
        }
        notifyItemMoved(position, mMyChannelItems.size() - 1 + COUNT_PRE_MY_HEADER);
    }

    /**
     * 其他频道 移动到 我的频道 伴随延迟
     *
     * @param otherHolder
     */
    private void moveOtherToMyWithDelay(OtherViewHolder otherHolder) {
        final int position = processItemRemoveAdd(otherHolder);
        if (position == -1) {
            return;
        }
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyItemMoved(position, mMyChannelItems.size() - 1 + COUNT_PRE_MY_HEADER);
            }
        }, ANIM_TIME);
    }

    private Handler delayHandler = new Handler();

    private int processItemRemoveAdd(OtherViewHolder otherHolder) {
        int position = otherHolder.getAdapterPosition();

        //索引要减去两个标题和我的频道的数量，才能正确从其他频道集合取到数据
        int startPosition = position - mMyChannelItems.size() - COUNT_PRE_OTHER_HEADER;
        if (startPosition > mOtherChannelItems.size() - 1) {
            return -1;
        }
        ChannelEntity item = mOtherChannelItems.get(startPosition);//获取item
        mOtherChannelItems.remove(startPosition);//移除它
        mMyChannelItems.add(item);//将它添加到我的频道
        return position;
    }


    /**
     * 添加需要移动的 镜像View
     */
    private ImageView addMirrorView(ViewGroup parent, RecyclerView recyclerView, View view) {
        /**
         * 我们要获取cache首先要通过setDrawingCacheEnable方法开启cache，然后再调用getDrawingCache方法就可以获得view的cache图片了。
         buildDrawingCache方法可以不用调用，因为调用getDrawingCache方法时，若果cache没有建立，系统会自动调用buildDrawingCache
         方法生成cache。
         若想更新cache, 必须要调用destoryDrawingCache方法把旧的cache销毁，才能建立新的。
         当调用setDrawingCacheEnabled方法设置为false, 系统也会自动把原来的cache销毁。
         */
        view.destroyDrawingCache();//首先销毁绘制的view缓存
        view.setDrawingCacheEnabled(true);//设置绘制view缓存
        final ImageView mirrorView = new ImageView(recyclerView.getContext());//创建image对象
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());//通过绘制的缓存创建bitmap
        mirrorView.setImageBitmap(bitmap);
        view.setDrawingCacheEnabled(false);//设置完imageview的图片后，设置不可绘制view的缓存
        int[] locations = new int[2];//创建一个2个长度的位置数组
        view.getLocationOnScreen(locations);//将view在屏幕上的位置存进数组
        int[] parenLocations = new int[2];//创建一个2个长度的父类位置数组
        recyclerView.getLocationOnScreen(parenLocations);//将父类recycleview在屏幕上的位置存进数组
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(bitmap.getWidth(), bitmap
                .getHeight());
        //设置镜像图片相对于屏幕的间距，这里top减去locations[1] - parenLocations[1]是为了控制镜像图片的位置相对于recycleview
        params.setMargins(locations[0], locations[1] - parenLocations[1], 0, 0);
        parent.addView(mirrorView, params);//添加到recycleview的父类容器

        return mirrorView;
    }

    @Override//实现条目移动排序的接口回调
    public void onItemMove(int fromPosition, int toPosition) {
        ChannelEntity item = mMyChannelItems.get(fromPosition - COUNT_PRE_MY_HEADER);
        mMyChannelItems.remove(fromPosition - COUNT_PRE_MY_HEADER);
        mMyChannelItems.add(toPosition - COUNT_PRE_MY_HEADER, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * 开启编辑模式
     *
     * @param parent
     */
    private void startEditMode(RecyclerView parent) {
        isEditMode = true;

        //遍历recycleview所有的孩子，如果有编辑模式的图片就显示出来
        int visibleChildCount = parent.getChildCount();
        for (int i = 0; i < visibleChildCount; i++) {
            View view = parent.getChildAt(i);
            ImageView imgEdit = (ImageView) view.findViewById(R.id.img_edit);
            if (imgEdit != null) {
                imgEdit.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 完成编辑模式
     *
     * @param parent
     */
    private void cancelEditMode(RecyclerView parent) {
        isEditMode = false;
        //遍历recycleview所有的孩子，如果有编辑模式的图片就隐藏
        int visibleChildCount = parent.getChildCount();
        for (int i = 0; i < visibleChildCount; i++) {
            View view = parent.getChildAt(i);
            ImageView imgEdit = (ImageView) view.findViewById(R.id.img_edit);
            if (imgEdit != null) {
                imgEdit.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 获取位移动画
     */
    private TranslateAnimation getTranslateAnimator(float targetX, float targetY) {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetX,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetY);
        // RecyclerView默认移动动画250ms 这里设置360ms 是为了防止在位移动画结束后 remove(view)过早 导致闪烁
        translateAnimation.setDuration(ANIM_TIME);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    public interface OnMyChannelItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnMyChannelItemClickListener(OnMyChannelItemClickListener listener) {
        this.mChannelItemClickListener = listener;
    }

    /**
     * 我的频道
     */
    class MyViewHolder extends RecyclerView.ViewHolder implements OnDragVHListener {
        private TextView textView;
        private ImageView imgEdit;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv);
            imgEdit = (ImageView) itemView.findViewById(R.id.img_edit);
        }

        /**
         * item 被选中时
         */
        @Override
        public void onItemSelected() {
            textView.setBackgroundResource(R.drawable.bg_channel_p);
        }

        /**
         * item 取消选中时
         */
        @Override
        public void onItemFinish() {
            textView.setBackgroundResource(R.drawable.bg_channel);
        }
    }

    /**
     * 其他频道
     */
    class OtherViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public OtherViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv);
        }
    }

    /**
     * 我的频道  标题部分
     */
    class MyChannelHeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBtnEdit;

        public MyChannelHeaderViewHolder(View itemView) {
            super(itemView);
            tvBtnEdit = (TextView) itemView.findViewById(R.id.tv_btn_edit);
        }
    }
}
