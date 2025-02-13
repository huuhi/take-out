package com.sky.controller.admin;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2025/2/11
 * 说明:
 */
@RestController
@RequestMapping("/admin/shop")
@Slf4j
public class ShopController {
    public static final String SHOP_STATUS = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;
    @PutMapping("/{status}")
    public Result updateStatus(@PathVariable("status") Integer status) {
//        存储到redis里面
        log.info("设置店铺状态为:{}", status==1? "营业中" : "休息中");
        redisTemplate.opsForValue().set(SHOP_STATUS, status);
        return Result.success();
    }
    @GetMapping("/status")
    public Result<Integer> getShopStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(SHOP_STATUS);
        return Result.success(status);
    }
}
