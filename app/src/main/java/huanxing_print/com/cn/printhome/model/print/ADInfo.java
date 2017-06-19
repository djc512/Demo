package huanxing_print.com.cn.printhome.model.print;

/**
 * 描述：广告信息</br>
 */
public class ADInfo {
	
	String imageUrl ;//图片地址
	String linkUrl ;//打开链接地址，为空或者为null时不可点击，否则加loginToken跳转地址

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
}
