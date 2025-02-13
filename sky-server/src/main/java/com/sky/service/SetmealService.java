package com.sky.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.PageDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

/**
* @author windows
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2025-02-11 19:01:06
*/
public interface SetmealService extends IService<Setmeal> {

    boolean addSetmeal(SetmealDTO setmealDTO,Long userId);

    PageDTO<SetmealVO> pageSetmeal(SetmealPageQueryDTO setmealPageQueryDTO);

    boolean updateStatus(Integer status, Long id, Long userId);

    boolean deleteByIds(List<Long> ids);

    SetmealVO getSetmealById(Long id);

    boolean updateSetmeal(SetmealDTO setmealDTO, Long userId);

    List<DishItemVO> getDishById(Long id);
}
