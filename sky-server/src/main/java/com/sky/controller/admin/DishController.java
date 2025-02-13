package com.sky.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.PageDTO;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.utils.JwtUtil;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2025/2/11
 * 说明:菜品controller
 */
@RestController
@Slf4j
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;

//    添加菜品
    @PostMapping
    public Result addDish(@RequestBody DishDTO dishDTO, @RequestHeader("token")String token){
        Long id = JwtUtil.getIdFromToken(token, JwtClaimsConstant.EMP_ID);
        boolean isSuccess = dishService.addDish(dishDTO,id);
        return isSuccess? Result.success() : Result.error("添加失败");
    }

//    分页查询
    @GetMapping("/page")
    public Result<PageDTO<DishVO>> queryPage(DishPageQueryDTO queryDTO){
        return Result.success(dishService.queryPage(queryDTO));
    }
//    批量删除
    @DeleteMapping
    public Result deleteDishByIds(@RequestParam List<Long> ids){
        boolean b = dishService.removeBatchByIds(ids);
        return b? Result.success() : Result.error("删除失败");
    }

//    根据id查询
    @GetMapping("/{id}")
    public Result<DishVO> queryDishById(@PathVariable Long id) {
        DishVO dishVO=dishService.queryDishById(id);
        return dishVO==null? Result.error("未找到该菜品") : Result.success(dishVO);
    }
//    根据分类查询
    @GetMapping("/list")
    public Result<List<DishVO>> queryDishByCategoryId(@RequestParam Long categoryId) {
        List<DishVO> list=dishService.queryDishByCategoryId(categoryId);
        return Result.success(list);
    }
//    修改菜品
    @PutMapping
    public Result updateDish(@RequestBody DishDTO dishDTO, @RequestHeader("token")String token){
        Long id = JwtUtil.getIdFromToken(token,JwtClaimsConstant.EMP_ID);
        boolean isSuccess= dishService.updateDish(dishDTO,id);
        return isSuccess? Result.success() : Result.error("修改失败");
    }
//    修改状态
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, @RequestParam Long id,
                               @RequestHeader("token")String token){
        Long id1 = JwtUtil.getIdFromToken(token, JwtClaimsConstant.EMP_ID);
        log.info("修改菜品状态,id:{},status:{},userId:{}",id,status,id1);
        boolean isSuccess = dishService.updateDishStatus(status,id,id1);
        return isSuccess? Result.success() : Result.error("修改失败");
    }
}
