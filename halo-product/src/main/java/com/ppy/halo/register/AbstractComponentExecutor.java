package com.ppy.halo.register;

import com.ppy.halo.bo.extension.ExtensionCoordinate;
import com.ppy.halo.bo.scenario.BizScenario;
import com.ppy.halo.extension.ExtensionPointI;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 产品扩展点获取以及执行超类
 *
 * @author jackie
 * @since 1.0.0 2022/11/18
 */
public abstract class AbstractComponentExecutor {

    /**
     * 根据扩展点类型，场景对应获取产品扩展点并执行扩展点方法并返回结果
     *
     * @param: [targetClz, bizScenario, exeFunction]
     * @return: R
     **/
    public <R, T extends ExtensionPointI> R execute(Class<T> targetClz, BizScenario bizScenario, Function<T, R> exeFunction) {
        T component = locateComponent(targetClz, bizScenario);
        return exeFunction.apply(component);
    }

    /**
     * 根据扩展点坐标获取产品扩展点并执行扩展点方法并返回结果
     *
     * @param: [extensionCoordinate, exeFunction]
     * @return: R
     **/
    public <R, T extends ExtensionPointI> R execute(ExtensionCoordinate extensionCoordinate, Function<T, R> exeFunction) {
        return execute((Class<T>) extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(), exeFunction);
    }

    /**
     * 根据扩展点类型，场景对应获取产品扩展点并执行扩展点方法，不返回执行结果
     *
     * @param: [targetClz, context, exeFunction]
     * @return: void
     **/
    public <T extends ExtensionPointI> void executeVoid(Class<T> targetClz, BizScenario context, Consumer<T> exeFunction) {
        T component = locateComponent(targetClz, context);
        exeFunction.accept(component);
    }

    /**
     * 根据扩展点坐标获取产品扩展点并执行扩展点方法，不返回结果
     *
     * @param: [extensionCoordinate, exeFunction]
     * @return: void
     **/
    public <T extends ExtensionPointI> void executeVoid(ExtensionCoordinate extensionCoordinate, Consumer<T> exeFunction) {
        executeVoid(extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(), exeFunction);
    }

    /**
     * 根据扩展点类型，场景对象加载对应的扩展点实现
     *
     * @param: [targetClz, context]
     * @return: C
     **/
    protected abstract <C extends ExtensionPointI> C locateComponent(Class<C> targetClz, BizScenario context);
}
