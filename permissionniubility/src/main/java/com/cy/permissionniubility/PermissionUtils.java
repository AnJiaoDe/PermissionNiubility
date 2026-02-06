package com.cy.permissionniubility;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.Nullable;
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
    public static String getApplicationMeta(Context context, String key) {
        String value = "";
        try {

            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo == null || appInfo.metaData == null) return value;
            value = appInfo.metaData.get(key).toString();
            //注意：是Exception，防止没有定义字段而崩溃
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    private static String getChannel(Context context) {
        return getApplicationMeta(context, "CHANNEL");
    }

    private static void showDialog(Context context, String text_ask, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context).setMessage(text_ask).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton(R.string.to_authorize, onClickListener).show();
    }

    private static void startRequestPermission(final Context context, String text_ask, final Intent intent) {
        if (TextUtils.isEmpty(text_ask)) {
            context.startActivity(intent);
            return;
        }
        showDialog(context, text_ask, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                context.startActivity(intent);
            }
        });
    }

    /**
     * @param context
     * @param callbackPermission
     */
    public static void requestPicSelect(final Context context, final CallbackPermission callbackPermission) {
        // > Android 9
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            callbackPermission.onPermissionHave();
            return;
        }
        requestPermission(context, null, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, callbackPermission);
    }

    /**
     * //这3个在android 11上面  用isExternalStorageManager判断已经不灵了
     *
     * @param permission
     * @return
     */
    public static boolean isStoragePermission(final String permission) {
        return permission.equals(Manifest.permission_group.STORAGE)
                || permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)
                || permission.equals(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
    }

    public static String replaceWRITE_EXTERNAL_STORAGE(final String permission) {
        if (isStoragePermission(permission)) {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ? Manifest.permission.MANAGE_EXTERNAL_STORAGE : Manifest.permission.WRITE_EXTERNAL_STORAGE;
        }
        return permission;
    }

    public static boolean havePermission(final Context context, final String p) {
        String permission = replaceWRITE_EXTERNAL_STORAGE(p);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && isStoragePermission(permission)) {
            return Environment.isExternalStorageManager();
        }
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean havePermissions(final Context context, final String[] permissions) {
        for (String permission : permissions) {
            if (!havePermission(context, permission)) return false;
        }
        return true;
    }

    public static boolean havePermissionExternalStorage(Context context) {
        return havePermission(context, Manifest.permission.MANAGE_EXTERNAL_STORAGE);
    }

    public static void requestPermission(final Context context, final String text_ask, final String[] permissions, @Nullable final CallbackPermission callbackPermission) {
        for (String permission : permissions) {
            permission = replaceWRITE_EXTERNAL_STORAGE(permission);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && permission.equals(Manifest.permission.MANAGE_EXTERNAL_STORAGE)) {
                // 先判断有没有权限
                if (!Environment.isExternalStorageManager()) {
//                    LogUtils.log("isExternalStorageManager");
                    PermissionManager.getInstance().setOnPermissionCallback(callbackPermission);
                    Intent intent = new Intent(context, PermissionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(PermissionManager.BUNDLE_KEY_PERMISSIONS, PermissionManager.STORAGE_11);
                    if ("vivo".equals(getChannel(context)))
                        bundle.putString(PermissionManager.INTENT_KEY_ASK, text_ask);
                    intent.putExtra(PermissionManager.INTENT_KEY_PERMISSIONS, bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startRequestPermission(context, text_ask, intent);
                    return;
                }
            } else if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
//                LogUtils.log("isExternalStorageManager 11111111");

                PermissionManager.getInstance().setOnPermissionCallback(callbackPermission);
                Intent intent = new Intent(context, PermissionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArray(PermissionManager.BUNDLE_KEY_PERMISSIONS, permissions);
                if ("vivo".equals(getChannel(context)))
                    bundle.putString(PermissionManager.INTENT_KEY_ASK, text_ask);
                intent.putExtra(PermissionManager.INTENT_KEY_PERMISSIONS, bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startRequestPermission(context, text_ask, intent);
                return;
            }
        }
        if (callbackPermission != null) callbackPermission.onPermissionHave();
    }

    public static void checknStorageAsk(final Context context, final CallbackPermission callbackPermission) {
        checkPermissionExternalStorage(context, context.getString(R.string.storage_permission_ask), callbackPermission);
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
                PermissionManager.getInstance().setOnPermissionCallback(callbackPermission);
                Intent intent = new Intent(context, PermissionActivity.class);
                Bundle bundle = new Bundle();
                if ("vivo".equals(getChannel(context)))
                    bundle.putString(PermissionManager.INTENT_KEY_ASK, text_ask);
                bundle.putString(PermissionManager.BUNDLE_KEY_PERMISSIONS, PermissionManager.STORAGE_11);
                intent.putExtra(PermissionManager.INTENT_KEY_PERMISSIONS, bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startRequestPermission(context, text_ask, intent);
            }
        } else {
            requestPermission(context, text_ask, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, callbackPermission);
        }
    }

    public static void requestWRITE_SETTINGS(final Context context, String text_ask, final CallbackPermission callbackPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(context)) {
            PermissionManager.getInstance().setOnPermissionCallback(callbackPermission);
            Intent intent = new Intent(context, PermissionActivity.class);
            Bundle bundle = new Bundle();
            if ("vivo".equals(getChannel(context)))
                bundle.putString(PermissionManager.INTENT_KEY_ASK, text_ask);
            bundle.putString(PermissionManager.BUNDLE_KEY_PERMISSIONS, PermissionManager.ACTION_MANAGE_WRITE_SETTINGS);
            intent.putExtra(PermissionManager.INTENT_KEY_PERMISSIONS, bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startRequestPermission(context, text_ask, intent);
            return;
        }
        callbackPermission.onPermissionHave();
    }
}
