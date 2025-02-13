package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2025/2/13
 * 说明:
 */
@RestController
@RequestMapping("/user/dish")
@Slf4j
public class DishUserController {
    @Autowired
    private DishService dishService;

    @GetMapping("/list")
    public Result<List<DishVO>> getDishList(@RequestParam("categoryId") Long categoryId){
        List<DishVO> dishVOS = dishService.queryDishByCategoryId(categoryId);
        log.info("dishVOS: {}", dishVOS);
        return Result.success(dishVOS);
    }

}
