//package com.cy.permissionniubility;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.graphics.drawable.GradientDrawable;
//import android.view.Window;
//
//import androidx.annotation.NonNull;
//
//public class AskDialog  extends Dialog {
//    public AskDialog(@NonNull Context context) {
//        super(context);
//
//        getWindow().setContentView(layoutResID);
//
//        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除对话框的标题
//        GradientDrawable gradientDrawable = new GradientDrawable();
//        gradientDrawable.setColor(0x00000000);
//        getWindow().setBackgroundDrawable(gradientDrawable);//设置对话框边框背景,必须在代码中设置对话框背景，不然对话框背景是黑色的
//
//    }
//}
