package huanxing_print.com.cn.printhome.constant;

public class HttpUrl {

	public static final String test = "http://106.14.77.102:22012/";//测试的，正式环境替换

	public static final String POSTHTTP_DAILY = "http://106.14.77.102:22012/";// 线下

	public static final String POSTHTTP_RELEASE = "http://www.huoyibang.com/";// 生产

	public static final String login = "member/login";// 登录
	public static final String loginWeiXin = "member/loginByWechat";// 微信登录
	public static final String LoginOut = "member/signOut";// 退出登录
	public static final String register = "member/sign";// 注册
	public static final String getVeryCode = "common/getValidCode"; // 获取短信验证码
	public static final String resetPasswd = "member/resetPasswd"; // 重置密码
	public static final String fileUpload = "common/fileUpload"; // 文件上传
	public static final String versionCheck = "common/versionCheck"; // 版本检查
	public static final String userInfo = "member/getMemberInfo"; // 用户信息
	public static final String updateInfo = "member/updateMember"; // 修改用户信息
	public static final String feedBack = "feedback/add"; // 反馈信息
	public static final String chongzhi = "pay/recharge/queryConfig"; // 充值接口
	public static final String myinfo = "member/getBalance"; //余额查询
	public static final String czRecord = "pay/recharge/queryOrder"; // 充值记录接口
	public static final String normalDebit = "pay/bill/addCommBill"; // 普通发票接口
	public static final String valueDebit = "pay/bill/addVATBill"; // 增值发票接口
	public static final String mxDetail = "/pay/queryConsume"; // 账单明细接口
	public static final String payOrderId = "pay/recharge/addOrder"; // 获取充值订单号接口
	public static final String doPay = "pay/doAlipay"; // 跳转支付宝的接口
	public static final String dyList = "order/getPrintHistory"; //打印订单列表接口
	public static final String printerList = "print/printer/history"; // 最近使用的打印机列表
	public static final String setDefaultprinter= "print/printer/setDefault"; // 设置默认打印机
	public static final String go2Debit= "pay/bill/getBillAmount"; // 获取能否开发票的接口
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
