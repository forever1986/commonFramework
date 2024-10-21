package com.demo.manage.biz.service;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import com.demo.manage.biz.entity.TUser;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.List;

@Service
public class TestRedisService {

    @Autowired
    RedisTemplate redisTemplate;

    public void testObject() {
        String KEY = "my-object";
        TUser user =  new TUser();
        user.setId(1L);
        user.setUsername("linmoo");
        redisTemplate.opsForValue().set(KEY, user);
        Object newObject = redisTemplate.opsForValue().get(KEY);
        System.out.println(newObject);
    }

    public void testByte() throws IOException {
        String KEY = "my-object";
        String old_file = "Redis-test.docx";
        String new_file = "Redis-test-new.docx";
        InputStream inputStream = readOldFile(old_file);
        byte[] bytes = IOUtils.toByteArray(inputStream);
        inputStream.close();
        //数据编码
        String data = Base64.getEncoder().encodeToString(bytes);
        redisTemplate.opsForValue().set(KEY, data);
        Object newObject = redisTemplate.opsForValue().get(KEY);
        byte[] newBytes = Base64.getDecoder().decode((String)newObject);
        System.out.println(newObject);
        saveNewFile(newBytes, new_file);
    }

    public InputStream readOldFile(String fileName) throws IOException {
        List<URL> resources = ResourceUtil.getResources(fileName);
        URL resource = resources.get(0);
        return resource.openStream();
    }

    private void saveNewFile(byte[] newBytes, String new_file) throws IOException {
        String jarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(jarPath+"/"+ new_file);
        if(!file.exists()){
            file.createNewFile();
        }
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(newBytes);
        outputStream.close();
    }
}
