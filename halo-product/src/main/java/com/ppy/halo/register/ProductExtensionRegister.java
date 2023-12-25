package com.ppy.halo.register;

import com.ppy.halo.bo.extension.ExtensionCoordinate;
import com.ppy.halo.bo.scenario.BizScenario;
import com.ppy.halo.exception.BizRuntimeException;
import com.ppy.halo.extension.Extension;
import com.ppy.halo.extension.ExtensionPointI;
import com.ppy.halo.facade.ProductExtFacadeI;
import com.ppy.halo.product.ProductExt;
import com.ppy.halo.repository.ProductExtensionRepositoryI;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.ppy.halo.exception.ErrorMessage.*;


/**
 * 产品扩展点注册类
 *
 * @author jackie
 * @since 1.0.0 2023/12/18
 */
@Component
public class ProductExtensionRegister {

    @Resource
    private ProductExtensionRepositoryI extensionRepository;

    /**
     * @description:
     * @date: 2022/11/18 11:07
     * @param: [productExt]
     * @return: void
     **/
    public void doRegistration(ProductExtFacadeI productExt) {
        Class<?> productExtClazz = productExt.getClass();
        if (AopUtils.isJdkDynamicProxy(productExt)) {
            productExtClazz = ClassUtils.getUserClass(productExt);
        }
        ProductExt extensionAnn = AnnotationUtils.findAnnotation(productExtClazz, ProductExt.class);
        // 获取被注解为Extension的方法
        List<Method> extMethod = Arrays.stream(productExtClazz.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(Extension.class)).collect(Collectors.toList());
        for (Method mtd : extMethod) {
            Class returnType = mtd.getReturnType();
            String simpleName = productExt.getClass().getSimpleName().concat(".").concat(mtd.getName());
            if (returnType == null || !ExtensionPointI.class.isAssignableFrom(returnType)) {
                throw new BizRuntimeException(EXTENSION_REGISTER_RETURN_TYPE_ERROR.getCode(), String.format(EXTENSION_REGISTER_RETURN_TYPE_ERROR.getMessage(), simpleName));
            }
            //只能是空参数定义方法返回扩展点实例，否则报错
            if (mtd.getParameterCount() > 0) {
                throw new BizRuntimeException(EXTENSION_REGISTER_PARAMETER_ERROR.getCode(), String.format(EXTENSION_REGISTER_PARAMETER_ERROR.getMessage(), simpleName));
            }
            ExtensionPointI extInstance = null;
            try {
                extInstance = (ExtensionPointI) mtd.invoke(productExt, null);
            } catch (Exception ex) {
                throw new BizRuntimeException(EXTENSION_REGISTER_METHOD_INVOKE_ERROR.getCode(), String.format(EXTENSION_REGISTER_METHOD_INVOKE_ERROR.getMessage(), simpleName), ex);
            }
            if (extInstance == null) {
                throw new BizRuntimeException(EXTENSION_REGISTER_RETURN_VALUE_ERROR.getCode(), String.format(EXTENSION_REGISTER_RETURN_VALUE_ERROR.getMessage(), simpleName));
            }
            //仅仅通过bizId去定义扩展点坐标。因为目前扩展点是基于产品维度的，bizId对应这一个产品。
            BizScenario bizScenario = BizScenario.valueOf(extensionAnn.code());
            ExtensionCoordinate extensionCoordinate = new ExtensionCoordinate(calculateExtensionPoint(extInstance.getClass()), bizScenario.getUniqueIdentity());
            ExtensionPointI preVal = extensionRepository.saveProductExtensionByUniqueKey(extensionCoordinate, extInstance);
            if (preVal != null) {
                throw new BizRuntimeException(EXTENSION_REGISTER_DUPLICATE_ERROR.getCode(), String.format(EXTENSION_REGISTER_DUPLICATE_ERROR.getMessage(), simpleName, preVal.getClass().getSimpleName(), extensionAnn.code()));
            }
        }
    }

    /**
     * @description:
     * @date: 2022/11/18 11:07
     * @param: [targetClz]
     * @return: java.lang.String
     **/
    private String calculateExtensionPoint(Class<?> targetClz) {
        Class<?>[] interfaces = ClassUtils.getAllInterfacesForClass(targetClz);
        if (interfaces != null && interfaces.length > 0) {
            for (Class intf : interfaces) {
                if (ExtensionPointI.class.isAssignableFrom(intf)) {
                    return intf.getName();
                }
            }
        }
        throw new BizRuntimeException(EXTENSION_REGISTER_INTERFACE_ERROR.getCode(), String.format(EXTENSION_REGISTER_INTERFACE_ERROR.getMessage(), targetClz.getSimpleName()));
    }
}
