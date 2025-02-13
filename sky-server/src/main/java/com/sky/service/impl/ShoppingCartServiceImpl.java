package com.sky.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.service.ShoppingCartService;
import com.sky.mapper.ShoppingCartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
* @author windows
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2025-02-13 17:28:34
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService  setmealService;
    @Override
    public boolean add(ShoppingCartDTO shoppingCartDTO, Long userId) {
        ShoppingCart shoppingCart = BeanUtil.copyProperties(shoppingCartDTO, ShoppingCart.class);
        shoppingCart.setUserId(userId);
        shoppingCart.setCreateTime(LocalDateTime.now());
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        BigDecimal price = null;
        if(dishId!=null){
//          先在购物车中查询是否有该菜品，如果有，则数量+1，金额+单价，如果没有，则新增一条记录
            ShoppingCart one = getOne(
                    new QueryWrapper<ShoppingCart>()
                            .eq("user_id", userId)
                            .eq("dish_id", dishId)

            );
            price= dishService.queryDishById(dishId).getPrice();
            if(one!=null){
                one.setNumber(one.getNumber()+1);
                return updateById(one);//直接返回
            }
//            如果one为null,则查询菜品的信息添加到购物车中
            Dish dish = dishService.getById(dishId);
            shoppingCart.setName(dish.getName());
            shoppingCart.setImage(dish.getImage());
        }else if(setmealId!=null){
            ShoppingCart one = getOne(
                    new QueryWrapper<ShoppingCart>()
                            .eq("user_id", userId)
                            .eq("setmeal_id", setmealId)

            );
            price= setmealService.getSetmealById(setmealId).getPrice();
            if(one!=null){
                one.setNumber(one.getNumber()+1);
                return updateById(one);//直接返回
            }
            //   如果one为null,则查询菜品的信息添加到购物车中
            Setmeal setmeal = setmealService.getById(setmealId);
            shoppingCart.setName(setmeal.getName());
            shoppingCart.setImage(setmeal.getImage());
        }
        shoppingCart.setDishFlavor(shoppingCartDTO.getDishFlavor());
        shoppingCart.setAmount(price);
        return save(shoppingCart);
    }

    @Override
    public List<ShoppingCart> getList(Long id) {
        return lambdaQuery()
                .eq(ShoppingCart::getUserId, id)
                .list();
    }

    @Override
    public boolean sub(ShoppingCartDTO shoppingCartDTO, Long id) {
        String dishFlavor = shoppingCartDTO.getDishFlavor();
        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();
        ShoppingCart one = lambdaQuery()
                .eq(ShoppingCart::getUserId, id)
                .eq(dishId != null, ShoppingCart::getDishId, dishId)
                .eq(setmealId != null, ShoppingCart::getSetmealId, setmealId)
                .eq(dishFlavor != null, ShoppingCart::getDishFlavor, dishFlavor)
                .one();
        if(one!=null){
            if(one.getNumber()<=1){
                return removeById(one.getId());
            }else{
                one.setNumber(one.getNumber()-1);
                return updateById(one);
            }
        }else{
            return false;
        }
    }

    @Override
    public boolean clear(Long id) {
        return remove(new QueryWrapper<ShoppingCart>()
                .eq("user_id", id));
    }
}




