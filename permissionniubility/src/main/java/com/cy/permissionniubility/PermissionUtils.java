package com.cy.permissionniubility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.security.Permission;

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

    public static void checkPermission(Context context, String[] permissions, final OnPermissionCallback onPermissionCallback) {
        for (String permission : permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                    (permission.equals(Manifest.permission_group.STORAGE)
                            || permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)
                            || permission.equals(Manifest.permission.MANAGE_EXTERNAL_STORAGE))) {
                // 先判断有没有权限
                if (!Environment.isExternalStorageManager()) {

                    PermissionManager.getInstance().setOnPermissionCallback(onPermissionCallback);
                    Intent intent = new Intent(context, PermissionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(PermissionManager.BUNDLE_KEY_PERMISSIONS, PermissionManager.STORAGE_11);
                    intent.putExtra(PermissionManager.INTENT_KEY_PERMISSIONS, bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    return;
                }
            } else if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

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

    /**
     * 特殊权限
     * return Permission.MANAGE_EXTERNAL_STORAGE.equals(permission) ||
     * Permission.REQUEST_INSTALL_PACKAGES.equals(permission) ||
     * Permission.SYSTEM_ALERT_WINDOW.equals(permission) ||
     * Permission.NOTIFICATION_SERVICE.equals(permission) ||
     * Permission.WRITE_SETTINGS.equals(permission);
     *
     * @param context
     * @param onPermissionCallback
     */
    public static void checkPermissionExternalStorage(Context context, OnPermissionCallback onPermissionCallback) {
        //Android 11
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if (Environment.isExternalStorageManager()) {
                onPermissionCallback.onPermissionHave();
            } else {
                PermissionManager.getInstance().setOnPermissionCallback(onPermissionCallback);
                Intent intent = new Intent(context, PermissionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(PermissionManager.BUNDLE_KEY_PERMISSIONS, PermissionManager.STORAGE_11);
                intent.putExtra(PermissionManager.INTENT_KEY_PERMISSIONS, bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } else {
            checkPermission(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, onPermissionCallback);
        }
    }
}
