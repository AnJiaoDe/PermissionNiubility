package com.cy.permissionniubility;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

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
public abstract class CallbackPermissionSimple extends CallbackPermission {

    public CallbackPermissionSimple(Context context) {
        super(context);
    }
    @Override
    public abstract void onPermissionHave();

    @Override
    public void onPermissionRefuse() {
        Toast.makeText(context, getAuthorizeDialogMessage(), Toast.LENGTH_SHORT).show();
    }

}
