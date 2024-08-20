package com.cy.permissionniubility;

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
    public static final String INTENT_KEY_ASK = "intent_key_ask";
    public static final String INTENT_KEY_PERMISSIONS = "intent_key_permissions";
    public static final String ACTION_MANAGE_WRITE_SETTINGS = "ACTION_MANAGE_WRITE_SETTINGS";
    public static final String BUNDLE_KEY_PERMISSIONS = "bundle_key_permissions";
    public static final String STORAGE_11 = "STORAGE_11";
    private CallbackPermission callbackPermission;
    private PermissionManager() {
    }

    private static class PermissionManagerHolder {
        private static PermissionManager instance = new PermissionManager();
    }

    public static PermissionManager getInstance() {
        return PermissionManagerHolder.instance;
    }

    public CallbackPermission getOnPermissionCallback() {
        return callbackPermission;
    }

    public void setOnPermissionCallback(CallbackPermission callbackPermission) {
        this.callbackPermission = callbackPermission;
    }
}
