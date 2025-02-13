package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
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
@RequestMapping("/user/category")
@Slf4j
public class CategoryUserController{
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/list")
    public Result<List<Category>> getCategory(@RequestParam(value ="type",required = false) Integer type){
        log.info("getCategory type:{}", type);
        List<Category> categories = categoryService.queryCategoryByTypes(type);
        log.info("分类{}的菜品:{}",type, categories);
        return Result.success(categories);
    }
}
