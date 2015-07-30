package com.huntor.mscrm.app2.utils;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

/**
 * DES加密的工具类
 */
public class DES {

    /**
     * 获取加密密钥，长度不能小于8位
     * 
     * @param key
     *            加密私钥，长度不能够小于8位
     */
    private static byte[] getByte(String key) throws UnsupportedEncodingException {
        byte[] keyByte = key.getBytes("GBK");
        // 创建一个空的八位数组,默认情况下为0
        byte[] genKeyBtye = new byte[8];
        genKeyBtye[7] = genKeyBtye[6] = 0;
        // 将用户指定的规则转换成八位数组
        for (int i = 0; i < genKeyBtye.length && i < keyByte.length; i++) {
            genKeyBtye[i] = keyByte[i];
        }
        return genKeyBtye;
    }

    /**
     * 使用DES加密，并对结果进行Base64编码
     * 
     * @param encryptString
     *            要加密的字串
     * @param encryptKey
     *            密钥
     * @return 使用DES加密并进行Base64编码的字串
     */
    public static String encryptDES(String encryptString, String encryptKey) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(getByte(encryptKey));
        // 生成加密用的密钥
        SecretKeySpec key = new SecretKeySpec(getByte(encryptKey), "DES");
        // Cipher对象完成实际的加密操作
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        // 执行加密操作，要加密的字串使用GBK编码
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes("GBK"));
        // 对加密后的字串进行Base64编码
        return Base64.encodeToString(encryptedData, Base64.DEFAULT);
    }

    /**
     * 对字串进行Base64解码，然后进行DES解密
     * 
     * @param decryptString
     *            要解密的字串
     * @param decryptKey
     *            密钥
     * @return Base64解码后并进行DES解密后的字串
     */
    public static String decryptDES(String decryptString, String decryptKey) throws Exception {
        // 使用Base64对字串进行解码
        byte[] byteMi = Base64.decode(decryptString, Base64.DEFAULT);
        IvParameterSpec zeroIv = new IvParameterSpec(getByte(decryptKey));
        // 生成解密用的密钥
        SecretKeySpec key = new SecretKeySpec(getByte(decryptKey), "DES");
        // Cipher对象完成实际的解密操作
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        // 执行解密操作
        byte decryptedData[] = cipher.doFinal(byteMi);
        // 将解密后的字串转成GBK编码
        return new String(decryptedData, "GBK");
    }

}