package com.yellow.usermanager.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

import com.yellow.usermanager.esdao.InterfaceInfoEsDao;
import com.yellow.usermanager.model.dto.interfaceinfo.InterfaceInfoEsDTO;
import com.yellow.yellowapicommon.services.InnerUserInterfaceInfoService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;


/**
 * 用户服务测试
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private InnerUserInterfaceInfoService userService;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private InterfaceInfoEsDao interfaceInfoEsDao;
    @Test
    public void testInterface() {
       InterfaceInfoEsDTO infoEsDTO = new InterfaceInfoEsDTO();
       infoEsDTO.setId(1L);
       infoEsDTO.setName("小明爱吃");
       infoEsDTO.setUserId(1L);
       infoEsDTO.setDescription("接口1111");
       infoEsDTO.setCreateTime(new Date());
       infoEsDTO.setUpdateTime(new Date());
       infoEsDTO.setIsDelete(0);
       InterfaceInfoEsDTO infoEsDTO1 = new InterfaceInfoEsDTO();
        BeanUtils.copyProperties(infoEsDTO,infoEsDTO1);
        infoEsDTO1.setId(2L);
        infoEsDTO1.setName("小明爱吃猪肉和认识李四");
        infoEsDTO1.setUserId(2L);
        InterfaceInfoEsDTO infoEsDTO2 = new InterfaceInfoEsDTO();
        BeanUtils.copyProperties(infoEsDTO,infoEsDTO2);
        infoEsDTO2.setId(3L);
        infoEsDTO2.setName("小明传");
        infoEsDTO2.setUserId(3L);
        List<InterfaceInfoEsDTO> list = Arrays.asList(infoEsDTO,infoEsDTO1,infoEsDTO2);
        interfaceInfoEsDao.saveAll(list);
        List<InterfaceInfoEsDTO> res = interfaceInfoEsDao.findByName("小明");
        System.out.println(res);
    }

    @Test
    public void testAddUser() {
        boolean b = userService.invokeCount(1L, 1L);
        Assertions.assertTrue(b);
    }





}