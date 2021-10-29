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
        String[] permissions = null;
        if (bundle != null) {
            permissions = bundle.getStringArray(PermissionManager.BUNDLE_KEY_PERMISSIONS);
            if (bundle.getString(PermissionManager.BUNDLE_KEY_PERMISSIONS, "").equals(PermissionManager.STORAGE_11)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Log.e("VERSION_CODES","VERSION_CODES");
                    // 先判断有没有权限
                    if (Environment.isExternalStorageManager()) {
                        OnPermissionCallback onPermissionCallback = PermissionManager.getInstance().getOnPermissionCallback();
                        PermissionManager.getInstance().setOnPermissionCallback(null);
                        finish();
                        if (onPermissionCallback != null)
                            onPermissionCallback.onPermissionHave();
                    } else {
                        Log.e("VERSION_CODES","VERSION_CODES0000");

                        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, REQUEST_CODE_CTORAGE_11);
                    }
                }
            }
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CTORAGE_11 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            OnPermissionCallback onPermissionCallback = PermissionManager.getInstance().getOnPermissionCallback();
            PermissionManager.getInstance().setOnPermissionCallback(null);
            finish();
            if (onPermissionCallback == null) return;
            Log.e("VERSION_CODES","VERSION_CODES1111111");

            if (Environment.isExternalStorageManager()) {
                onPermissionCallback.onPermissionHave();
            } else {
                onPermissionCallback.onPermissionRefuse();
            }
        }
    }

}
