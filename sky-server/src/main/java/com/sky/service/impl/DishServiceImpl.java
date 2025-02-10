package com.sky.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.PageDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.query.PageQuery;
import com.sky.service.CategoryService;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2025/2/11
 * 说明:
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private CategoryService categoryService;

    @Override
    public boolean addDish(DishDTO dishDTO,Long userId) {
        List<DishFlavor> flavors = dishDTO.getFlavors();
        Dish dish = BeanUtil.copyProperties(dishDTO, Dish.class);
//        口味也需要添加到数据库中
        dish.setCreateUser(userId);
        dish.setCreateTime(LocalDateTime.now());
        dish.setUpdateTime(LocalDateTime.now());
        boolean save = save(dish);
        if (save&& !flavors.isEmpty()) {
//            查询出刚才添加的菜品id
            Long id = getOne(Wrappers.<Dish>lambdaQuery().eq(Dish::getName, dish.getName())).getId();

//            存储口味
            flavors.forEach(flavor -> {
                flavor.setDishId(id);
                dishFlavorMapper.insert(flavor);//行不行？？？
            });
        }
        return save;
    }

    @Override
    public PageDTO<DishVO> queryPage(DishPageQueryDTO queryDTO) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPage(queryDTO.getPage());
        pageQuery.setPageSize(queryDTO.getPageSize());
        String name = queryDTO.getName();
        Integer status = queryDTO.getStatus();
        Integer categoryId = queryDTO.getCategoryId();

        Page<Dish> page = pageQuery.toMpPageDefaultSortByUpdateTime();
        Page<Dish> page1 = lambdaQuery()
                .like(name != null, Dish::getName, name)
                .eq(status != null, Dish::getStatus, status)
                .eq(categoryId != null, Dish::getCategoryId, categoryId)
                .page(page);
        PageDTO<DishVO> dishVOPageDTO = PageDTO.of(page1, dish->{
//            将分类id转换为名称
            DishVO dishVO = BeanUtil.copyProperties(dish, DishVO.class);
            Category category = categoryService.getById(dish.getCategoryId());
            dishVO.setCategoryName(category.getName());
            return dishVO;
        });
        List<DishVO> records = dishVOPageDTO.getRecords();
        if (!records.isEmpty()) {
//            查询出口味
            records.forEach(dish -> {
                Long id = dish.getId();
//                根据id查询出口味
                List<DishFlavor> flavors = dishFlavorMapper.selectList(
                    Wrappers.<DishFlavor>lambdaQuery()
                        .eq(DishFlavor::getDishId, id)
                );
                dish.setFlavors(flavors);
            });
        }
//        还需要查询分类


        return dishVOPageDTO;
    }

    @Override
    public List<DishVO> queryDishByCategoryId(Long categoryId) {
        List<Dish> list = lambdaQuery().eq(Dish::getCategoryId, categoryId)
                .list();
        if (!list.isEmpty()) {
            List<DishVO> dishVO = BeanUtil.copyToList(list, DishVO.class);
            dishVO.forEach(dish->{
                String name = categoryService.getById(categoryId).getName();
                dish.setCategoryName(name);
            });
            return dishVO;
        }
        return List.of();
    }

    @Override
    public boolean updateDish(DishDTO dishDTO, Long id) {
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors.isEmpty()){
//            如果口味为空，则删除原有的口味
            dishFlavorMapper.delete(Wrappers.<DishFlavor>lambdaQuery()
                    .eq(DishFlavor::getDishId,dishDTO.getId()));
        }else{
//             如果口味不为空，遍历处理！如果在数据库不存在就添加
//            如果存在就更新
            flavors.forEach(flavor->{
                flavor.setDishId(dishDTO.getId());
                if(flavor.getId()==null){
//                    新增
                    dishFlavorMapper.insert(flavor);
                }else{
                    dishFlavorMapper.updateById(flavor);
                }
            });
        }
        Dish dish = BeanUtil.copyProperties(dishDTO, Dish.class);
        dish.setUpdateUser(id);
        dish.setUpdateTime(LocalDateTime.now());
        return updateById(dish);
    }

    @Override
    public DishVO queryDishById(Long id) {
        Dish dish = getById(id);
        Long categoryId = dish.getCategoryId();
        String name = categoryService.getById(categoryId).getName();
        DishVO dishVO = BeanUtil.copyProperties(dish, DishVO.class);
        dishVO.setCategoryName(name);
        List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(Wrappers.<DishFlavor>lambdaQuery()
                .eq(DishFlavor::getDishId, id));
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    @Override
    public boolean updateDishStatus(Integer status, Long id, Long userId) {
        Dish dish = getById(id);
        dish.setStatus(status);
        dish.setUpdateUser(userId);
        dish.setUpdateTime(LocalDateTime.now());
        return updateById(dish);
    }
}
