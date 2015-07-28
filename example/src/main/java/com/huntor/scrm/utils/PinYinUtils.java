package com.huntor.scrm.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

/**
 * 拼音工具类
 */
public class PinYinUtils {

    /**
     * 输入的字串中是否包含中文
     * 
     * @param inputString
     *            输入字串
     * @return 包含中文true，否则false
     */
    public static boolean containsChinese(String inputString) {
        char[] input = inputString.trim().toCharArray();
        for (int i = 0; i < input.length; i++) {
            if (Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     * 
     * @param inputString
     *            任意字符串
     * @return 将字符串中的中文转化为拼音,其他字符不变
     */
    public static String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = inputString.trim().toCharArray();
        String output = "";

        for (int i = 0; i < input.length; i++) {
            if (Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
                String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
                output += temp[0];
            } else {
                output += Character.toString(input[i]);
            }
        }
        return output;
    }

    /**
     * 获取汉字串拼音首字母，英文字符不变
     * 
     * @param chinese
     *            汉字串
     * @return 汉语拼音首字母
     */
    public static String getInitialSpell(String chinese) {
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                if (temp != null) {
                    pybf.append(temp[0].charAt(0));
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }

    /**
     * 获取汉字串拼音，英文字符不变
     * 
     * @param chinese
     *            汉字串
     * @return 汉语拼音
     */
    public static String getFullSpell(String chinese) {
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                if (temp != null) {
                    pybf.append(temp[0]);
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString();
    }
}
