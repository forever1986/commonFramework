package com.demo.route.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.nimbusds.jose.JWSObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;

public class KeyPairUtil {

    public static void main(String[] args) throws Exception {
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiYXV0aC1yZXNvdXJjZTEiXSwidXNlcl9uYW1lIjoibGluIiwiYXV0aG9yIjoibGlubW9vIiwic2NvcGUiOlsiYWxsIl0sInVzZXJEZXRhaWxzIjp7InVzZXIiOnsiaWQiOjEsInVzZXJuYW1lIjoibGluIiwicGFzc3dvcmQiOm51bGwsImVtYWlsIjpudWxsLCJwaG9uZSI6IjEyMzQ1Njc4OTEwIn0sInBhc3N3b3JkIjpudWxsLCJlbmFibGVkIjp0cnVlLCJjcmVkZW50aWFsc05vbkV4cGlyZWQiOnRydWUsImFjY291bnROb25FeHBpcmVkIjp0cnVlLCJhY2NvdW50Tm9uTG9ja2VkIjp0cnVlLCJhdXRob3JpdGllcyI6bnVsbCwidXNlcm5hbWUiOiJsaW4ifSwianRpIjoiZDljZThmNTgtMDU2Zi00Y2U0LWI0ZDEtODJkZGIyYWQyMjRjIiwiY2xpZW50X2lkIjoiY2xpZW50LWxxIn0.CMuaDpTOxKYr75x9DyZI1USTxxa-zbQ7k6u7R52y5eeHuuQPsSZpq1Dsb_4MUAW8xzfG-aDKpttziHJqEywlirvhoVWGV2KNy_bM9q027Mx_dOuJ_FdYGhcqsUAEhJdB-4TkGnaZySmPNjvCOR7wLmTxslYcKVLLlmEEvzw2Aiw8v35EyW6WMYKOROVJ0CzvSiHMnsboaRBsuSSom4W3-KBXgRByjD2QIfiCfUKNsiI_E5p6SGtDROoaexaMNW_OlMLIH52ku0YRVGogtIP2AX8_do1DpOvGd278r--4ZcAdAf7EbGP8pnTcfffB6rGOSnaojJPZyEw4nmCXOl9PRA";
        String payload = StrUtil.toString(JWSObject.parse(token).getPayload());
        NimbusReactiveJwtDecoder nimbusReactiveJwtDecoder = new NimbusReactiveJwtDecoder(rsaPublicKey());
        nimbusReactiveJwtDecoder.decode(token).subscribe();
        System.out.println(payload);
    }

    public static RSAPublicKey rsaPublicKey() throws Exception{
        Resource resource = new ClassPathResource("public.key");
        InputStream is = resource.getInputStream();
        String publicKeyData = IoUtil.read(is).toString();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKeyData));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey)keyFactory.generatePublic(keySpec);

//        byte[] certder = Base64.decode(publicKeyData);
//        InputStream certstream = new ByteArrayInputStream(certder);
//        Certificate cert = CertificateFactory.getInstance("X.509").generateCertificate(certstream);
//        PublicKey key = cert.getPublicKey();
//        return (RSAPublicKey)key;
    }
}
