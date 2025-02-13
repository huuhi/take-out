package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2025/2/12
 * 说明:
 */
@Configuration
@Slf4j
public class RedisConfiguration {
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始创建RedisTemplate");
        RedisTemplate redisTemplate = new RedisTemplate();
//        设置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
//            设置序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }


}
