package huanxing_print.com.cn.printhome.model.print;

/**
 * Created by LGH on 2017/3/23.
 */

public class PrintList {
    //{"data":[{"addTime":1490176358000,"colourFlag":1,"delFlag":0,"directionFlag":1,"doubleFlag":1,"fileName":"小文档
    // .docx","filePage":1,"fileUrl":"http://139.196.224.235:12003/file/df_/g1/M00/00/19/Ci
    // -4nVjSSWKAIesjAAAtMGbNrx432.docx","id":1642,"memberId":"100000091","printCount":1,"sizeType":0,"status":0,
    // "updateTime":1490176358000},{"addTime":1490175204000,"colourFlag":1,"delFlag":0,"directionFlag":1,
    // "doubleFlag":1,"fileName":"小文档.docx","filePage":1,
    // "fileUrl":"http://139.196.224.235:12003/file/df_/g1/M00/00/19/Ci-4nVjSROKAHP9DAAAtMGbNrx460.docx","id":1640,
    private long addTime;
    private int colourFlag;
    private int delFlag;
    private int directionFlag;
    private int doubleFlag;
    private String fileName;
    private int filePage;
    private String fileUrl;
    private int id;
    private int memberId;
    private int printCount;
    private int sizeType;
    private int status;
    private long updateTime;

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public int getColourFlag() {
        return colourFlag;
    }

    public void setColourFlag(int colourFlag) {
        this.colourFlag = colourFlag;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public int getDirectionFlag() {
        return directionFlag;
    }

    public void setDirectionFlag(int directionFlag) {
        this.directionFlag = directionFlag;
    }

    public int getDoubleFlag() {
        return doubleFlag;
    }

    public void setDoubleFlag(int doubleFlag) {
        this.doubleFlag = doubleFlag;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFilePage() {
        return filePage;
    }

    public void setFilePage(int filePage) {
        this.filePage = filePage;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getPrintCount() {
        return printCount;
    }

    public void setPrintCount(int printCount) {
        this.printCount = printCount;
    }

    public int getSizeType() {
        return sizeType;
    }

    public void setSizeType(int sizeType) {
        this.sizeType = sizeType;
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
