package huanxing_print.com.cn.printhome.model.login;

public class RegisterBean {
	private String addTime;
	private String id;
	private String memberId;// 会员id
	private String memberType;// 会员类型：0-普通会员，1-正式店，2-品牌商城，3-供应商
	private String mobileNumber;
	private String name;
	private String nickName;
	private String referrerMid;//上级id
	private String status;// 会员状态，0-有效，1-已注销
	private String updateTime;
	private String wechatId;// 微信id

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
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

	public String getReferrerMid() {
		return referrerMid;
	}

	public void setReferrerMid(String referrerMid) {
		this.referrerMid = referrerMid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}

	@Override
	public String toString() {
		return "RegisterBean [addTime=" + addTime + ", id=" + id
				+ ", memberId=" + memberId + ", memberType=" + memberType
				+ ", mobileNumber=" + mobileNumber + ", name=" + name
				+ ", nickName=" + nickName + ", referrerMid=" + referrerMid
				+ ", status=" + status + ", updateTime=" + updateTime
				+ ", wechatId=" + wechatId + "]";
	}

}
