package toolRental;

public class Tool {
	String type;
	String brand;
	String code;
	ToolDetail toolDetail;
	
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }
	public String getBrand() { return brand; }
	public void setBrand(String brand) { this.brand = brand; }
	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }
	public ToolDetail getToolDetail() { return toolDetail; }
	public void setToolDetail(ToolDetail toolDetail) { this.toolDetail = toolDetail; }
}
