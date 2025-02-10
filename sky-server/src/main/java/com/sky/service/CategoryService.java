package com.sky.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.PageDTO;
import com.sky.entity.Category;
import com.sky.result.Result;

import java.util.List;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2025/2/10
 * 说明:
 */
//@Service
public interface CategoryService extends IService<Category> {
    boolean saveCategory(Category category, Long userId);

    PageDTO<Category> queryCategoryByPage(CategoryPageQueryDTO c);

    boolean updateStatus(Integer status, Long id, Long userId);

    boolean updateCategory(CategoryDTO categoryDTO, Long userId);

    List<Category> queryCategoryByTypes(Integer type);
}
