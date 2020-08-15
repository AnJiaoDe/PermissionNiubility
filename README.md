文章目录

GitHub:https://github.com/AnJiaoDe/PermissionNiubility

使用方法

3.调用方式：

注意：记得去gayhub查看最新版本，最新版本最niubility

1.全部允许
2.拒绝
3.拒绝
4.用户拒绝并且选中了不再询问，弹窗让用户去授权
5.用户同意了授权，闲的难受去关闭了授权

实现原理：

源码：

欢迎联系、指正、批评

## [GitHub:https://github.com/AnJiaoDe/PermissionNiubility](https://github.com/AnJiaoDe/PermissionNiubility)

## 使用方法
1.工程目录下的build.gradle中添加代码：

```java
allprojects {
		repositories {
			
			maven { url 'https://jitpack.io' }
		}
	}
```
2.直接在需要使用的模块的build.gradle中添加代码：

```java
api 'com.github.AnJiaoDe:PermissionNiubility:V1.0.2'
```

## 3.调用方式：

可以一个个请求：
```java
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

                            }
                        });
                    }
                });
```
也可以同时请求：
```java
      PermissionUtils.checkPermission(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        new OnPermissionCallback(MainActivity.this) {
                    @Override
                    public void onPermissionHave() {
                    }
                });
```

## 注意：记得去gayhub查看最新版本，最新版本最niubility
Android 6.0以上，危险权限必须经过动态请求，危险权限分组图

![在这里插入图片描述](https://img-blog.csdn.net/20180923113051590?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbmZ1c2luZ19hd2FrZW5pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)



## 1.全部允许


![在这里插入图片描述](https://img-blog.csdn.net/20180923113205889?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbmZ1c2luZ19hd2FrZW5pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)



## 2.拒绝

![在这里插入图片描述](https://img-blog.csdn.net/2018092311322513?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbmZ1c2luZ19hd2FrZW5pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

## 3.拒绝

![在这里插入图片描述](https://img-blog.csdn.net/20180923113241448?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbmZ1c2luZ19hd2FrZW5pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

## 4.用户拒绝并且选中了不再询问，弹窗让用户去授权

![在这里插入图片描述](https://img-blog.csdn.net/20180923113304837?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NvbmZ1c2luZ19hd2FrZW5pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

## 5.用户同意了授权，闲的难受去关闭了授权

## 实现原理：

启动一个全透明的Activity去请求权限，回调对象用单例类持有
## 源码：
PermissionUtils：

```java
public class PermissionUtils {
    public static void checkPermission(Activity activity,String[] permissions, OnPermissionCallback onPermissionCallback) {
       for(String permission:permissions){
           if(ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
               PermissionManager.getInstance().setOnPermissionCallback(onPermissionCallback);
               Intent intent=new Intent(activity,PermissionActivity.class);
               Bundle bundle=new Bundle();
               bundle.putStringArray(PermissionManager.BUNDLE_KEY_PERMISSIONS,permissions);
               intent.putExtra(PermissionManager.INTENT_KEY_PERMISSIONS,bundle);
               activity.startActivity(intent);
               return;
           }
       }
       onPermissionCallback.onPermissionHave();
    }
}
```

PermissionActivity:

```java
public class PermissionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        Bundle bundle = getIntent().getBundleExtra(PermissionManager.INTENT_KEY_PERMISSIONS);
        String[] permissions=null;
        if (bundle != null)
            permissions = bundle.getStringArray(PermissionManager.BUNDLE_KEY_PERMISSIONS);
        if (permissions != null && permissions.length > 0)
            ActivityCompat.requestPermissions(this, permissions, 1001);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        OnPermissionCallback onPermissionCallback = PermissionManager.getInstance().getOnPermissionCallback();
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) continue;
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                //在用户已经拒绝授权的情况下，如果shouldShowRequestPermissionRationale返回false则
                // 可以推断出用户选择了“不在提示”选项，在这种情况下需要引导用户至设置页手动授权
                finish();
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                    if (onPermissionCallback != null)
                        onPermissionCallback.onPermissionRefuseNoAsk();
                } else {
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
}

```
OnPermissionCallback：

```java
public abstract class OnPermissionCallback {
    private Activity activity;

    public OnPermissionCallback(Activity activity) {
        this.activity = activity;
    }

    public abstract void onPermissionHave();

    public void onPermissionRefuse() {
    }

    public void onPermissionRefuseNoAsk() {
        authorize();
    }
    public String getAuthorizeDialogButtonPositive(){
        return "去授权";
    }
    public String getAuthorizeDialogMessage(){
        return "禁用这些权限，您可能无法使用某些功能";
    }
    public String getAuthorizeDialogButtonNegative(){
        return "取消";
    }
    public void authorize() {
        //解释原因，并且引导用户至设置页手动授权
        new AlertDialog.Builder(activity)
                .setMessage(getAuthorizeDialogMessage())
                .setPositiveButton(getAuthorizeDialogButtonPositive(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //引导用户至设置页手动授权
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivity(intent);
                    }
                })
                .setNegativeButton(getAuthorizeDialogButtonNegative(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //引导用户手动授权，权限请求失败
                    }
                }).show();
    }
}

```
PermissionManager：

```java
class PermissionManager {
    public static final String INTENT_KEY_PERMISSIONS = "intent_key_permissions";
    public static final String BUNDLE_KEY_PERMISSIONS = "bundle_key_permissions";
    private OnPermissionCallback onPermissionCallback;
    private PermissionManager() {
    }

    private static class PermissionManagerHolder {
        private static PermissionManager instance = new PermissionManager();
    }

    public static PermissionManager getInstance() {
        return PermissionManagerHolder.instance;
    }

    public OnPermissionCallback getOnPermissionCallback() {
        return onPermissionCallback;
    }

    public void setOnPermissionCallback(OnPermissionCallback onPermissionCallback) {
        this.onPermissionCallback = onPermissionCallback;
    }
}

```
## 欢迎联系、指正、批评



Github:[https://github.com/AnJiaoDe](https://github.com/AnJiaoDe)

简书：[https://www.jianshu.com/u/b8159d455c69](https://www.jianshu.com/u/b8159d455c69)

CSDN：[https://blog.csdn.net/confusing_awakening](https://blog.csdn.net/confusing_awakening)

ffmpeg入门教程:[https://www.jianshu.com/p/042c7847bd8a](https://www.jianshu.com/p/042c7847bd8a)

 微信公众号
 ![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL3VwbG9hZC1pbWFnZXMuamlhbnNodS5pby91cGxvYWRfaW1hZ2VzLzExODY2MDc4LWZjZmJiNDUxNzVmOTlkZTA)

QQ群

![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL3VwbG9hZC1pbWFnZXMuamlhbnNodS5pby91cGxvYWRfaW1hZ2VzLzExODY2MDc4LWEzMWZmNDBhYzY4NTBhNmQ)

