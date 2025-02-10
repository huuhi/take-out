package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.PageDTO;
import com.sky.entity.Dish;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2025/2/11
 * 说明:
 */
@Service
public interface DishService extends IService<Dish> {
    boolean addDish(DishDTO dishDTO,Long userId);

    PageDTO<DishVO> queryPage(DishPageQueryDTO queryDTO);

    List<DishVO> queryDishByCategoryId(Long categoryId);

    boolean updateDish(DishDTO dishDTO, Long id);

    DishVO queryDishById(Long id);

    boolean updateDishStatus(Integer status, Long id, Long userId);
}
