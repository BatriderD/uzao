package com.zhaotai.uzao.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegUtils {
    private static String msg         = "";
    private static String regRepSpace = "\\s*|\t|\r|\n";

    public RegUtils() {
    }

    /**
     * <strong>删除字符串中的空格、换行符、Tab</strong><br>
     * <br>
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util RegUtils.java replaceSpace, 2016-03-03 13:13:00.083 Exp $
     *
     * @param str
     *        需要删除空格、换行符、Tab的源字符串
     *
     * @return String
     */
    public static String replaceSpace(String str) {
        return RegUtils.replace(str, regRepSpace, "");
    }

    /**
     * <strong>字符串替换</strong><br>
     * <ul>
     * <li>通过正则表达式匹配，匹配的部分将被替换</li>
     * </ul>
     * <br>
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util RegUtils.java replace, 2016-01-14 00:13:56.657 Exp $
     *
     * @param str
     *        需要替换的源字符串
     * @param reg
     *        正则表达式，匹配需要被替换的部分
     * @param replacement
     *        替换后的字符串
     * @return String
     */
    public static String replace(String str, String reg, String replacement) {
        String strReplace = "";

        try {
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(str);
            strReplace = matcher.replaceAll(replacement);
        } catch (PatternSyntaxException pse) {

        }

        return strReplace;
    }

    /**
     * <strong>判读字符串是否符合正则表达式</strong><br>
     * <ul>
     * <li>若符合正则表达式，则返回 true</li>
     * </ul>
     * <br>
     * @author Aaron.ffp
     * @version V1.0.0: autotest-base cn.ffp.autotest.base.util RegUtils.java reg, 2015-12-27 23:30:35.064 Exp $
     *
     * @param str
     *        源字符串
     * @param reg
     *        正则表达式
     * @return boolean
     */
    public static boolean reg(String str, String reg) {
        boolean match = false;

        try {
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(str);
            match = matcher.matches();
        } catch (PatternSyntaxException pse) {

        }

        return match;
    }
}