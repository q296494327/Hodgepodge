package pers.xiemiao.hodgepodge.activity;


import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.fragment.DefauleFragment;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.BeautyFragment;
import pers.xiemiao.hodgepodge.fragment.jokefragment.JokeFragment;
import pers.xiemiao.hodgepodge.fragment.newsfragment.NewsFragment;

/**
 * 使用枚举的方式初始化tabhost所需标签
 * 封装所需要的标签内容(名称\图标\绑定的fragment)
 */
public enum MainTab {

    JOKE(0, "轻松一刻", R.drawable.selector_maintab_icon_joke, JokeFragment.class),
    News(1, "新闻5分钟", R.drawable.selector_maintab_icon_news, NewsFragment.class),
    QUICK(2, "快速启动", R.drawable.selector_maintab_icon_joke, null),
    BEAUTY(3, "动感萌妹子", R.drawable.selector_maintab_icon_beauty, BeautyFragment.class),
    FOUR(4, "待开发3", R.drawable.selector_maintab_icon_joke, DefauleFragment.class);

    public int idx;
    public String name;
    public int icon;
    public Class<?> clz;

    private MainTab(int idx, String name, int icon, Class<?> clz) {
        this.idx = idx;
        this.name = name;
        this.icon = icon;
        this.clz = clz;
    }
}
