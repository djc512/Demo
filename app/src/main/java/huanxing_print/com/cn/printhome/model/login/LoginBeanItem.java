package huanxing_print.com.cn.printhome.model.login;

public class LoginBeanItem {
	private String easemobId;//环信聊天用户id
	private String faceUrl;
	private String memberId;// 会员id
	//private String memberType;// 会员类型：0-普通会员，1-正式店，2-品牌商城，3-供应商
	private String mobileNumber;
	private String name;//名称
	private String nickName;//昵称
	private String uniqueId;//印家号
	private String wechatId;// 微信id
	private String wechatName;// 微信昵称

	public String getEasemobId() {
		return easemobId;
	}

	public void setEasemobId(String easemobId) {
		this.easemobId = easemobId;
	}

	public String getFaceUrl() {
		return faceUrl;
	}

	public void setFaceUrl(String faceUrl) {
		this.faceUrl = faceUrl;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}

	public String getWechatName() {
		return wechatName;
	}

	public void setWechatName(String wechatName) {
		this.wechatName = wechatName;
	}
}
