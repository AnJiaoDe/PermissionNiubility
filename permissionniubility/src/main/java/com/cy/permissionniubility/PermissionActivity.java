package com.cy.permissionniubility;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        Bundle bundle = getIntent().getBundleExtra(PermissionManager.INTENT_KEY_PERMISSIONS);
        String[] permissions = null;
        if (bundle != null)
            permissions = bundle.getStringArray(PermissionManager.BUNDLE_KEY_PERMISSIONS);
        if (permissions != null && permissions.length > 0)
            ActivityCompat.requestPermissions(this, permissions, 1001);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        OnPermissionCallback onPermissionCallback  = PermissionManager.getInstance().getOnPermissionCallback();
        PermissionManager.getInstance().setOnPermissionCallback(null);
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) continue;
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                //在用户已经拒绝授权的情况下，如果shouldShowRequestPermissionRationale返回false则
                // 可以推断出用户选择了“不在提示”选项，在这种情况下需要引导用户至设置页手动授权
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                    authorize();
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

    private String getAuthorizeDialogButtonPositive() {
        return getResources().getString(R.string.to_authorize);
    }

    private String getAuthorizeDialogMessage() {
        return getResources().getString(R.string.forbid_ask);
    }

    private String getAuthorizeDialogButtonNegative() {
        return getResources().getString(R.string.cancel);
    }

    private void authorize() {
        //解释原因，并且引导用户至设置页手动授权
        new AlertDialog.Builder(this)
                .setMessage(getAuthorizeDialogMessage())
                .setPositiveButton(getAuthorizeDialogButtonPositive(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //引导用户至设置页手动授权
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(getAuthorizeDialogButtonNegative(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //引导用户手动授权，权限请求失败
                        dialog.dismiss();
                    }
                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        }).show();
    }

}
