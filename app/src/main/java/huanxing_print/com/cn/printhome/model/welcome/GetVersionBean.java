package huanxing_print.com.cn.printhome.model.welcome;

public class GetVersionBean {

	private String deployTime; // 发布日期 string
	private String downloadUrl;// 下载地址 string
	private String isForceUpdate;// 是否要强制更新 number
	private String isNew;// 是否是最新版本0 否 1 是最新版本，以下信息都可以忽略 string
	private String versionCode;// 版本号 string
	private String versionDetail;// 版本更新细节 string

	public String getDeployTime() {
		return deployTime;
	}

	public void setDeployTime(String deployTime) {
		this.deployTime = deployTime;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getIsForceUpdate() {
		return isForceUpdate;
	}

	public void setIsForceUpdate(String isForceUpdate) {
		this.isForceUpdate = isForceUpdate;
	}

	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionDetail() {
		return versionDetail;
	}

	public void setVersionDetail(String versionDetail) {
		this.versionDetail = versionDetail;
	}

	@Override
	public String toString() {
		return "GetVersionBean [deployTime=" + deployTime + ", downloadUrl="
				+ downloadUrl + ", isForceUpdate=" + isForceUpdate + ", isNew="
				+ isNew + ", versionCode=" + versionCode + ", versionDetail="
				+ versionDetail + "]";
	}

}
