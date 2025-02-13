package com.sky.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.service.UserService;
import com.sky.mapper.UserMapper;
import com.sky.utils.HttpClientUtil;
import com.sky.vo.UserLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
* @author windows
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2025-02-12 13:58:02
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Autowired
    private UserMapper userMapper;

    private static final String WX_LOGIN="https://api.weixin.qq.com/sns/jscode2session";
    @Override
    public User wxlogin(String code) {

        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("appid","wx67da6849ac3e12c0");
        paramMap.put("secret","c8639b7842bc5be06858a8cfd4b5558a");
        paramMap.put("js_code",code);
        paramMap.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, paramMap);
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        if(openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
//        判断是否为新用户，根据openid查询用户是否存在
        User user = isNewUser(openid);
        if(user==null){
//            新用户，注册
            User newUser = new User();
            newUser.setOpenid(openid);
            boolean save = save(newUser);
            if(save){
//                newUser没有id，应该在数据库中查询
                User user1 = isNewUser(openid);
                return user1;
            }
        }
        return user;
    }
//    判断是否为新用户
    private User isNewUser(String openid){
        return userMapper.selectOne(new QueryWrapper<User>()
        .eq("openid", openid));
    }
}




