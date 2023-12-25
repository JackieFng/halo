package com.ppy.halo.extension;

import com.ppy.halo.bo.scenario.BizScenario;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 扩展点注解，标明是个扩展点，可以作用于类跟方法上
 *
 * @since 1.0 2023/12/17
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Component
public @interface Extension {
    String bizId() default BizScenario.DEFAULT_BIZ_ID;

    String useCase() default BizScenario.DEFAULT_USE_CASE;

    String scenario() default BizScenario.DEFAULT_SCENARIO;

    String des() default "";
}
