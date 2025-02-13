package com.sky.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.AddressBook;

import java.util.List;

/**
* @author windows
* @description 针对表【address_book(地址簿)】的数据库操作Service
* @createDate 2025-02-13 19:58:35
*/
public interface AddressBookService extends IService<AddressBook> {

    List<AddressBook> listByUserId(Long userId);

    boolean saveAddress(AddressBook addressBookDTO, Long userId);

    boolean setDefaultAddress(Integer id);

    AddressBook getDefaultAddress(Long id);
}
