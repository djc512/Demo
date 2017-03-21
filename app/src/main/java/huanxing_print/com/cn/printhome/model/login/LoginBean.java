package huanxing_print.com.cn.printhome.model.login;

public class LoginBean {
	private String loginToken;//登录校验token，需要登录的接口需要校验该token
	private LoginBeanItem memberInfo;	
	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public LoginBeanItem getMemberInfo() {
		return memberInfo;
	}

	public void setMemberInfo(LoginBeanItem memberInfo) {
		this.memberInfo = memberInfo;
	}

	@Override
	public String toString() {
		return "LoginBean [loginToken=" + loginToken +",memberInfo=" + memberInfo + "]";
	}

}
