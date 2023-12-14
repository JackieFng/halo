package com.ppy.halo.domain;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 领域能力标注注解类
 * @since 1.0 2023/12/17
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Component
public @interface Ability {
    String code()  default "";
    String desc()  default "";
}
