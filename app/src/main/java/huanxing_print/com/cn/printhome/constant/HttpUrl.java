package huanxing_print.com.cn.printhome.constant;

public class HttpUrl {

	public static final String POSTHTTP_DAILY = "http://appprint.inkin.cc/";// 线下

	public static final String POSTHTTP_RELEASE = "http://www.huoyibang.com/";// 生产

	public static final String Login = "driver/driverLogin.do";// 登录
	public static final String LoginOut = "commons/logout.do";// 退出登录
	public static final String Register = "driver/driverRegister.do";// 注册
	public static final String getVeryCode = "commons/getVerCode.do"; // 获取短信验证码


	private static String postUrl;


	private static HttpUrl httpUrl;

	public static String getPostUrl() {
		return postUrl;
	}

	public static void setPostUrl(String postUrl) {
		HttpUrl.postUrl = postUrl;
	}


	public static HttpUrl getInstance() {
		if (null != httpUrl) {
			httpUrl = new HttpUrl();
		}
		initEnvironment();
		return httpUrl;

	}

	private static void initEnvironment() {
		switch (Config.CURRENT_ENVIRONMENT) {
		case RELEASE:
			setPostUrl(POSTHTTP_RELEASE);
			break;
		case DAILY:
			setPostUrl(POSTHTTP_DAILY);
			break;
		default:
			break;
		}
	}

}
