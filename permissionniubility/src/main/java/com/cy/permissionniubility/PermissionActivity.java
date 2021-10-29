package com.cy.permissionniubility;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;


/**
 * Created by lenovo on 2017/4/25.
 */

public class PermissionActivity extends AppCompatActivity {
    private final int REQUEST_CODE_CTORAGE_11 = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        Bundle bundle = getIntent().getBundleExtra(PermissionManager.INTENT_KEY_PERMISSIONS);
        if (bundle == null) return;
        if (bundle.getString(PermissionManager.BUNDLE_KEY_PERMISSIONS, "").equals(PermissionManager.STORAGE_11)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // 先判断有没有权限
                if (Environment.isExternalStorageManager()) {
                    OnPermissionCallback onPermissionCallback = PermissionManager.getInstance().getOnPermissionCallback();
                    PermissionManager.getInstance().setOnPermissionCallback(null);
                    finish();
                    if (onPermissionCallback != null)
                        onPermissionCallback.onPermissionHave();
                } else {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, REQUEST_CODE_CTORAGE_11);

                }
            }
            return;
        }
        String[] permissions = bundle.getStringArray(PermissionManager.BUNDLE_KEY_PERMISSIONS);
        if (permissions != null && permissions.length > 0)
            ActivityCompat.requestPermissions(this, permissions, 1001);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        OnPermissionCallback onPermissionCallback = PermissionManager.getInstance().getOnPermissionCallback();
        PermissionManager.getInstance().setOnPermissionCallback(null);
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) continue;
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                //在用户已经拒绝授权的情况下，如果shouldShowRequestPermissionRationale返回false则
                // 可以推断出用户选择了“不在提示”选项，在这种情况下需要引导用户至设置页手动授权
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                    finish();
                    //权限请求失败，但未选中“不再提示”选项
                    if (onPermissionCallback != null)
                        onPermissionCallback.onPermissionNoAsk();
                } else {
                    finish();
                    //权限请求失败，但未选中“不再提示”选项
                    if (onPermissionCallback != null)
                        onPermissionCallback.onPermissionRefuse();
                }
                return;
            }
        }
        finish();
        if (onPermissionCallback != null)
            onPermissionCallback.onPermissionHave();
    }

    /**
     * 在所欲文件访问权限界面，反复开关，导致APP重启，即使这样，问题也不大，因为用户点击按钮才触发权限请求的场景，
     * 大不了让用户再点击一次按钮。而一进入界面，自动触发权限请求的场景，APP重启后，自然会在那个地方判断权限，所以，完全不用当成一个问题
     * 2021-10-29 20:09:29.800 18060-18060/com.cy.permissionmaster E/onActivityResultoneate: onCreatefalse
     * 2021-10-29 20:09:29.804 18060-18060/com.cy.permissionmaster E/onActivityResult: REQUEST_CODE_CTORAGE_11
     * 2021-10-29 20:09:29.866 18060-18060/com.cy.permissionmaster E/onActivityResult: onRestoreInstanceState
     * 2021-10-29 20:09:29.881 18060-18060/com.cy.permissionmaster E/onActivityResult: VERSION_CODES1111111:100
     * 2021-10-29 20:09:29.881 18060-18060/com.cy.permissionmaster E/onActivityResult: VERSION_CODES1111111
     * 2021-10-29 20:09:29.882 18060-18060/com.cy.permissionmaster E/onActivityResult: true
     * 2021-10-29 20:09:29.884 18060-18060/com.cy.permissionmaster E/onActivityResult: onResume
     * 2021-10-29 20:09:29.993 18060-18060/com.cy.permissionmaster E/onActivityResult: onPause
     * 2021-10-29 20:09:30.177 18060-18060/com.cy.permissionmaster E/onActivityResult: onStop
     * 2021-10-29 20:09:30.179 18060-18060/com.cy.permissionmaster E/onActivityResult: onDestroy
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CTORAGE_11 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            OnPermissionCallback onPermissionCallback = PermissionManager.getInstance().getOnPermissionCallback();
            PermissionManager.getInstance().setOnPermissionCallback(null);
            finish();
            if (onPermissionCallback == null) return;
            if (Environment.isExternalStorageManager()) {
                onPermissionCallback.onPermissionHave();
            } else {
                onPermissionCallback.onPermissionRefuse();
            }
        }
    }

}
