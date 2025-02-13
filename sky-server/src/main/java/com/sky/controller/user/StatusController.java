package com.sky.controller.user;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2025/2/12
 * 说明:
 */
@RestController
@RequestMapping("/user/shop")
@Slf4j
public class StatusController {
    public static final String SHOP_STATUS = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Integer o = (Integer) redisTemplate.opsForValue().get(SHOP_STATUS);
        log.info("获取店铺状态{}",o==1?"营业中":"休息中");
        return Result.success(o);
    }
}
