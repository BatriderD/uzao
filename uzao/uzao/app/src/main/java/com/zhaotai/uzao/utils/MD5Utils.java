package com.zhaotai.uzao.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Dotions 2015年7月1日 上午11:50:29
 */
public class MD5Utils {
    private static final String ENCODING_ALGORITHM = "MD5";
    // 全局数组
    private final static String[] strDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    private static byte[] md5sum(byte[] data) {
        try {
            MessageDigest mdTemp = MessageDigest.getInstance(ENCODING_ALGORITHM);
            mdTemp.update(data);
            return mdTemp.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    } /* 将data数组转换为16进制字符串 */

    private static String convertToHexString(byte data[]) {
        StringBuilder strBuffer = new StringBuilder();
        for (byte b : data) {
            strBuffer.append(Integer.toHexString(0xff & b));
        }
        return strBuffer.toString();
    }

    private static byte[] md5sum(File file) {
        InputStream fis = null;
        byte[] buffer = new byte[1024];
        int numRead;
        MessageDigest md5;
        try {
            fis = new FileInputStream(file);
            md5 = MessageDigest.getInstance(ENCODING_ALGORITHM);
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            return md5.digest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getMD5(String strObj) {
        String resultString = "";
        if (TextUtils.isEmpty(strObj))
            return resultString;
        try {
            resultString = strObj;
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }


    /**
     * 获取文件的MD5值
     */
    public static String getMD5(File file) {
        if (file != null && file.exists()) return convertToHexString(md5sum(file));
        else return null;
    }
}
