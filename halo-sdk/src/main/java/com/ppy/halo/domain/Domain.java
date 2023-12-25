package com.ppy.halo.domain;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 领域标注注解类
 *
 * @since 1.0 2023/12/17
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface Domain {
    String code() default "";

    String desc() default "";
}
