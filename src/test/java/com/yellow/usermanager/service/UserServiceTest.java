package com.yellow.usermanager.service;
import java.util.Date;

import com.yellow.usermanager.model.domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/*
 * 用户服务测试
 * */
@SpringBootTest
class UserServiceTest {


    @Resource
    private UserService userService;

    @Test
    public void testAddUser() {
    }

    @Test
    void userRegister() {
        String uA="ssss";
        String uP="";
        String cP="123456";
        String uC="123";
        long result = userService.userRegister(uA,uP,cP,uC);
        Assert.assertEquals(-1,result);
        uA="sss";
        result = userService.userRegister(uA,uP,cP,uC);
        Assert.assertEquals(-1,result);
        uA="ssss";
        uP="123456";
        result = userService.userRegister(uA,uP,cP,uC);
        Assert.assertEquals(-1,result);
        uA="ss ss";
        uP="12345678";
        cP="12345678";
        result = userService.userRegister(uA,uP,cP,uC);
        Assert.assertEquals(-1,result);
        uA="ssss";
        cP="123456789";
        result = userService.userRegister(uA,uP,cP,uC);
        Assert.assertEquals(-1,result);
        uA="ssssx";
        cP="12345678";
        result = userService.userRegister(uA,uP,cP,uC);
        Assert.assertEquals(-1,result);
        uA="ssss";
        result = userService.userRegister(uA,uP,cP,uC);
        Assert.assertEquals(-1,result);
    }
}