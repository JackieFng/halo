package com.ppy.halo.product;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 产品扩展点实现类标注用注解,标明类是个产品扩展点实现类
 * @author jackie
 * @since 1.0 2023/12/17
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface ProductExt {
    String code()  default "";
    String desc()  default "";
}
