package com.demo.manage.biz.utils;

import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;

public class NacosSecretUtil {

    public static void main(String[] args) {
        // 自定义生成JWT令牌的密钥
        String nacosSecret = "commonframework_nacos_screct_20240923";
        // 输出密钥长度，要求不得低于32字符，否则无法启动节点。
        System.out.println("密钥长度》》》" + nacosSecret.length());
        // 密钥进行Base64编码
        byte[] data = nacosSecret.getBytes(StandardCharsets.UTF_8);
        System.out.println("密钥Base64编码》》》" + Base64Utils.encodeToString(data));
    }

}
