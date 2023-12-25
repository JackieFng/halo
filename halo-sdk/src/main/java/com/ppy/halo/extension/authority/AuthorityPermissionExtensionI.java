package com.ppy.halo.extension.authority;

import com.ppy.halo.extension.ExtensionPointI;

/**
 * 扩展点定义类，扩展点的定义与领域至少一一对应,也可以具体对应到领域下的某个能力,如有多少个领域或者领域能力就应该有多少个相应的扩展点定义类。
 *
 * @author: jackie
 * @date: 2023/12/25 17:33
 **/
public interface AuthorityPermissionExtensionI extends ExtensionPointI {

    String getLoadCase2SePermission();

    String getLoadCase2GePermission();

    String getNode2GePermission();

    String getNode2SePermission();

    String getCae2SePermission();
}
