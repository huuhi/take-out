package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetDefault;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
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
@RequestMapping("/user/addressBook")
@Slf4j
public class AddressController {
    @Autowired
    private AddressBookService addressBookService;

//    新增地址
    @PostMapping
    public Result addAddressBook(@RequestBody AddressBook addressBookDTO,
                                 @RequestHeader("authentication") String token) {
        Long userId = JwtUtil.getIdFromToken(token, JwtClaimsConstant.USER_ID);
        boolean save = addressBookService.saveAddress(addressBookDTO, userId);
        return save? Result.success() : Result.error(MessageConstant.UNKNOWN_ERROR);
    }
//    获取用户地址列表
    @GetMapping("/list")
    public Result<List<AddressBook>> listAddressBook(@RequestHeader("authentication") String token) {
        Long userId = JwtUtil.getIdFromToken(token, JwtClaimsConstant.USER_ID);
        return Result.success(addressBookService.listByUserId(userId));
    }
//    设置默认地址
    @PutMapping("/default")
    public Result setDefaultAddress(@RequestBody SetDefault setDefault){
        log.info("设置默认地址id:{}",setDefault.getId());
        boolean setDefault1 = addressBookService.setDefaultAddress(setDefault.getId());
        return setDefault1? Result.success() : Result.error(MessageConstant.UNKNOWN_ERROR);
    }
//    查询默认地址
    @GetMapping("/default")
    public Result<AddressBook> getDefaultAddress(@RequestHeader("authentication") String token) {
        Long id = JwtUtil.getIdFromToken(token, JwtClaimsConstant.USER_ID);
        AddressBook addressBook=  addressBookService.getDefaultAddress(id);
        return Result.success(addressBook);
    }
    @PutMapping
    public Result updateAddressBook(@RequestBody AddressBook addressBookDTO){
        boolean b = addressBookService.updateById(addressBookDTO);
        return b? Result.success() : Result.error(MessageConstant.UNKNOWN_ERROR);
    }
    @DeleteMapping
    public Result deleteAddressBook(@RequestParam("id") Integer id){
        boolean b = addressBookService.removeById(id);
        return b? Result.success() : Result.error(MessageConstant.UNKNOWN_ERROR);
    }
//    根据id查询地址
    @GetMapping("/{id}")
    public Result<AddressBook> getAddressBookById(@PathVariable("id") Integer id){
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

}
