package huanxing_print.com.cn.printhome.model.approval;

import java.util.ArrayList;

/**
 * 提交的审批对象
 * Created by dd on 2017/5/12.
 */

public class AddApprovalObject {
    private String amountMonney;//总金额
    private ArrayList<Approver> approverList;//审批人列表
    private ArrayList<ImageUrl> attachmentList;//附件列表
    private String bankAccount;//收款方账号
    private String bankName;//收款方开户行
    private String bankPerson;//收款方全称
    private ArrayList<Approver> copyerList;//抄送人列表
    private String department;//部门
    private String finishTime;//完成时间
    private String purchaseList;//采购清单
    private String remark;//备注
    private ArrayList<SubFormItem> subFormList;//报销条目
    private String title;//标题
    private String groupId;//groupId
    private int type;//类型  1.采购 2报销

    public String getAmountMonney() {
        return amountMonney;
    }

    public void setAmountMonney(String amountMonney) {
        this.amountMonney = amountMonney;
    }

    public ArrayList<Approver> getApproverList() {
        return approverList;
    }

    public void setApproverList(ArrayList<Approver> approverList) {
        this.approverList = approverList;
    }

    public ArrayList<ImageUrl> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(ArrayList<ImageUrl> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankPerson() {
        return bankPerson;
    }

    public void setBankPerson(String bankPerson) {
        this.bankPerson = bankPerson;
    }

    public ArrayList<Approver> getCopyerList() {
        return copyerList;
    }

    public void setCopyerList(ArrayList<Approver> copyerList) {
        this.copyerList = copyerList;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getPurchaseList() {
        return purchaseList;
    }

    public void setPurchaseList(String purchaseList) {
        this.purchaseList = purchaseList;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ArrayList<SubFormItem> getSubFormList() {
        return subFormList;
    }

    public void setSubFormList(ArrayList<SubFormItem> subFormList) {
        this.subFormList = subFormList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
