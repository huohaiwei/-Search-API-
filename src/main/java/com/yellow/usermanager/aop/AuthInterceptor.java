package com.yellow.usermanager.aop;

import com.yellow.usermanager.annotation.AuthChecker;
import com.yellow.usermanager.exception.BusinessException;
import com.yellow.yellowapicommon.model.entity.User;
import com.yellow.usermanager.model.enums.ErrorCode;
import com.yellow.usermanager.model.enums.UserRoleEnum;
import com.yellow.usermanager.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 权限校验 AOP
 *
 * @author 陈翰垒
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * 权限校验织入
     *
     * @param joinPoint ProceedingJoinPoint只能在循环通知中使用,并且将结果返回，如果不返回相当于结果再此被拦截，请求方收到的就是空数据
     * @return
     * @throws Throwable
     */
    @Around(value = "@annotation(authChecker)")
    public Object doIntercept(ProceedingJoinPoint joinPoint, AuthChecker authChecker) throws Throwable {
        String mustRole = authChecker.mustRole();
        // 从RequestContextHolder中获取RequestAttributes，

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        // 转成原声的ServletRequestAttributes才能获取到request
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        // 判断当前用户是否拥有该权限
        if (StringUtils.isNotBlank(mustRole)) {
            UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
            if (userRoleEnum == null) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
            String userRole = loginUser.getUserRole();
            if (UserRoleEnum.BAN.equals(userRoleEnum)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
            if (UserRoleEnum.ADMIN.equals(userRoleEnum)) {
                if (!mustRole.equals(userRole)) {
                    throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
                }
            }
        }
        //校验通过放行,被注解的方法
        return joinPoint.proceed();
    }
}
