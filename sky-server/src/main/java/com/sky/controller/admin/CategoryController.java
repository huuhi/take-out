package com.sky.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.PageDTO;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sky.entity.Category;

import java.util.List;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2025/2/10
 * 说明:
 */
@RequestMapping("/admin/category")
@Slf4j
@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

//    新增分类
    @PostMapping()
    public Result addCategory(@RequestBody CategoryDTO categoryDTO,
                              @RequestHeader("token") String token) {
        Category category = BeanUtil.copyProperties(categoryDTO, Category.class);
        Long id = JwtUtil.getIdFromToken(token, JwtClaimsConstant.EMP_ID);
        boolean save = categoryService.saveCategory(category,id);
        if (save) {
            return Result.success();
        } else {
            return Result.error("新增失败");
        }
    }
//    分页查询分类
    @GetMapping("/page")
    public Result<PageDTO<Category>> queryCategoryByPage(CategoryPageQueryDTO c){
        PageDTO<Category> categoryPageDTO = categoryService.queryCategoryByPage(c);
        return Result.success(categoryPageDTO);
    }

//    更新状态
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable("status") Integer status,
                               @RequestParam("id") Long id,
                               @RequestHeader("token") String token){
        boolean update = categoryService.updateStatus(status,id, JwtUtil.getIdFromToken(token, JwtClaimsConstant.EMP_ID));
        return update?Result.success():Result.error("更新失败");
    }
//    修改分类
    @PutMapping
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO,
                                 @RequestHeader("token") String token){
        boolean update = categoryService.updateCategory(categoryDTO, JwtUtil.getIdFromToken(token, JwtClaimsConstant.EMP_ID));
        return update?Result.success():Result.error("更新失败");
    }
//    根据id删除分类
    @DeleteMapping
    public Result deleteCategory(@RequestParam("id") Long id){
        boolean b = categoryService.removeById(id);
        return b?Result.success():Result.error("删除失败");
    }
//    根据类型查询分类
    @GetMapping("/list")
    public Result<List<Category>> queryCategoryByTypes(@RequestParam("type") Integer type){
        List<Category> categories = categoryService.queryCategoryByTypes(type);
        return Result.success(categories);
    }

    @GetMapping("/user/category/list")
    public Result<List<Category>> queryCategoryByType(@RequestParam("type") Integer type){
        List<Category> categories = categoryService.queryCategoryByTypes(type);
        return Result.success(categories);
    }
}
