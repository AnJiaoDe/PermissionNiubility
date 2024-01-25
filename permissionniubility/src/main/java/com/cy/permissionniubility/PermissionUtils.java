package com.cy.permissionniubility;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;
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

    private static void showDialogAsk(Context context, String text_ask, final CallbackAsk callbackAsk) {
        if (TextUtils.isEmpty(text_ask)) {
            callbackAsk.onToAuthorizeClicked();
            return;
        }
        new AlertDialog.Builder(context)
                .setMessage(text_ask)
                .setPositiveButton(context.getResources().getString(R.string.to_authorize), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        callbackAsk.onToAuthorizeClicked();
                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public static void checkPermission(final Context context, String text_ask, final String[] permissions, final CallbackPermission callbackPermission) {
        for (String permission : permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                    (permission.equals(Manifest.permission_group.STORAGE)
                            || permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)
                            || permission.equals(Manifest.permission.MANAGE_EXTERNAL_STORAGE))) {
                // 先判断有没有权限
                if (!Environment.isExternalStorageManager()) {
                    showDialogAsk(context, text_ask, new CallbackAsk() {
                        @Override
                        public void onToAuthorizeClicked() {
                            PermissionManager.getInstance().setOnPermissionCallback(callbackPermission);
                            Intent intent = new Intent(context, PermissionActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(PermissionManager.BUNDLE_KEY_PERMISSIONS, PermissionManager.STORAGE_11);
                            intent.putExtra(PermissionManager.INTENT_KEY_PERMISSIONS, bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    });
                    return;
                }
            } else if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                showDialogAsk(context, text_ask, new CallbackAsk() {
                    @Override
                    public void onToAuthorizeClicked() {
                        PermissionManager.getInstance().setOnPermissionCallback(callbackPermission);
                        Intent intent = new Intent(context, PermissionActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArray(PermissionManager.BUNDLE_KEY_PERMISSIONS, permissions);
                        intent.putExtra(PermissionManager.INTENT_KEY_PERMISSIONS, bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
                return;
            }
        }
        callbackPermission.onPermissionHave();
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
     * @param callbackPermission
     */
    public static void checkPermissionExternalStorage(final Context context, String text_ask, final CallbackPermission callbackPermission) {
        //Android 11
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if (Environment.isExternalStorageManager()) {
                callbackPermission.onPermissionHave();
            } else {
                showDialogAsk(context, text_ask, new CallbackAsk() {
                    @Override
                    public void onToAuthorizeClicked() {
                        PermissionManager.getInstance().setOnPermissionCallback(callbackPermission);
                        Intent intent = new Intent(context, PermissionActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(PermissionManager.BUNDLE_KEY_PERMISSIONS, PermissionManager.STORAGE_11);
                        intent.putExtra(PermissionManager.INTENT_KEY_PERMISSIONS, bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
            }
        } else {
            checkPermission(context, text_ask, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, callbackPermission);
        }
    }

    public static boolean havePermissionExternalStorage(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager())
            return true;
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }


    public static void checkWRITE_SETTINGS(final Context context, String text_ask, final CallbackPermission callbackPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(context)) {
            showDialogAsk(context, text_ask, new CallbackAsk() {
                @Override
                public void onToAuthorizeClicked() {
                    PermissionManager.getInstance().setOnPermissionCallback(callbackPermission);
                    Intent intent = new Intent(context, PermissionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(PermissionManager.BUNDLE_KEY_PERMISSIONS, PermissionManager.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.putExtra(PermissionManager.INTENT_KEY_PERMISSIONS, bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            return;
        }
        callbackPermission.onPermissionHave();
    }

    private static interface CallbackAsk {
        void onToAuthorizeClicked();
    }
}
