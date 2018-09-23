package com.cy.permission;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;


/**
 * Created by lenovo on 2017/4/25.
 */

public abstract class PermissionActivity extends AppCompatActivity implements View.OnClickListener {
    private String toast_perm_refuse,dialog_perm_refuse_noask;
    private OnPermissionRequestListener onPermissionHaveListener;


    public <T extends View> T getViewByID(@IdRes int id) {
        return (T) findViewById(id);
    }


    //??????????????????????????????????????????????????????????????????????
    public String nullToString(Object object) {
        return object == null ? "" : object.toString();
    }


    //??????????????????????????????????????????????????????????????????????
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int string_id) {
        Toast.makeText(this, getResources().getString(string_id), Toast.LENGTH_SHORT).show();
    }


    public void startAppcompatActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }


    /*
            6.0权限检查存储权限
             */
    public void checkPermissionWRITE_EXTERNAL_STORAGE( OnPermissionRequestListener onPermissionHaveListener) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        this.toast_perm_refuse = "存储权限已被禁用";
        this.dialog_perm_refuse_noask="存储权限被禁用，您将无法使用相机、相册、图片下载等功能";

        this.onPermissionHaveListener = onPermissionHaveListener;

    }
    /*
            6.0权限检查相机权限
             */
    public void checkPermissionCAMERA( OnPermissionRequestListener onPermissionHaveListener) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        this.toast_perm_refuse = "相机权限已被禁用";
        this.dialog_perm_refuse_noask="相机权限被禁用，您将无法使用相机进行拍照";

        this.onPermissionHaveListener = onPermissionHaveListener;

    }
    /*
            6.0权限检查
             */
    public void checkPermission(String[] permission, String toast_perm_refuse,String dialog_perm_refuse_noask, OnPermissionRequestListener onPermissionHaveListener) {
        ActivityCompat.requestPermissions(this, permission, 1);
        this.toast_perm_refuse = toast_perm_refuse;
        this.dialog_perm_refuse_noask=dialog_perm_refuse_noask;

        this.onPermissionHaveListener = onPermissionHaveListener;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; i++) {


            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                if (onPermissionHaveListener != null) {
                    onPermissionHaveListener.onPermissionHave();
                }
                continue;

            }

            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                //在用户已经拒绝授权的情况下，如果shouldShowRequestPermissionRationale返回false则
                // 可以推断出用户选择了“不在提示”选项，在这种情况下需要引导用户至设置页手动授权
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                    if (onPermissionHaveListener != null) {
                        onPermissionHaveListener.onPermissionRefuseNoAsk();
                    }
                    //解释原因，并且引导用户至设置页手动授权
                    new AlertDialog.Builder(this)
                            .setMessage(dialog_perm_refuse_noask)
                            .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //引导用户至设置页手动授权
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //引导用户手动授权，权限请求失败
                                }
                            }).show();

                } else {
                    //权限请求失败，但未选中“不再提示”选项
                    showToast(toast_perm_refuse);
                    if (onPermissionHaveListener != null) {
                        onPermissionHaveListener.onPermissionRefuse();
                    }
                }
                break;
            }

        }


    }

    public interface OnPermissionRequestListener {
        public void onPermissionHave();

        public void onPermissionRefuse();

        public void onPermissionRefuseNoAsk();
    }
}
