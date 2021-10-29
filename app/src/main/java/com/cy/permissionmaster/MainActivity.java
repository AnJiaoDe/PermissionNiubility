package com.cy.permissionmaster;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.cy.permissionniubility.OnPermissionCallback;
import com.cy.permissionniubility.PermissionUtils;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtils.checkPermission(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        new OnPermissionCallback(MainActivity.this) {
                    @Override
                    public void onPermissionHave() {
                        PermissionUtils.checkPermission(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                new OnPermissionCallback(MainActivity.this) {
                            @Override
                            public void onPermissionHave() {
                                Toast.makeText(MainActivity.this, "可以拍照了", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
//                PermissionUtils.checkPermission(MainActivity.this,
//                        new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        new OnPermissionCallback(MainActivity.this) {
//                    @Override
//                    public void onPermissionHave() {
//                    }
//                });
            }
        });
        findViewById(R.id.btn_11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtils.checkPermissionExternalStorage(MainActivity.this, new OnPermissionCallback(MainActivity.this) {
                    @Override
                    public void onPermissionHave() {
                        Toast.makeText(MainActivity.this, "拥有存储权限了", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRefuse() {
                        super.onPermissionRefuse();
                        Toast.makeText(MainActivity.this, "尚未同意存储权限", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
