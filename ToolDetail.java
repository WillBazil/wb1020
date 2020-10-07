package toolRental;

import java.math.BigDecimal;

public class ToolDetail {
	String toolType;
	BigDecimal dailyCharge;
	Boolean weekdayCharge;
	Boolean weekendCharge;
	Boolean holidayCharge;
	
	public String getToolType() { return toolType; }
	public void setToolType(String toolType) { this.toolType = toolType; }
	public BigDecimal getDailyCharge() { return dailyCharge; }
	public void setDailyCharge(BigDecimal dailyCharge) { this.dailyCharge = dailyCharge; }
	public Boolean isWeekdayCharge() { return weekdayCharge; }
	public void setWeekdayCharge(Boolean weekdayCharge) { this.weekdayCharge = weekdayCharge; }
	public Boolean isWeekendCharge() { return weekendCharge; }
	public void setWeekendCharge(Boolean weekendCharge) { this.weekendCharge = weekendCharge; }
	public Boolean isHolidayCharge() { return holidayCharge; }
	public void setHolidayCharge(Boolean holidayCharge) { this.holidayCharge = holidayCharge; }
}
