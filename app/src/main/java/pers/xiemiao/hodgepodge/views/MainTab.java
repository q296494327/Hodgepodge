package pers.xiemiao.hodgepodge.views;


import pers.xiemiao.hodgepodge.R;
import pers.xiemiao.hodgepodge.fragment.DefauleFragment;
import pers.xiemiao.hodgepodge.fragment.jokefragment.JokeFragment;

/**
 * 使用枚举的方式初始化tabhost所需标签
 * 封装所需要的标签内容(名称\图标\绑定的fragment)
 */
public enum MainTab {

    JOKE(0, "笑话", R.drawable.tab_icon_joke, JokeFragment.class),
    TWO(1, "待开发1", R.drawable.tab_icon_joke, DefauleFragment.class),
    QUICK(2, "快速启动", R.drawable.tab_icon_joke, null),
    THREE(3, "待开发2", R.drawable.tab_icon_joke, DefauleFragment.class),
    FOUR(4, "待开发3", R.drawable.tab_icon_joke, DefauleFragment.class);

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
