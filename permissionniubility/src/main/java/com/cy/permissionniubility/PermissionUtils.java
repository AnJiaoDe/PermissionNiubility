package com.cy.permissionniubility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/8/14 22:37
 * @UpdateUser:
 * @UpdateDate: 2020/8/14 22:37
 * @UpdateRemark:
 * @Version: 1.0
 */
public class PermissionUtils {
    public static void checkPermission(Context context, String[] permissions, OnPermissionCallback onPermissionCallback) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                PermissionManager.getInstance().setOnPermissionCallback(onPermissionCallback);
                Intent intent = new Intent(context, PermissionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArray(PermissionManager.BUNDLE_KEY_PERMISSIONS, permissions);
                intent.putExtra(PermissionManager.INTENT_KEY_PERMISSIONS, bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return;
            }
        }
        onPermissionCallback.onPermissionHave();
    }
}
