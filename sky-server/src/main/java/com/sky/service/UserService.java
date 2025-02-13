package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.User;
import com.sky.vo.UserLoginVO;

/**
* @author windows
* @description 针对表【user(用户信息)】的数据库操作Service
* @createDate 2025-02-12 13:58:02
*/
public interface UserService extends IService<User> {

    User wxlogin(String code);
}
