package com.ppy.halo.product;

import com.ppy.halo.extension.authority.AuthorityPermissionExtensionI;
import com.ppy.halo.facade.ProductExtFacadeI;

/**
 * 将产品相关的扩展点定义类汇聚到产品facade 上,实现扩展点的统一管控
 *
 * @author: jackie
 * @date: 2023/12/25 17:21
 **/
public interface StandardProductExtI extends ProductExtFacadeI {

    AuthorityPermissionExtensionI getAuthorityPermissionSDK();
}
