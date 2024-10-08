package com.demo.auth.authentication.jwt;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;

public class RSAkeypair {

    public static void main(String[] args) throws Exception {
        // 加载JKS文件
        String keystorePath = "E:/demo.jks";
        String password = "linmoo"; // 替换为实际密码
        KeyStore keyStore = KeyStore.getInstance("JKS");
        FileInputStream fis = new FileInputStream(keystorePath);
        keyStore.load(fis, password.toCharArray());

        // 获取证书并提取公钥
        Certificate cert = keyStore.getCertificate("demo");
        PublicKey publicKey = cert.getPublicKey();

        // 打印公钥
        System.out.println(publicKey.getFormat());
        System.out.println(publicKey.getAlgorithm());
        System.out.println(publicKey.getEncoded());
        System.out.println(publicKey);
        System.out.println("===============");
        System.out.println("Public Key: " + Base64.encode(publicKey.toString().getBytes(StandardCharsets.UTF_8)));
    }


}