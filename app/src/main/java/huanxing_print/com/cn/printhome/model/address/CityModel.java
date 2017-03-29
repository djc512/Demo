package huanxing_print.com.cn.printhome.model.address;

import java.util.List;

public class CityModel {
	private String areaId;//地区id
	private String areaName;//地区名称
	private String level;//层级1省2市3区
	private String superAreaId;//上级id
	private List<DistrictModel> children;//子数组
	
	public CityModel() {
		super();
	}

	public CityModel(String areaId, String areaName, String level, String superAreaId, List<DistrictModel> children) {
		super();
		this.areaId = areaId;
		this.areaName = areaName;
		this.level = level;
		this.superAreaId = superAreaId;
		this.children = children;
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

	public List<DistrictModel> getChildren() {
		return children;
	}

	public void setChildren(List<DistrictModel> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return "CityModel [areaId=" + areaId + ", areaName=" + areaName+ ", level=" + level
				+ ", superAreaId=" + superAreaId+ ", children=" + children+ "]";
	}
	
}
