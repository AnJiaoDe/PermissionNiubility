package com.cy.permissionniubility;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/8/14 22:43
 * @UpdateUser:
 * @UpdateDate: 2020/8/14 22:43
 * @UpdateRemark:
 * @Version: 1.0
 */
public abstract class OnPermissionCallback {
    private Context context;

    public OnPermissionCallback(Context context) {
        this.context = context;
    }


    public abstract void onPermissionHave();

    public void onPermissionRefuse() {
    }

    public void onPermissionRefuseNoAsk() {
        authorize();
    }

    public String getAuthorizeDialogButtonPositive() {
        return context.getResources().getString(R.string.to_authorize);
    }

    public String getAuthorizeDialogMessage() {
        return context.getResources().getString(R.string.forbid_ask);
    }

    public String getAuthorizeDialogButtonNegative() {
        return context.getResources().getString(R.string.cancel);
    }

    public void authorize() {
        //解释原因，并且引导用户至设置页手动授权
        new AlertDialog.Builder(context)
                .setMessage(getAuthorizeDialogMessage())
                .setPositiveButton(getAuthorizeDialogButtonPositive(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //引导用户至设置页手动授权
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                        intent.setData(uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton(getAuthorizeDialogButtonNegative(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //引导用户手动授权，权限请求失败
                        dialog.dismiss();
                    }
                }).show();
    }
}
