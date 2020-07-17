# 介绍

这是一个桌面应用。


## 主要功能文件

MainFragment 主界面

HomeFragment 右滑界面 

ReportFragment 左滑界面

## 目录介绍

bean目录：存放实体类
moudle目录：存放业务
view目录：存放自定义UI控件
util目录：存放一些工具类



#### 旧的签名文件为：

        signingConfigs {
            release {
                keyAlias 'wyd'
                keyPassword '123456'
                storeFile file('wyd.jks')
                storePassword '123456'
            }
    
            debug {
                keyAlias 'vtech'
                keyPassword 'v123456'
                storeFile file('vtech.jks')
                storePassword 'v123456'
            }
        }
### 获取token

    public static final String AUTHORITY = "com.vtech.app.homeprovider";
    private Uri URI = Uri.parse("content://" + AUTHORITY);
    public static final String CMD_GET_TOKEN = "cmd_get_token";
    public static final String TOKEN = "token";
          
        Bundle bundle = getContext().getContentResolver().call(URI, CMD_GET_TOKEN, "", null);
        if (bundle != null) {
          String token = bundle.getString(TOKEN);
        }