package pers.xiemiao.hodgepodge.utils;

import opensource.jpinyin.PinyinFormat;
import opensource.jpinyin.PinyinHelper;

public class PinyinUtil {
    public static String getPinyin(String str) {
        str = str.substring(0, 2);
        return PinyinHelper.convertToPinyinString(str, "", PinyinFormat.WITHOUT_TONE);
    }
}
