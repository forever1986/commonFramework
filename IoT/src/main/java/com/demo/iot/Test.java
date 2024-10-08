package com.demo.iot;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nimbusds.jose.JWSObject;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Base64;

public class Test {

    public static void main(String[] args) throws ParseException {
//        String basicPlainText = new String(Base64.getDecoder().decode("YWRtaW4td2ViOjByUGF3TnoyY0hiSzZkSStkcEZHbnc9PQ==".getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
//        String clientId = basicPlainText.split(":")[0];
//        System.out.println(clientId);

//        System.out.println(String.join("☆", "username", "tenantCode", "loginPlat"));

        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJkZXB0TmFtZSI6bnVsbCwidXNlcl9uYW1lIjoiYWRtaW4iLCJ1c2VyUGhvbmUiOiIxNTgzNDIzODk3NiIsInNvdXJjZSI6IiIsInR5cGUiOiJwbGF0IiwidGVuYW50LWlkIjoxLCJjbGllbnRfaWQiOiJhZG1pbi13ZWIiLCJwb3J0YWxBY2NvdW50IjpudWxsLCJpc1Bhc3N3b3JkRXhwaXJlIjpmYWxzZSwic2NvcGUiOlsiYWxsIl0sImV4cCI6MTcyNzcwNzc1NywianRpIjoiNTU2Zjc0NjctZjZhOS00OTlmLWI0MTktMDFlNGM3MzhkMTMwIiwiZW50ZXJwcmlzZUNvZGUiOm51bGwsImlzRmlyc3RMb2dpbiI6ZmFsc2UsImRlcHRJZCI6bnVsbCwic3lzQWNjb3VudCI6ImFkbWluIiwidGVuYW50LWNvZGUiOiJUMDAwMDAxIiwidXNlcklkIjoiMSIsInRlbmFudC1uYW1lIjoi5Lit5YyW6ZuG5Zui77yI5byD55So77yJIiwicG9ydGFsVXNlclV1aWQiOiJzdXBlcmFkbWluIiwiY2hpbmVzZU5hbWUiOiJhZG1pbiIsInVzZXJVdWlkIjoiMSIsImFjY291bnQiOiJhZG1pbiIsInVzZXJuYW1lIjoiYWRtaW4iLCJzdGF0dXMiOjF9.EBWlZ7g-lz3cnpLuE6uUdoSF4udNTVrj4_Xc_GQ_oAEVVEUvtlyP88EgWC1f7DuhuCNZd00rb2Je6cICZ1rMl4Sl11rGtthi2ZB7lKJnuV1lcOhTbcwBZG9PpJA5npdlF4R3GPDzK76fq0UpdioTq6E0O3_Cg_KxtBqpOjwmihukyML0_3nHU6mhktVDJcnF4bm48eOWmZyiXaqAwpt91FsNEB4pGXP_BhRNDSnfQ9cWY22UI2_2-I8Wu0rwxuQngbbIGVcxZcOqT5q9HgVI3OqcnmKHyY2SpLraxoNfT1gSNKpK2hkoKj3TW4rHNhmLBABk3mZnSbNh4e4zguC7nQ";
        String payload = StrUtil.toString(JWSObject.parse(token).getPayload());
        JSONObject jsonObject = JSONUtil.parseObj(payload);
        System.out.println(jsonObject);
    }
}
