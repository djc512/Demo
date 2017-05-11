package huanxing_print.com.cn.printhome.model.image;

public class ImageUploadItem {
	private String fileContent;
	private String fileId;
	private String fileName;
	private String fileType;
	public String getFileContent() {
		return fileContent;
	}
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	@Override
	public String toString() {
		return "ImageUploadItem [fileContent=" + fileContent + ", fileId="
				+ fileId + ", fileName=" + fileName + ", fileType=" + fileType
				+ "]";
	}

	
	
	
}
