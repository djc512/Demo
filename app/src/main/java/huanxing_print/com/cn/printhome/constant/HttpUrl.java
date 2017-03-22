package huanxing_print.com.cn.printhome.constant;

public class HttpUrl {

	public static final String POSTHTTP_DAILY = "http://appprint.inkin.cc/";// 线下

	public static final String POSTHTTP_RELEASE = "http://www.huoyibang.com/";// 生产

	public static final String login = "member/login";// 登录
	public static final String loginWeiXin = "member/loginByWechat";// 微信登录
	//public static final String LoginOut = "commons/logout.do";// 退出登录
	public static final String register = "member/sign";// 注册
	public static final String getVeryCode = "common/getValidCode"; // 获取短信验证码
	public static final String resetPasswd = "member/resetPasswd"; // 重置密码
	public static final String fileUpload = "common/fileUpload"; // 文件上传
	public static final String versionCheck = "common/versionCheck"; // 文件上传
	public static final String userInfo = "member/getMemberInfo"; // 用户信息
	public static final String feedBack = "feedback/add"; // 反馈信息

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
		switch (ConFig.CURRENT_ENVIRONMENT) {
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
