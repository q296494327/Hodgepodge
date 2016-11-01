package pers.xiemiao.hodgepodge.activity;


import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.fragment.beautyfragment.BeautyTabFragment;
import pers.xiemiao.hodgepodge.fragment.cartoonfragment.CartoonTabFragment;
import pers.xiemiao.hodgepodge.fragment.jokefragment.JokeTabFragment;
import pers.xiemiao.hodgepodge.fragment.ghostfragment.GhostTabFragment;

/**
 * 使用枚举的方式初始化tabhost所需标签
 * 封装所需要的标签内容(名称\图标\绑定的fragment)
 */
public enum MainTab {

    JOKE(0, "轻松一刻", R.drawable.selector_maintab_icon_joke, JokeTabFragment.class),
    News(1, "三更有鬼", R.drawable.selector_maintab_icon_news, GhostTabFragment.class),
    QUICK(2, "快速启动", R.drawable.selector_maintab_icon_joke, null),
    BEAUTY(3, "动感萌妹", R.drawable.selector_maintab_icon_beauty, BeautyTabFragment.class),
    CARTOON(4, "动感漫画", R.drawable.selector_maintab_icon_cartoon, CartoonTabFragment.class);

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
