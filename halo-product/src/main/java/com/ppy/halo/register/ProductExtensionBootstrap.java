package com.ppy.halo.register;

import com.ppy.halo.facade.ProductExtFacadeI;
import com.ppy.halo.product.ProductExt;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * 加载产品扩展点配置用的启动类
 *
 * @author jackie
 * @since 1.0.0 2023/12/18
 */
@Component
public class ProductExtensionBootstrap implements ApplicationContextAware {

    @Resource
    private ProductExtensionRegister extensionRegister;

    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        Map<String, Object> extensionBeans = applicationContext.getBeansWithAnnotation(ProductExt.class);
        extensionBeans.values().forEach(
                extension -> extensionRegister.doRegistration((ProductExtFacadeI) extension)
        );
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
