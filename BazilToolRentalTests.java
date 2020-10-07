package toolRental;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

class BazilToolRentalTests {

	//Fails because a discount of 101% is invalid
	@Test
	public final void testBazilToolRental_JAKR_9_13_15() throws Exception {
		try{
			BazilToolRental.run("JAKR", "9/3/15", 5, 101);
			fail();
		} catch (Exception e) {
			assert(e.getMessage().equals("Discounts can't be over 100%. Passed value is (101)"));
		}
	}

	//Succeeds because input is valid and...
	//Fri the 3rd is not charged because it is the date taken for the holiday of the 4th
	//Sat-Sun the 4th-5th are charged because ladders have weekend charges, leaving 2 total charge days
	//at $1.99 a day, charged for 2 days prior to discount, $3.98 is owed
	//10% of that is $0.398, rounded up to $0.40 leaving $3.58 owed
	@Test
	public final void testBazilToolRental_LADW_7_2_20() throws Exception {
		var rentalAgreement = BazilToolRental.run("LADW", "7/2/20", 3, 10);
		assert(rentalAgreement.getTool().getCode().equals("LADW"));
		assert(rentalAgreement.getTool().getType().equals("LADDER"));
		assert(rentalAgreement.getTool().getBrand().equals("WERNER"));
		assert(rentalAgreement.getRentalDays().equals(3));
		assert(rentalAgreement.getCheckOutDate().equals(LocalDate.parse("07/02/2020", DateTimeFormatter.ofPattern("MM/dd/yyyy"))));
		assert(rentalAgreement.getDueDate().equals(LocalDate.parse("07/05/2020", DateTimeFormatter.ofPattern("MM/dd/yyyy"))));
		assert(rentalAgreement.getChargeDays().equals(2));
		assert(rentalAgreement.getPreDiscountCharge().equals(BigDecimal.valueOf(3.98).setScale(2)));
		assert(rentalAgreement.getDiscountPercent().equals(10));
		assert(rentalAgreement.getDiscountAmount().equals(BigDecimal.valueOf(.4).setScale(2)));
		assert(rentalAgreement.getFinalCharge().equals(BigDecimal.valueOf(3.58).setScale(2)));
	}

//	//Succeeds because input is valid and...
//	//Fri the 3rd is charged because while it is the date taken for the holiday of the 4th, chainsaws are charged on holidays
//	//Sat-Sun the 4th-5th are not charged because chainsaws don't have weekend charges
//	//Mon-Tue the 6th-7th are charged as normal weekdays, leaving 3 total charge days
//	//at $1.49 a day, charged for 3 days prior to discount, $4.47 is owed
//	//25% of that is $1.1175, rounded up to $1.12 leaving $3.35 owed
	@Test
	public final void testBazilToolRental_CHNS_7_2_15() throws Exception {
		var rentalAgreement = BazilToolRental.run("CHNS", "7/2/15", 5, 25);
		assert(rentalAgreement.getTool().getCode().equals("CHNS"));
		assert(rentalAgreement.getTool().getType().equals("CHAINSAW"));
		assert(rentalAgreement.getTool().getBrand().equals("STIHL"));
		assert(rentalAgreement.getRentalDays().equals(5));
		assert(rentalAgreement.getCheckOutDate().equals(LocalDate.parse("07/02/2015", DateTimeFormatter.ofPattern("MM/dd/yyyy"))));
		assert(rentalAgreement.getDueDate().equals(LocalDate.parse("07/07/2015", DateTimeFormatter.ofPattern("MM/dd/yyyy"))));
		assert(rentalAgreement.getChargeDays().equals(3));
		assert(rentalAgreement.getPreDiscountCharge().equals(BigDecimal.valueOf(4.47).setScale(2)));
		assert(rentalAgreement.getDiscountPercent().equals(25));
		assert(rentalAgreement.getDiscountAmount().equals(BigDecimal.valueOf(1.12).setScale(2)));
		assert(rentalAgreement.getFinalCharge().equals(BigDecimal.valueOf(3.35).setScale(2)));
	}
	
	//Succeeds because input is valid and...
	//Fri the 4th is charged as a normal weekday
	//Sat-Sun the 5th-6th are not charged because jackhammers don't have weekend charges
	//Mon the 7th is not charged because it is labor day
	//Tue-Wed the 8th-9th are charged as normal weekdays, leaving 3 total charge days
	//at $2.99 a day, charged for 3 days with no discount, $8.97 is owed
	@Test
	public final void testBazilToolRental_JAKD_9_3_15() throws Exception {
		var rentalAgreement = BazilToolRental.run("JAKD", "9/3/15", 6, 0);
		assert(rentalAgreement.getTool().getCode().equals("JAKD"));
		assert(rentalAgreement.getTool().getType().equals("JACKHAMMER"));
		assert(rentalAgreement.getTool().getBrand().equals("DEWALT"));
		assert(rentalAgreement.getRentalDays().equals(6));
		assert(rentalAgreement.getCheckOutDate().equals(LocalDate.parse("09/03/2015", DateTimeFormatter.ofPattern("MM/dd/yyyy"))));
		assert(rentalAgreement.getDueDate().equals(LocalDate.parse("09/09/2015", DateTimeFormatter.ofPattern("MM/dd/yyyy"))));
		assert(rentalAgreement.getChargeDays().equals(3));
		assert(rentalAgreement.getPreDiscountCharge().equals(BigDecimal.valueOf(8.97).setScale(2)));
		assert(rentalAgreement.getDiscountPercent().equals(0));
		assert(rentalAgreement.getDiscountAmount().equals(BigDecimal.valueOf(0).setScale(2)));
		assert(rentalAgreement.getFinalCharge().equals(BigDecimal.valueOf(8.97).setScale(2)));
	}
	
	//Succeeds because input is valid and...
	//Fri the 3rd is not charged because it is the date taken for the holiday of the 4th
	//Sat-Sun the 4th-5th are not charged because jackhammers don't have weekend charges
	//Mon-Fri the 6th-10th are charged as normal weekdays
	//Sat the 11th is not charged because jackhammers don't have weekend charges, leaving 5 total charge days
	//at $2.99 a day, charged for 5 days with no discount, $14.95 is owed
	@Test
	public final void testBazilToolRental_JAKR_7_2_15() throws Exception {
		var rentalAgreement = BazilToolRental.run("JAKR", "7/2/15", 9, 0);
		assert(rentalAgreement.getTool().getCode().equals("JAKR"));
		assert(rentalAgreement.getTool().getType().equals("JACKHAMMER"));
		assert(rentalAgreement.getTool().getBrand().equals("RIDGID"));
		assert(rentalAgreement.getRentalDays().equals(9));
		assert(rentalAgreement.getCheckOutDate().equals(LocalDate.parse("07/02/2015", DateTimeFormatter.ofPattern("MM/dd/yyyy"))));
		assert(rentalAgreement.getDueDate().equals(LocalDate.parse("07/11/2015", DateTimeFormatter.ofPattern("MM/dd/yyyy"))));
		assert(rentalAgreement.getChargeDays().equals(5));
		assert(rentalAgreement.getPreDiscountCharge().equals(BigDecimal.valueOf(14.95).setScale(2)));
		assert(rentalAgreement.getDiscountPercent().equals(0));
		assert(rentalAgreement.getDiscountAmount().equals(BigDecimal.valueOf(0).setScale(2)));
		assert(rentalAgreement.getFinalCharge().equals(BigDecimal.valueOf(14.95).setScale(2)));
	}

	//Succeeds because input is valid and...
	//Sun the 1st is not charged because jackhammers don't have weekend charges
	//Mon the 2nd is not charged because it is labor day
	//Tue-Wed the 3th-4th are charged as normal weekdays, leaving 2 total charge days
	//at $2.99 a day, charged for 2 days prior to discount, $5.98 is owed
	//50% of that is $2.99, leaving $2.99 owed
	@Test
	public final void testBazilToolRental_JAKR_8_31_19() throws Exception {
		var rentalAgreement = BazilToolRental.run("JAKR", "8/31/19", 4, 50);
		assert(rentalAgreement.getTool().getCode().equals("JAKR"));
		assert(rentalAgreement.getTool().getType().equals("JACKHAMMER"));
		assert(rentalAgreement.getTool().getBrand().equals("RIDGID"));
		assert(rentalAgreement.getRentalDays().equals(4));
		assert(rentalAgreement.getCheckOutDate().equals(LocalDate.parse("08/31/2019", DateTimeFormatter.ofPattern("MM/dd/yyyy"))));
		assert(rentalAgreement.getDueDate().equals(LocalDate.parse("09/04/2019", DateTimeFormatter.ofPattern("MM/dd/yyyy"))));
		assert(rentalAgreement.getChargeDays().equals(2));
		assert(rentalAgreement.getPreDiscountCharge().equals(BigDecimal.valueOf(5.98).setScale(2)));
		assert(rentalAgreement.getDiscountPercent().equals(50));
		assert(rentalAgreement.getDiscountAmount().equals(BigDecimal.valueOf(2.99).setScale(2)));
		assert(rentalAgreement.getFinalCharge().equals(BigDecimal.valueOf(2.99).setScale(2)));
	}

}
