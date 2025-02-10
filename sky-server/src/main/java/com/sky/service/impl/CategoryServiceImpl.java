package com.sky.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.PageDTO;
import com.sky.mapper.CategoryMapper;
import com.sky.query.PageQuery;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sky.entity.Category;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2025/2/10
 * 说明:
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public boolean saveCategory(Category category,Long createId) {
        category.setStatus(1);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(createId);
        return save(category);
    }

    @Override
    public PageDTO<Category> queryCategoryByPage(CategoryPageQueryDTO c) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPage(c.getPage());
        pageQuery.setPageSize(c.getPageSize());
        pageQuery.setName(c.getName());
        Page<Category> sort = pageQuery.toMpPage(OrderItem.asc("sort"));
        Page<Category> page = lambdaQuery().eq(c.getType() != null, Category::getType, c.getType())
                .like(c.getName() != null, Category::getName, c.getName())
                .page(sort);
        return PageDTO.of(page, Category.class);
    }

    @Override
    public boolean updateStatus(Integer status, Long id, Long userId) {
        Category category = getById(id);
        category.setStatus(status);
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(userId);
        return updateById(category);
    }

    @Override
    public boolean updateCategory(CategoryDTO categoryDTO, Long userId) {
        Category category = BeanUtil.copyProperties(categoryDTO, Category.class);
        category.setUpdateUser(userId);
        category.setUpdateTime(LocalDateTime.now());
        return updateById(category);
    }

    @Override
    public List<Category> queryCategoryByTypes(Integer type) {
        return lambdaQuery()
                .eq(type != null, Category::getType, type)
                .list();
    }
}
