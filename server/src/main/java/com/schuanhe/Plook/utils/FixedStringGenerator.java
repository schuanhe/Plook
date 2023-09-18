package com.schuanhe.Plook.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class FixedStringGenerator {
    public static String generateFixedString(String input) {
        try {
            // 将input字符串进行md5加密
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            // 将md5Bytes数组中的每个字节转换为16进制
            StringBuilder stringBuilder = new StringBuilder();
            for (byte md5Byte : md5Bytes) {
                int val = ((int) md5Byte) & 0xff;
                if (val < 16) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(Integer.toHexString(val));
            }
            // 返回固定长度的字符串
            return stringBuilder.substring(0, 8);
        } catch (NoSuchAlgorithmException e) {
            // 处理异常
            return null;
        }
    }
}
