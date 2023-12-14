package com.ppy.halo.repository;

import com.ppy.halo.bo.extension.ExtensionCoordinate;
import com.ppy.halo.extension.ExtensionPointI;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * ExtensionRepository
 * @author jackie
 * @version 1.0.0
 * @since 1.0.0 2023/13/18
 */
@Repository
public class ProductExtensionRepository implements ProductExtensionRepositoryI {
    
    private Map<ExtensionCoordinate, ExtensionPointI> extensionRepo = new HashMap<>();

    @Override
    public  <Ext extends ExtensionPointI> Ext getProductExtensionByUniqueKey(Class<Ext> clazz, ExtensionCoordinate extCo) {
        return (Ext)extensionRepo.get(extCo);
    }

    @Override
    public ExtensionPointI saveProductExtensionByUniqueKey(ExtensionCoordinate extCo, ExtensionPointI ext) {
        return extensionRepo.put(extCo,ext);
    }
}
