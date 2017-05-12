package huanxing_print.com.cn.printhome.model.approval;

import java.util.ArrayList;

/**
 * 审批详情
 * Created by dd on 2017/5/9.
 */

public class ApprovalDetail {
    private String addTime;//创建时间
    private String amountMonney;//金额
    private String approveId;//	申请单号
    private ArrayList<ApprovalOrCopy> approverList;//审核列表
    private ArrayList<String> attachmentList;//附件列表
    private String bankAccount;//收款方账号
    private String bankName;//收款方开户行
    private String bankPerson;//收款仿全称
    private String department;//部门
    private String finishTime;//完成时间
    private String jobNumber;//工号
    private String purchaseList;//采购清单
    private String remark;//标注
    private int status;//状态
    private ArrayList<SubFormItem> subFormList;//报销条目
    private String title;//名称
    private int type;//类别  类型1.采购 2报销

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getAmountMonney() {
        return amountMonney;
    }

    public void setAmountMonney(String amountMonney) {
        this.amountMonney = amountMonney;
    }

    public String getApproveId() {
        return approveId;
    }

    public void setApproveId(String approveId) {
        this.approveId = approveId;
    }

    public ArrayList<ApprovalOrCopy> getApproverList() {
        return approverList;
    }

    public void setApproverList(ArrayList<ApprovalOrCopy> approverList) {
        this.approverList = approverList;
    }

    public ArrayList<String> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(ArrayList<String> attachmentList) {
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

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
}
