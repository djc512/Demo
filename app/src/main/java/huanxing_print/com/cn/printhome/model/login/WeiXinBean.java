package huanxing_print.com.cn.printhome.model.login;

public class WeiXinBean {
	private boolean loginFlag;//登录标记 true的时候有loginResult，FALSE时返回wechatId
	private LoginBean loginResult;
	private String wechatId;

	public boolean isLoginFlag() {
		return loginFlag;
	}

	public void setLoginFlag(boolean loginFlag) {
		this.loginFlag = loginFlag;
	}

	public LoginBean getLoginResult() {
		return loginResult;
	}

	public void setLoginResult(LoginBean loginResult) {
		this.loginResult = loginResult;
	}

	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}

	@Override
	public String toString() {
		return "WeiXinBean{" +
				"loginFlag=" + loginFlag +
				", loginResult=" + loginResult +
				", wechatId='" + wechatId + '\'' +
				'}';
	}
}
