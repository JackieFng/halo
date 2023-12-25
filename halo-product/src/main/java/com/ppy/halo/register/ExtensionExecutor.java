package com.ppy.halo.register;

import com.ppy.halo.bo.extension.ExtensionCoordinate;
import com.ppy.halo.bo.scenario.BizScenario;
import com.ppy.halo.exception.BizRuntimeException;
import com.ppy.halo.extension.ExtensionPointI;
import com.ppy.halo.repository.ProductExtensionRepositoryI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.ppy.halo.exception.ErrorMessage.EXTENSION_NOT_FOUND_ERROR;
import static com.ppy.halo.exception.ErrorMessage.EXTENSION_SCENARIO_NULL_ERROR;

/**
 * 产品扩展点获取子类，包括具体加载扩展点的实现
 *
 * @author jackie
 * @since 1.0.0 2022/11/18
 */
@Component
public class ExtensionExecutor extends AbstractComponentExecutor {

    private static final String EXTENSION_NOT_FOUND = "extension_not_found";

    private Logger logger = LoggerFactory.getLogger(ExtensionExecutor.class);

    @Resource
    private ProductExtensionRepositoryI productExtensionRepository;

    /**
     * 加载扩展点的入口方法
     *
     * @date: 2022/11/18 16:25
     * @param: [targetClz, bizScenario]
     * @return: C
     **/
    @Override
    protected <C extends ExtensionPointI> C locateComponent(Class<C> targetClz, BizScenario bizScenario) {
        C extension = locateExtension(targetClz, bizScenario);
        logger.debug("[Located Extension]: " + extension.getClass().getSimpleName());
        return extension;
    }

    /**
     * 扩展点加载具体实现。会尝试三次操作，
     * 第一次根据bizId+useCase+scenario尝试获取，如果获取不到
     * 第二次根据bizId+useCase尝试获取，如果获取不到
     * 第三次根据bizId尝试获取，三次全部获取不到报错
     *
     * @date: 2022/11/18 16:25
     * @param: [targetClz, bizScenario]
     * @return: Ext
     **/
    protected <Ext extends ExtensionPointI> Ext locateExtension(Class<Ext> targetClz, BizScenario bizScenario) {
        checkNull(bizScenario);

        Ext extension;

        logger.debug("BizScenario in locateExtension is : " + bizScenario.getUniqueIdentity());

        // first try with full namespace
        extension = firstTry(targetClz, bizScenario);
        if (extension != null) {
            return extension;
        }

        // second try with default scenario
        extension = secondTry(targetClz, bizScenario);
        if (extension != null) {
            return extension;
        }
        // third try with default use case + default scenario
        extension = defaultUseCaseTry(targetClz, bizScenario);
        if (extension != null) {
            return extension;
        }
        throw new BizRuntimeException(EXTENSION_NOT_FOUND_ERROR.getCode(), String.format(EXTENSION_NOT_FOUND_ERROR.getMessage(), targetClz, bizScenario.getUniqueIdentity()));
    }

    /**
     * 用全名bizId+useCase+scenario尝试获取扩展点
     *
     * @date: 2022/11/21 09:18
     * @param: [targetClz, bizScenario]
     * @return: Ext
     **/
    private <Ext extends ExtensionPointI> Ext firstTry(Class<Ext> targetClz, BizScenario bizScenario) {
        logger.debug("First trying with " + bizScenario.getUniqueIdentity());
        return locate(targetClz, targetClz.getName(), bizScenario.getUniqueIdentity());
    }

    /**
     * 用bizId+useCase+defaultScenario尝试获取扩展点
     *
     * @date: 2022/11/21 09:22
     * @param: [targetClz, bizScenario]
     * @return: Ext
     **/
    private <Ext extends ExtensionPointI> Ext secondTry(Class<Ext> targetClz, BizScenario bizScenario) {
        logger.debug("Second trying with " + bizScenario.getIdentityWithDefaultScenario());
        return locate(targetClz, targetClz.getName(), bizScenario.getIdentityWithDefaultScenario());
    }

    /**
     * 用bizId+defaultUseCase+defaultScenario尝试获取扩展点
     *
     * @date: 2022/11/21 09:22
     * @param: [targetClz, bizScenario]
     * @return: Ext
     **/
    private <Ext extends ExtensionPointI> Ext defaultUseCaseTry(Class<Ext> targetClz, BizScenario bizScenario) {
        logger.debug("Third trying with " + bizScenario.getIdentityWithDefaultUseCase());
        return locate(targetClz, targetClz.getName(), bizScenario.getIdentityWithDefaultUseCase());
    }

    private <Ext extends ExtensionPointI> Ext locate(Class<Ext> clazz, String name, String uniqueIdentity) {
        final Ext ext = (Ext) productExtensionRepository.getProductExtensionByUniqueKey(clazz, new ExtensionCoordinate(name, uniqueIdentity));
        return ext;
    }

    private void checkNull(BizScenario bizScenario) {
        if (bizScenario == null) {
            throw new IllegalArgumentException(EXTENSION_SCENARIO_NULL_ERROR.getCode());
        }
    }
}
