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
    public abstract void onPermissionHave();
    public void onPermissionRefuse() {
    }
}
