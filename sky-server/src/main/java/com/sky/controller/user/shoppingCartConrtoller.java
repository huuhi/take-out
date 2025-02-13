package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import com.sky.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2025/2/13
 * 说明:
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class shoppingCartConrtoller {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO,
                      @RequestHeader("authentication") String token){
        Long id = JwtUtil.getIdFromToken(token, JwtClaimsConstant.USER_ID);
        boolean flag= shoppingCartService.add(shoppingCartDTO,id);
        return flag? Result.success() : Result.error(MessageConstant.USER_NOT_LOGIN);
    }

    @GetMapping("/list")
    public Result<List<ShoppingCart>> getList(@RequestHeader("authentication") String token){
        Long id = JwtUtil.getIdFromToken(token, JwtClaimsConstant.USER_ID);
        List<ShoppingCart> list = shoppingCartService.getList(id);
        log.info("购物车数据:{}",list);
        return Result.success(list);
    }
    @PostMapping("/sub")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO,
                      @RequestHeader("authentication") String token){
        Long id = JwtUtil.getIdFromToken(token, JwtClaimsConstant.USER_ID);
        boolean flag= shoppingCartService.sub(shoppingCartDTO,id);
        return flag? Result.success() : Result.error(MessageConstant.USER_NOT_LOGIN);
    }
//    清空购物车
    @DeleteMapping("/clean")
    public Result clear(@RequestHeader("authentication") String token){
        Long id = JwtUtil.getIdFromToken(token, JwtClaimsConstant.USER_ID);
        boolean flag= shoppingCartService.clear(id);
        return flag? Result.success() : Result.error(MessageConstant.USER_NOT_LOGIN);
    }

}
