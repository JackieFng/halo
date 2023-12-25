package com.ppy.halo.repository;

import com.ppy.halo.bo.extension.ExtensionCoordinate;
import com.ppy.halo.extension.ExtensionPointI;

/**
 * 产品仓库存储接口
 *
 * @since 1.0.0 2023/13/18
 */
public interface ProductExtensionRepositoryI {
    /**
     * 根据产品code+扩展点类名组成的唯一编码获取对应扩展点实例
     *
     * @date: 2023/12/18 11:34
     * @param: [clazz, extCo]
     * @return: Ext
     **/
    public <Ext extends ExtensionPointI> Ext getProductExtensionByUniqueKey(Class<Ext> clazz, ExtensionCoordinate extCo);

    /**
     * @date: 2023/12/18 13:14
     * @param: [extCo, ext]
     * @return: ExtensionPointI
     **/
    public ExtensionPointI saveProductExtensionByUniqueKey(ExtensionCoordinate extCo, ExtensionPointI ext);
}
