package huanxing_print.com.cn.printhome.model.login;

public class LoginBean {
	private LoginBeanItem userInfo;

	public LoginBeanItem getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(LoginBeanItem userInfo) {
		this.userInfo = userInfo;
	}

	@Override
	public String toString() {
		return "LoginBean [userInfo=" + userInfo + "]";
	}

}
