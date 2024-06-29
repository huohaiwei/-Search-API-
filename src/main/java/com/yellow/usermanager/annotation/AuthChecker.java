package com.yellow.usermanager.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限检查器
 * @author 陈翰垒
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthChecker {
    /**
     * 必须有某个角色
     *
     * @return 角色
     */
    String mustRole() default "";
}
