package com.demo.redis.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class RedisLockController {


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedissonClient redisson;


    private static final String REDIS_KEY = "inventory_product001"; //库存的key

    private static final int MAX_SIZE = 1000;

    /**
     * 初始化库存
     */
    @GetMapping("/redisLock/init")
    public Boolean init() {
        // 使用setIfAbsent方法设置key
        Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(REDIS_KEY, String.valueOf(MAX_SIZE));
        // 设置成功后，设置过期
        if(result!=null&&result.booleanValue()){
            stringRedisTemplate.expire(REDIS_KEY, 2 ,TimeUnit.MINUTES);
        }
        stringRedisTemplate.hasKey(REDIS_KEY);
        return result;
    }

    /**
     * 删除库存
     */
    @GetMapping("/redisLock/delete")
    public Boolean delete() {
        Boolean result = stringRedisTemplate.delete(REDIS_KEY);
        return result;
    }

    /**
     * 扣库存业务
     */
    @GetMapping("/redisLock/exportInventory")
    public void exportInventory() throws InterruptedException {
        int num = 0;
        int confirm_num = 0;
        while (num < 300){
            String lockKey = "product001";
            RLock lock = redisson.getLock(lockKey);
            try {
                lock.lock();
                int s = Integer.parseInt(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(REDIS_KEY)));
                s--;
                log.info("库存当前为：" + s + "");
                stringRedisTemplate.opsForValue().set(REDIS_KEY, String.valueOf(s));
            } catch (Exception e) {
            } finally {
                lock.unlock();
            }
            Thread.sleep(10);
            num++;
            confirm_num++;
        }
        log.info("最终结果"+confirm_num);

    }
}
