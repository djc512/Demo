package huanxing_print.com.cn.printhome.model.login;

public class LoginBeanItem {
	private String userId;
	private String phone;
	private String sessionId;
	private String invitationCode;
	private String userName;
	private String modelsId;
	private String carModel;
	private String headImg;
	private String userType;
	private String approvalStatus;
	private String driverStatus;
	private String newEnergy;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getModelsId() {
		return modelsId;
	}

	public void setModelsId(String modelsId) {
		this.modelsId = modelsId;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getDriverStatus() {
		return driverStatus;
	}

	public void setDriverStatus(String driverStatus) {
		this.driverStatus = driverStatus;
	}

	
	public String getNewEnergy() {
		return newEnergy;
	}

	public void setNewEnergy(String newEnergy) {
		this.newEnergy = newEnergy;
	}

	@Override
	public String toString() {
		return "LoginBean [userId=" + userId + ", phone=" + phone + ", sessionId=" + sessionId 
				+ ", invitationCode=" + invitationCode + ", userName=" + userName +", modelsId=" + modelsId 
				+", carModel=" + carModel+", headImg=" + headImg +", userType=" + userType 
				+", approvalStatus=" + approvalStatus+", driverStatus=" + driverStatus +", newEnergy=" + newEnergy +"]";
	}

}
