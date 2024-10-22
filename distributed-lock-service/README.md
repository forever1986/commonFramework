# distributed-lock-service
一个演示分布式锁的demo项目

# 1 理论知识
## 1.1 分布式锁的作用
在数据库，我们对一个资源进行操作，比如更新一行数据，那么数据库根据你设置的事务级别，一般都会对其加锁，加锁的原因其实就是怕并发操作时，避免脏数据。  
而在不同服务之间，其锁的概念也是有的。比如我们为了避免前端重复点击，一般会给前端返回一个key，然后前端提交数据时，将key返回给后端，后端验证是否同时有同一个key多个请求，如果存在则返回重复操作。
## 1.2 分布式锁的实现方式
常见的分布式锁实现有以下几种方式：  
1）基于数据库实现分布式锁  
2）基于zookeeper实现分布式锁  
3）基于redis实现分布式锁  

从理解的难易程度角度（从低到高） ：数据库 > 缓存 > Zookeeper  
从实现的复杂性角度（从低到高）：Zookeeper >= 缓存 > 数据库  
从性能角度（从高到低）：缓存 > Zookeeper >= 数据库  
从可靠性角度（从高到低）：Zookeeper > 缓存 > 数据库

# 2 代码实践
本案例中使用redis来实现分布式锁，同时引入redisson框架（该框架封装了基于redis的分布式锁，让我们非常方便使用。另外如zookeeper也有Curator框架）  
1）新建distributed-lock-service子模块，引入以下依赖：
```xml
<dependency>
    <groupId>org.example</groupId>
    <artifactId>common-redis</artifactId>
    <version>${project.version}</version>
</dependency>
<!--redisson中已经引入spring-starter-web，因此无需在引入 -->
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson-spring-boot-starter</artifactId>
    <version>3.12.5</version>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```
2）编写RedissonConfig，配置RedissonClient
```java
package com.demo.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean
    public RedissonClient getRedisson(){

        Config config = new Config();
        //单机模式  依次设置redis地址和密码
        config.useSingleServer().
                setAddress("redis://" + host + ":" + port);
//                setPassword(redisPassword);
        return Redisson.create(config);
    }
}

```
3）编写RedisLockController，实现一个扣取库存的分布式锁模拟场景  
4）通过启动2台服务器（记得修改接口），然后分别访问2台服务器的/redisLock/exportInventory接口，查看扣取库存日志是否正确
