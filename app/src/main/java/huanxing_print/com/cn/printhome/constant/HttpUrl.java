package huanxing_print.com.cn.printhome.constant;

public class HttpUrl {

	public static final String POSTHTTP_HF = "http://192.168.1.171:8080/appserver/";// 海飞
	public static final String IMAGEHTTP_HF = "http://192.168.1.171:8080/resources/upload/image/";


	public static final String POSTHTTP_DAILY = "http://211.149.216.69/appserver/";// 测试
	public static final String IMAGEHTTP_DAILY = "http://211.149.216.69/resources/upload/image/";

	public static final String POSTHTTP_RELEASE = "http://www.huoyibang.com/appserver/";// 生产
	public static final String IMAGEHTTP_RELEASE = "http://www.huoyibang.com/resources/upload/image/";

	public static final String Login = "driver/driverLogin.do";// 登录
	public static final String LoginOut = "commons/logout.do";// 退出登录
	public static final String Register = "driver/driverRegister.do";// 注册
	public static final String getVeryCode = "commons/getVerCode.do"; // 获取短信验证码


	private static String postUrl;

	private static String imageUrl;

	private static HttpUrl httpUrl;

	public static String getPostUrl() {
		return postUrl;
	}

	public static void setPostUrl(String postUrl) {
		HttpUrl.postUrl = postUrl;
	}

	public static String getImageUrl() {
		return imageUrl;
	}

	public static void setImageUrl(String imageUrl) {
		HttpUrl.imageUrl = imageUrl;
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
			setImageUrl(IMAGEHTTP_RELEASE);
			break;
		case DAILY:
			setPostUrl(POSTHTTP_DAILY);
			setImageUrl(IMAGEHTTP_DAILY);
			break;
		case LOCAL_HF:
			setPostUrl(POSTHTTP_HF);
			setImageUrl(IMAGEHTTP_HF);
			break;
		default:
			break;
		}
	}

}
