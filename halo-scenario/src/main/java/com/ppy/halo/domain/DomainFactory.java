package com.ppy.halo.domain;

/**
 *
 * @author: jackie
 * @since: 1.0 2023/11/23
 */
public class DomainFactory {

    public static <T> T create(Class<T> entityClz){
        return ApplicationContextHelper.getBean(entityClz);
    }
}
