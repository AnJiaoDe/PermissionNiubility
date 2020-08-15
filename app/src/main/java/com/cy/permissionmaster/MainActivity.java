package com.cy.permissionmaster;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

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
//                PermissionUtils.checkPermission(MainActivity.this,
//                        new String[]{Manifest.permission.CAMERA},
//                        new OnPermissionCallback(MainActivity.this) {
//                    @Override
//                    public void onPermissionHave() {
//                        PermissionUtils.checkPermission(MainActivity.this,
//                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                new OnPermissionCallback(MainActivity.this) {
//                            @Override
//                            public void onPermissionHave() {
//
//                            }
//                        });
//                    }
//                });
                PermissionUtils.checkPermission(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        new OnPermissionCallback(MainActivity.this) {
                    @Override
                    public void onPermissionHave() {
                    }
                });
            }
        });

    }
}
