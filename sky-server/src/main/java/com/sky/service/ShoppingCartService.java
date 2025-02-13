package com.sky.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
* @author windows
* @description 针对表【shopping_cart(购物车)】的数据库操作Service
* @createDate 2025-02-13 17:28:34
*/
public interface ShoppingCartService extends IService<ShoppingCart> {

    boolean add(ShoppingCartDTO shoppingCartDTO, Long userId);

    List<ShoppingCart> getList(Long id);

    boolean sub(ShoppingCartDTO shoppingCartDTO, Long id);

    boolean clear(Long id);
}
