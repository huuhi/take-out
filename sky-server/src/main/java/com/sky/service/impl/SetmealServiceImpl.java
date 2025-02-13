package com.sky.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.PageDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.query.PageQuery;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.mapper.SetmealMapper;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author windows
* @description 针对表【setmeal(套餐)】的数据库操作Service实现
* @createDate 2025-02-11 19:01:06
*/
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
    implements SetmealService{
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishService dishService;
    @Override
    public boolean addSetmeal(SetmealDTO setmealDTO,Long userId) {
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        Setmeal setmeal = BeanUtil.copyProperties(setmealDTO, Setmeal.class);
        setmeal.setCreateUser(userId);
        setmeal.setCreateTime(LocalDateTime.now());
        setmeal.setUpdateTime(LocalDateTime.now());
        boolean save = save(setmeal);
        Long setmealId;
        if(save){
            setmealId = lambdaQuery()
                    .eq(Setmeal::getName, setmeal.getName()).one().getId();
        } else {
            setmealId = 0L;
        }
        if(setmealDishes!= null && !setmealDishes.isEmpty()){
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
                setmealDishMapper.insert(setmealDish);
            });
        }
        return save;
    }

    @Override
    public PageDTO<SetmealVO> pageSetmeal(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPage(setmealPageQueryDTO.getPage());
        pageQuery.setPageSize(setmealPageQueryDTO.getPageSize());
        Page<Setmeal> page = pageQuery.toMpPageDefaultSortByUpdateTime();
        String name = setmealPageQueryDTO.getName();
        Integer categoryId = setmealPageQueryDTO.getCategoryId();
        Integer status = setmealPageQueryDTO.getStatus();
        Page<Setmeal> paged = lambdaQuery().like(name != null, Setmeal::getName, name)
                .eq(categoryId != null, Setmeal::getCategoryId, categoryId)
                .eq(status != null, Setmeal::getStatus, status)
                .page(page);

        return PageDTO.of(paged,setmeal->{
            SetmealVO setmealVO = BeanUtil.copyProperties(setmeal, SetmealVO.class);
            String categoryName = categoryService.getById(setmeal.getCategoryId()).getName();
            setmealVO.setCategoryName(categoryName);
            return setmealVO;
        });
    }

    @Override
    public boolean updateStatus(Integer status, Long id, Long userId) {
        Setmeal setmeal = getById(id);
        setmeal.setStatus(status);
        setmeal.setUpdateUser(userId);
        setmeal.setUpdateTime(LocalDateTime.now());
        return updateById(setmeal);
    }

    @Override
    public boolean deleteByIds(List<Long> ids) {
        boolean b = removeBatchByIds(ids);
        int setmealId = setmealDishMapper.delete(
                new QueryWrapper<SetmealDish>()
                        .in("setmeal_id", ids)
        );
        log.info("删除套餐菜品关联数据：{}条", setmealId);
        return b;
    }

    @Override
    public SetmealVO getSetmealById(Long id) {
        Setmeal setmeal = getById(id);
        List<SetmealDish> setmealDish = extracted(id);
        SetmealVO setmealVO = BeanUtil.copyProperties(setmeal, SetmealVO.class);
        if(setmealVO==null){
            return null;
        }
        setmealVO.setSetmealDishes(setmealDish);
        return setmealVO;
    }

    @Override
    public boolean updateSetmeal(SetmealDTO setmealDTO, Long userId) {
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        List<SetmealDish> extracted = extracted(setmealDTO.getId());
        if(extracted.size()>setmealDishes.size()){
//            说明需要删除
            List<Long> idsToDelete = extracted.stream()
                .filter(dish -> dish.getId() != null)  // 过滤掉 id 为 null 的元素
                .map(SetmealDish::getId)
                .filter(id -> setmealDishes.stream()
                        .filter(dish -> dish.getId() != null)  // 过滤掉 id 为 null 的元素
                    .noneMatch(dish -> Objects.equals(dish.getId(), id)))  // 使用 Objects.equals 安全比较
                .collect(Collectors.toList());
                if(!idsToDelete.isEmpty()){
                    int i = setmealDishMapper.deleteBatchIds(idsToDelete);
                    log.info("删除套餐菜品关联数据：{}条", i);
                }
        }
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealDTO.getId());
            if(setmealDish.getId() == null){
                setmealDishMapper.insert(setmealDish);
            }else{
                setmealDishMapper.updateById(setmealDish);
            }
        });
        Setmeal setmeal = BeanUtil.copyProperties(setmealDTO, Setmeal.class);
        setmeal.setUpdateUser(userId);
        setmeal.setUpdateTime(LocalDateTime.now());
        return updateById(setmeal);
    }

    @Override
    public List<DishItemVO> getDishById(Long id) {
        List<DishItemVO> dishItemVOS = new ArrayList<>();
        List<SetmealDish> extracted = extracted(id);
        extracted.forEach(setmealDish -> {
            Long dishId = setmealDish.getDishId();
            Dish dish = dishService.getById(dishId);
            DishItemVO dishItemVO = new DishItemVO();
            dishItemVO.setCopies(setmealDish.getCopies());
            dishItemVO.setName(dish.getName());
            dishItemVO.setImage(dish.getImage());
            dishItemVO.setDescription(dish.getDescription());
            dishItemVOS.add(dishItemVO);
        });
        return dishItemVOS;
    }

    private List<SetmealDish> extracted(Long setmealDTO) {
        return setmealDishMapper.selectList(
                new QueryWrapper<SetmealDish>()
                        .eq("setmeal_id", setmealDTO)
        );
    }
}

