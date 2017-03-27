package huanxing_print.com.cn.printhome.model.my;

public class PrinterInfoBean {

	private String addTime;
    private int id;
	private String memberId;// 会员id
	private String pageCount;// 打印页数
	private String printerAddress;//打印机地址
	private int printerDef;//默认打印机0否 1是
	private String printerNo;//打印机id
	private String remark;//打印机名称
    private String remarkCount;//评论数量
	private boolean status;//状态 true-可用 false-不可用
	private String updateTime;//

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}

	public String getPrinterAddress() {
		return printerAddress;
	}

	public void setPrinterAddress(String printerAddress) {
		this.printerAddress = printerAddress;
	}

	public int getPrinterDef() {
		return printerDef;
	}

	public void setPrinterDef(int printerDef) {
		this.printerDef = printerDef;
	}

	public String getPrinterNo() {
		return printerNo;
	}

	public void setPrinterNo(String printerNo) {
		this.printerNo = printerNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemarkCount() {
		return remarkCount;
	}

	public void setRemarkCount(String remarkCount) {
		this.remarkCount = remarkCount;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "PrinterInfoBean{" +
				"addTime='" + addTime + '\'' +
				", id=" + id +
				", memberId='" + memberId + '\'' +
				", pageCount='" + pageCount + '\'' +
				", printerAddress='" + printerAddress + '\'' +
				", printerDef=" + printerDef +
				", printerNo='" + printerNo + '\'' +
				", remark='" + remark + '\'' +
				", remarkCount='" + remarkCount + '\'' +
				", status=" + status +
				", updateTime='" + updateTime + '\'' +
				'}';
	}
}
