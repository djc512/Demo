package huanxing_print.com.cn.printhome.constant;

import java.io.File;

import huanxing_print.com.cn.printhome.util.FileUtils;


public class ConFig {

    public static final Environment CURRENT_ENVIRONMENT = Environment.RELEASE;

    public static final String SHAREDPREFERENCES_NAME = "print_home";

    public static final int CONNECT_TIME_OUT = 1000 * 1000;

    public static final int FILE_CONNECT_TIME_OUT = 30 * 1000;

    public static final String IMG_CACHE_PATH = FileUtils.getSDCardPath() + "print_home" + File.separator + "img";

    public static final String IMG_SAVE = FileUtils.getSDCardPath() + "print_home" + File.separator + "save";
    public static final String FILE_SAVE = FileUtils.getSDCardPath() + "print_home" + File.separator + "file";

    public static final int IMG_CACHE_SIZE = 100 * 1024 * 1024;

    public static final String CLIENT_TYPE = "1";// 终端类型：Android ：1 IOS：2 PC：3

    public static final String CHANNEL_ID = "0";// 百度推送ID

    public static final String USER_TYPE = "2"; // 1:货主端 2：司机端

    public static final String VERCODE_TYPE = "2"; // 验证码类型1注册 2忘记密码

    public static final String VERSION_TYPE = "1"; //代码版本号

    public static final String PHONE_TYPE = "android"; //android端

    public static final String PAGESIZE = "10"; // 一页个数

    public static final long FILE_UPLOAD_MAX = 20 * 1024 * 1024;

    //微信登录
    public static final String spName = "WX_SP";
    public static final String CODE = "code";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String WXOPENID = "wxopenid";
    public static final String EXPIRES_IN = "expires_in";
    public static final String IS_AUTH_LOGIN = "is_auth_login";
}
