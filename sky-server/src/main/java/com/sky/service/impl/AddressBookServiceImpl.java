package com.sky.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.AddressIsDefault;
import com.sky.entity.AddressBook;
import com.sky.service.AddressBookService;
import com.sky.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author windows
* @description 针对表【address_book(地址簿)】的数据库操作Service实现
* @createDate 2025-02-13 19:58:35
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

    @Override
    public List<AddressBook> listByUserId(Long userId) {
        return lambdaQuery()
                .eq(AddressBook::getUserId, userId)
                .list();
    }

    @Override
    public boolean saveAddress(AddressBook addressBookDTO, Long userId) {
        addressBookDTO.setUserId(userId);
        return save(addressBookDTO);
    }

    @Override
    public boolean setDefaultAddress(Integer id) {
        AddressBook address = getById(id);
        if (address != null) {
            address.setIsDefault(AddressIsDefault.IS_DEFAULT);
            return updateById(address);
        } else {
            return false;
        }
    }

    @Override
    public AddressBook getDefaultAddress(Long id) {
        return  lambdaQuery()
                .eq(AddressBook::getUserId, id)
                .eq(AddressBook::getIsDefault, AddressIsDefault.IS_DEFAULT)
                .one();
    }
}




