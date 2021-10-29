package com.cy.permissionniubility;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/8/14 23:17
 * @UpdateUser:
 * @UpdateDate: 2020/8/14 23:17
 * @UpdateRemark:
 * @Version: 1.0
 */
 class PermissionManager {
    public static final String INTENT_KEY_PERMISSIONS = "intent_key_permissions";
    public static final String BUNDLE_KEY_PERMISSIONS = "bundle_key_permissions";
    public static final String STORAGE_11 = "STORAGE_11";
    private OnPermissionCallback onPermissionCallback;
    private PermissionManager() {
    }

    private static class PermissionManagerHolder {
        private static PermissionManager instance = new PermissionManager();
    }

    public static PermissionManager getInstance() {
        return PermissionManagerHolder.instance;
    }

    public OnPermissionCallback getOnPermissionCallback() {
        return onPermissionCallback;
    }

    public void setOnPermissionCallback(OnPermissionCallback onPermissionCallback) {
        this.onPermissionCallback = onPermissionCallback;
    }
}
