package huanxing_print.com.cn.printhome.model.address;

public class DistrictModel {
	private String areaId;//地区id
	private String areaName;//地区名称
	private String level;//层级1省2市3区
	private String superAreaId;//上级id
	
	public DistrictModel() {
		super();
	}

	public DistrictModel(String areaId, String areaName, String level, String superAreaId) {
		super();
		this.areaId = areaId;
		this.areaName = areaName;
		this.level = level;
		this.superAreaId = superAreaId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getSuperAreaId() {
		return superAreaId;
	}

	public void setSuperAreaId(String superAreaId) {
		this.superAreaId = superAreaId;
	}

	@Override
	public String toString() {
		return "DistrictModel [areaId=" + areaId + ", areaName=" + areaName 
				+ ", level=" + level+ ", superAreaId=" + superAreaId+ "]";
	}

}
