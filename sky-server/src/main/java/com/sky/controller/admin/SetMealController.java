package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.PageDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.utils.JwtUtil;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2025/2/11
 * 说明:套餐，涉及多个表
 */
@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetMealController {
    @Autowired
    private SetmealService setmealService;

//    添加套餐
    @PostMapping
    public Result addSetmeal(@RequestBody SetmealDTO setmealDTO,
                             @RequestHeader("token")String token){
        Long id = JwtUtil.getIdFromToken(token, JwtClaimsConstant.EMP_ID);
        boolean isSuccess = setmealService.addSetmeal(setmealDTO,id);
        return isSuccess? Result.success() : Result.error("添加失败");
    }
//    分页查询
    @GetMapping("/page")
    public Result<PageDTO<SetmealVO>> pageSetmeal(SetmealPageQueryDTO setmealPageQueryDTO){
        return Result.success(setmealService.pageSetmeal(setmealPageQueryDTO));
    }
//    修改套餐状态
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable("status") Integer status,
                               @RequestParam("id") Long id,
                               @RequestHeader("token")String token){
        Long userId = JwtUtil.getIdFromToken(token,JwtClaimsConstant.EMP_ID);
        boolean isSuccess = setmealService.updateStatus(status,id,userId);
        return isSuccess? Result.success() : Result.error("修改失败");
    }
//    批量删除
    @DeleteMapping
    public Result deleteSetmeal(@RequestParam("ids") List<Long> ids) {
        boolean isSuccess = setmealService.deleteByIds(ids);
        return isSuccess ? Result.success() : Result.error("删除失败");

    }
//根据id查询
    @GetMapping("/{id}")
    public Result<SetmealVO> getSetmealById(@PathVariable("id") Long id){
        return Result.success(setmealService.getSetmealById(id));
    }
//    修改套餐
    @PutMapping
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO,
                               @RequestHeader("token")String token){
        Long userId = JwtUtil.getIdFromToken(token,JwtClaimsConstant.EMP_ID);
        boolean isSuccess = setmealService.updateSetmeal(setmealDTO,userId);
        return isSuccess ? Result.success() : Result.error("修改失败");
    }


}
