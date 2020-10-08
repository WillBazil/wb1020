package toolRental;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RentalAgreement {
	Tool tool;
	Integer rentalDays;
	Integer chargeDays;
	LocalDate checkOutDate;
	LocalDate dueDate;
	BigDecimal preDiscountCharge;
	Integer discountPercent;
	BigDecimal discountAmount;
	BigDecimal finalCharge;
	
	public Tool getTool() { return tool; }
	public void setTool(Tool tool) { this.tool = tool; }
	public Integer getRentalDays() { return rentalDays; }
	public void setRentalDays(Integer rentalDays) { this.rentalDays = rentalDays; }
	public Integer getChargeDays() { return chargeDays; }
	public void setChargeDays(Integer chargeDays) { this.chargeDays = chargeDays; }
	public LocalDate getCheckOutDate() { return checkOutDate; }
	public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }
	public LocalDate getDueDate() { return dueDate; }
	public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
	public BigDecimal getPreDiscountCharge() { return preDiscountCharge; }
	public void setPreDiscountCharge(BigDecimal preDiscountCharge) { this.preDiscountCharge = preDiscountCharge; }
	public Integer getDiscountPercent() { return discountPercent; }
	public void setDiscountPercent(Integer discountPercent) { this.discountPercent = discountPercent; }
	public BigDecimal getDiscountAmount() { return discountAmount; }
	public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
	public BigDecimal getFinalCharge() { return finalCharge; }
	public void setFinalCharge(BigDecimal finalCharge) { this.finalCharge = finalCharge; }
	
	public void printOut() {
		var formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		System.out.println("Tool code: "+this.getTool().getCode());
		System.out.println("Tool type: "+this.getTool().getType());
		System.out.println("Tool brand: "+this.getTool().getBrand());
		System.out.println("Rental days: "+this.getRentalDays());
		System.out.println("Check out date: "+this.getCheckOutDate().format(formatter));
		System.out.println("Due date: "+this.getDueDate().format(formatter));
		System.out.println("Charge days: "+this.getChargeDays());
		System.out.println("Pre-discount charge: $"+String.format("%,.2f", this.getPreDiscountCharge()));
		System.out.println("Discount percent: "+this.getDiscountPercent()+"%");
		System.out.println("Discount amount: $"+String.format("%,.2f", this.getDiscountAmount()));
		System.out.println("Final charge: $"+String.format("%,.2f", this.getFinalCharge()));
	}
}
