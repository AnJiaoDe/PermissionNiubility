package com.cy.permissionmaster;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import com.cy.permission.PermissionActivity;


public class MainActivity extends PermissionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionCAMERA(new OnPermissionRequestListener() {
                    @Override
                    public void onPermissionHave() {
                        showToast("用户同意了相机权限");

                        checkPermissionWRITE_EXTERNAL_STORAGE(new OnPermissionRequestListener() {
                            @Override
                            public void onPermissionHave() {
                                showToast("用户同意了存储权限,可以执行拍照操作了");

                            }

                            @Override
                            public void onPermissionRefuse() {
                                showToast("用户拒绝了存储权限");

                            }

                            @Override
                            public void onPermissionRefuseNoAsk() {
                                showToast("用户拒绝了存储权限并且选中了不再询问");

                            }
                        });
                    }

                    @Override
                    public void onPermissionRefuse() {
                        showToast("用户拒绝了相机权限");

                    }

                    @Override
                    public void onPermissionRefuseNoAsk() {
                        showToast("用户拒绝了相机权限并且选中了不再询问");

                    }
                });
            }
        });

       //或者
//        checkPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                "存储权限已被禁用","拍照功能必须打开存储权限，否则无法使用", new OnPermissionRequestListener() {
//
//                    @Override
//                    public void onPermissionHave() {
//
//                        showToast("用户同意了存储权限");
//
//
//                    }
//
//                    @Override
//                    public void onPermissionRefuse() {
//                        showToast("用户拒绝了存储权限");
//
//                    }
//
//                    @Override
//                    public void onPermissionRefuseNoAsk() {
//                        showToast("用户拒绝了存储权限并且选中了不再询问");
//
//                    }
//                });
    }

    @Override
    public void onClick(View v) {

    }
}
