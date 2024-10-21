# common-redis
是一个集成redis的公共包
## 1 redis配置
主要做redis配置。redis有2种不同的template（2种的key不能共享）  
1.StringRedisTemplate：以String作为存储方式：默认使用StringRedisTemplate，其value都是以String方式存储  
2.RedisTemplate：  
&ensp;&ensp;1）使用默认RedisTemplate时，其value都是根据jdk序列化的方式存储  
&ensp;&ensp;2）自定义Jackson2JsonRedisSerializer序列化，以json格式存储，其key与StringRedisTemplate共享，返回值是LinkedHashMap  
&ensp;&ensp;3）自定义GenericJackson2JsonRedisSerializer序列化，以json格式存储，其key与StringRedisTemplate共享，返回值是原先对象(因为保存了classname)

## 2 本案例配置
1.默认配置RedisTemplate和StringRedisTemplate，其中RedisTemplate的序列化是Jackson2JsonRedisSerializer。  
2.同时使用@ConditionalOnMissingBean({RedisTemplate.class})注解，使得引用该子模块也可以自定义自己的Template