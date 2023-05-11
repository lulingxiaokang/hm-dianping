package com.hmdp.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: RedissonConfig
 * Package: com.hmdp.config
 *
 * @author 庐陵小康
 * @version 1.0
 * @Desc
 * @Date 2023/5/11 15:59
 */
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient(){
        //配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.156.100:6379")
                .setPassword("202304");
        //创建RedissonClient对象
        return Redisson.create(config);
    }

}
