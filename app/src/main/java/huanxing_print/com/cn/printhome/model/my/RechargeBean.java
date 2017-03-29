package huanxing_print.com.cn.printhome.model.my;

/**
 * Created by Administrator on 2017/3/29 0029.
 */

public class RechargeBean {

    /**
     * addTime : 2017-03-28 14:32:47
     * amount : 1
     * cashierNo : 20170328020348
     * channel : 支付宝
     * delFlag : 0
     * id : 10002106
     * memberId : 100000091
     * outOrderNo : 20170328020348
     * payTime : 1490682775000
     * status : 1
     * updateTime : 1490682774000
     */

    private String addTime;
    private String amount;
    private String cashierNo;
    private String channel;
    private int delFlag;
    private int id;
    private String memberId;
    private String outOrderNo;
    private long payTime;
    private int status;
    private long updateTime;

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCashierNo() {
        return cashierNo;
    }

    public void setCashierNo(String cashierNo) {
        this.cashierNo = cashierNo;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
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

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
