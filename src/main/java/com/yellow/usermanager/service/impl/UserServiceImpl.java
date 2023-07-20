package com.yellow.usermanager.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yellow.usermanager.common.ErrorCode;
import com.yellow.usermanager.constant.UserConstant;
import com.yellow.usermanager.exception.BusinessException;
import com.yellow.usermanager.model.domain.User;
import com.yellow.usermanager.service.UserService;
import com.yellow.usermanager.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;



import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 陈翰垒
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2023-07-14 00:14:18
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    UserMapper userMapper;

    /**
     * 盐值
     */
    private static final String SALT = "yellow";


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword,String userCode) {
        //1.校验非空
        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword,userCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"表单有空项！");
        }
        //判断账号和密码的长度
        if (userAccount.length() < 4) throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号小于4位");;
        if (userPassword.length() < 8 || checkPassword.length() < 8) throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码小于8位");;
        //用户编号长度校验
        if (userCode.length()>5)throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户编号大于5位数");;
        //账户不能包含特殊字符
        String validPattern = "[_`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户包含特殊字符");;
        //两次密码是否相同
        if (!checkPassword.equals(userPassword))throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码相同");;
        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户已存在");;
        //用户编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userCode", userCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户编号已存在");;
        //2.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserCode(userCode);
        boolean saveResult = this.save(user);
        if (!saveResult) throw new BusinessException(ErrorCode.PARAMS_ERROR,"注册用户失败！");;

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验非空
        if (StringUtils.isAllBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"表单有空项！");
        }
        //判断密码和账号的长度
        if (userAccount.length() < 4) throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号长度少于4位");;
        if (userPassword.length() < 8) throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度少于8位");;
        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.NULL_ERROR,"账号或密码错误！");
        }
        //脱敏
        User safetyUser = getSafetyUser(user);
        //登录成功后记录用户的登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    /**
     * 用户信息脱敏
     *
     * @param originUser 原始用户信息
     * @return 脱敏后的用户信息
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        //返回用户信息（脱敏）
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUserCode(originUser.getUserCode());
        return safetyUser;
    }

    /**
     * 用户注销
     *
     * @param request 注销用户的请求
     * @return 随便返回一个数
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return 1;
    }

}




