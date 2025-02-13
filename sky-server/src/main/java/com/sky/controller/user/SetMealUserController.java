package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2025/2/13
 * 说明:
 */
@RestController
@RequestMapping("/user/setmeal")
@Slf4j
public class SetMealUserController {
    @Autowired
    private SetmealService service;

    @GetMapping("/list")
    public Result<SetmealVO> getSetmealById(@RequestParam("categoryId") Long categoryId){
        return Result.success(service.getSetmealById(categoryId));
    }

    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> getDishById(@PathVariable("id") Long id){
       List<DishItemVO>   dishItemVOList =  service.getDishById(id);
        return Result.success(dishItemVOList);
    }


}
