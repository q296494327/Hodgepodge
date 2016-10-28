package pers.xiemiao.hodgepodge.utils;

import opensource.jpinyin.PinyinFormat;
import opensource.jpinyin.PinyinHelper;

public class PinyinUtil {
    public static String getPinyin(String str) {
        str = str.substring(0, 2);
        return PinyinHelper.convertToPinyinString(str, "", PinyinFormat.WITHOUT_TONE);
    }

    public static String getPinyin(String str1, String str2) {
        return PinyinHelper.convertToPinyinString(str1 + str2, "", PinyinFormat.WITHOUT_TONE);
    }
}
