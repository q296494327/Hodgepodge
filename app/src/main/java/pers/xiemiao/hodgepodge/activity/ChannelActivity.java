package pers.xiemiao.hodgepodge.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.adapter.ChannelAdapter;
import pers.xiemiao.hodgepodge.bean.ChannelEntity;
import pers.xiemiao.hodgepodge.bean.MessageEvent;
import pers.xiemiao.hodgepodge.conf.Constants;
import pers.xiemiao.hodgepodge.touchhelper.ItemDragHelperCallback;
import pers.xiemiao.hodgepodge.utils.SpUtil;

/**
 * User: xiemiao
 * Date: 2016-11-10
 * Time: 18:15
 * Desc: 频道管理Activity
 */
public class ChannelActivity extends AppCompatActivity {
    private RecyclerView mRecy;
    private ArrayList<String> mTitles;
    private int mTag;
    private int span=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        mRecy = (RecyclerView) findViewById(R.id.recy);
        init();
    }

    private void init() {
        //先获取传递过来的标题内容
        mTitles = getIntent().getStringArrayListExtra("TITLES");
        mTag = getIntent().getIntExtra("TAG", 1);
        //1初始化我的频道的内容
        final List<ChannelEntity> items = new ArrayList<>();
        for (String title : mTitles) {
            ChannelEntity entity = new ChannelEntity();
            entity.setName(title);
            items.add(entity);
        }

        //2初始化其他频道的内容
        //从sp里去获取其他频道的数据
        final List<ChannelEntity> otherItems = new ArrayList<>();
        initOtherChannel(otherItems);//初始化其他频道数据
        GridLayoutManager manager = null;
        switch (mTag) {
            case 1:
                span=1;
                manager = new GridLayoutManager(this, span);
                break;
            case 2:
                span=3;
                manager = new GridLayoutManager(this, span);
                break;
            case 3:
                span=3;
                manager = new GridLayoutManager(this, span);
                break;
            case 4:
                span=1;
                manager = new GridLayoutManager(this, span);
                break;
        }
        mRecy.setLayoutManager(manager);

        //1创建item拖拽回调类
        ItemDragHelperCallback callback = new ItemDragHelperCallback();
        //2将回调传递给ItemTouchHelper
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        //3绑定recycleview给ItemTouchHelper
        helper.attachToRecyclerView(mRecy);
        //创建recycleview适配器
        final ChannelAdapter adapter = new ChannelAdapter(ChannelActivity.this, mTag, helper,
                items, otherItems);
        //因为要在Grid布局里显示不同类型的item，所以这里要根据情况返回span的个数
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);//通过索引获取它的类型
                //如果类型是我的频道或者其他频道，那就返回span的个数为1，否则就是我们设置的span个数
                return viewType == ChannelAdapter.TYPE_MY || viewType == ChannelAdapter
                        .TYPE_OTHER ? 1 : span;
            }
        });
        mRecy.setAdapter(adapter);

        //设置我的频道点击监听
        adapter.setOnMyChannelItemClickListener(new ChannelAdapter.OnMyChannelItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                MessageEvent event = new MessageEvent();
                event.msg = "click" + mTag;
                event.value = items.get(position).getName();
                EventBus.getDefault().post(event);
                finish();
            }
        });
    }

    /**
     * 初始化其他频道数据
     *
     * @param otherItems
     */
    private void initOtherChannel(List<ChannelEntity> otherItems) {
        switch (mTag) {
            case 1:
                String[] otherArr1 = SpUtil.getStringArr(this, Constants.JOKE_OTHER_TITLES, null);
                if (otherArr1 != null) {
                    for (String other : otherArr1) {
                        ChannelEntity entity = new ChannelEntity();
                        entity.setName(other);
                        otherItems.add(entity);
                    }
                }
                break;
            case 2:
                String[] otherArr2 = SpUtil.getStringArr(this, Constants.GHOST_OTHER_TITLES, null);
                if (otherArr2 != null) {
                    for (String other : otherArr2) {
                        ChannelEntity entity = new ChannelEntity();
                        entity.setName(other);
                        otherItems.add(entity);
                    }
                }
                break;
            case 3:
                String[] otherArr3 = SpUtil.getStringArr(this, Constants.GRIL_OTHER_TITLES, null);
                if (otherArr3 != null) {
                    for (String other : otherArr3) {
                        ChannelEntity entity = new ChannelEntity();
                        entity.setName(other);
                        otherItems.add(entity);
                    }
                }
                break;
            case 4:
                String[] otherArr4 = SpUtil.getStringArr(this, Constants.CARTOON_OTHER_TITLES,
                        null);
                if (otherArr4 != null) {
                    for (String other : otherArr4) {
                        ChannelEntity entity = new ChannelEntity();
                        entity.setName(other);
                        otherItems.add(entity);
                    }
                }
                break;
        }
    }

}
