package com.ppy.halo.config;

import com.ppy.halo.extension.Extension;
import com.ppy.halo.extension.authority.AuthorityPermissionExtensionI;
import com.ppy.halo.product.StandardProductExtI;
import com.ppy.halo.product.ProductExt;
import org.springframework.stereotype.Component;

/**
 * @author: jackie
 * @date: 2023/12/25 17:24
 **/
@ProductExt(code = "standardProduct")
@Component
public class StandardProduct implements StandardProductExtI {


    @Override
    @Extension
    public AuthorityPermissionExtensionI getAuthorityPermissionSDK() {
        return new AuthorityPermissionExtensionI() {
            @Override
            public String getLoadCase2SePermission() {
                return "1,2";
            }

            @Override
            public String getLoadCase2GePermission() {
                return "2";
            }

            @Override
            public String getNode2GePermission() {
                return "3,5";
            }

            @Override
            public String getNode2SePermission() {
                return "1,2,3";
            }

            @Override
            public String getCae2SePermission() {
                return "1,2,3,4,5";
            }
        };
    }
}
