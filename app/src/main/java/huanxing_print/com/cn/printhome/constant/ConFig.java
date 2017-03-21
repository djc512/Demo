package huanxing_print.com.cn.printhome.constant;

import java.io.File;

import cn.com.huanxing.store.util.FileUtils;


public class ConFig {

	public static final Environment CURRENT_ENVIRONMENT = Environment.DAILY;

	public static final int CONNECT_TIME_OUT = 15 * 1000;

	public static final int FILE_CONNECT_TIME_OUT = 30 * 1000;

	public static final String IMG_CACHE_PATH =FileUtils.getSDCardPath() + "tuniu_store" + File.separator + "img";
	
	public static final String IMG_SAVE =FileUtils.getSDCardPath() + "tuniu_store" + File.separator + "save";

	public static final int IMG_CACHE_SIZE = 100 * 1024 * 1024;

	public static final String CLIENT_TYPE = "1";// 终端类型：Android ：1 IOS：2 PC：3

	public static final String CHANNEL_ID = "0";// 百度推送ID

	public static final String USER_TYPE = "2"; // 1:货主端 2：司机端
	
	public static final String VERCODE_TYPE = "2"; // 验证码类型1注册 2忘记密码
		
	public static final String VERSION_TYPE = "1"; //代码版本号
	
	public static final String PHONE_TYPE = "android"; //android端

	public static final String PAGESIZE = "10"; // 一页个数

}
