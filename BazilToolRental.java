package toolRental;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BazilToolRental {


	public static void main(String[] args) throws Exception {
		run();
	}

	//Run with no passed in inputs
	public static RentalAgreement run() throws Exception {
		//chose to only import files once since it is being pulled from a txt file.
		//under normal circumstances a DB would be hit on each request
		var toolDetails = importToolDetailsFile(new File("C:\\Dev\\workspace\\Workspace\\BazilToolRental\\src\\toolRental\\toolTypeDetails.txt"));
		var tools = importToolsFile(new File("C:\\Dev\\workspace\\workspace\\BazilToolRental\\src\\toolRental\\tools.txt"), toolDetails);
		var rentalAgreement = checkOut(gatherInput(tools));
		rentalAgreement.printOut();
		return rentalAgreement;
	}

	//Run with passed in inputs
	public static RentalAgreement run(String toolCode, String checkoutDate, Integer rentalDays, Integer discountPercent) throws Exception {
		var toolDetails = importToolDetailsFile(new File("C:\\Dev\\workspace\\Workspace\\BazilToolRental\\src\\toolRental\\toolTypeDetails.txt"));
		var tools = importToolsFile(new File("C:\\Dev\\workspace\\workspace\\BazilToolRental\\src\\toolRental\\tools.txt"), toolDetails);
		var rentalAgreement = checkOut(insertPassedInput(toolCode, checkoutDate, rentalDays, discountPercent, tools));
		rentalAgreement.printOut();
		return rentalAgreement;
	}
	
	//import data from tools.txt
	private static HashMap<String, Tool> importToolsFile(File toolsFile, HashMap<String, ToolDetail> toolDetails) throws Exception {
		var tools = new HashMap<String, Tool>();
		try {
			var br = new BufferedReader(new FileReader(toolsFile));
			var headers = br.readLine().split("\\|");//file is | delimited
			String line;
			while ((line = br.readLine()) != null) {
				var columns = line.split("\\|");
				var tool = new Tool();
				for(var i = 0; i < columns.length; i++) {
					var record = columns[i];
					if(headers[i].equals("Tool_Type")) {
						tool.setType(record.toUpperCase());
					} else if(headers[i].equals("Brand")) {
						tool.setBrand(record.toUpperCase());
					} else if(headers[i].equals("Tool_Code")) {
						tool.setCode(record.toUpperCase());
					}
					if(i == columns.length-1) {
						if(toolDetails.get(tool.getType()) == null) {
							br.close();
							throw new Exception("Tool detail record couldn't be found for tool type of "+tool.getType());
						}
						tool.setToolDetail(toolDetails.get(tool.getType()));
						if(tools.containsKey(tool.getCode())) {
							br.close();
							throw new Exception("More than one tool found with Tool Code of "+tool.getCode());
						}
						tools.put(tool.getCode(), tool);
					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tools;//map is returned to make look ups constant time
	}

	//import data from toolTypeDetails.txt
	private static HashMap<String, ToolDetail> importToolDetailsFile(File toolDetailsFile) throws Exception {
		var toolDetails = new HashMap<String, ToolDetail>();
		try {
			var br = new BufferedReader(new FileReader(toolDetailsFile));
			var headers = br.readLine().split("\\|");//file is | delimited
			String line;
			while ((line = br.readLine()) != null) {
				var columns = line.split("\\|");
				var toolDetail = new ToolDetail();
				for(var i = 0; i < columns.length; i++) {
					var record = columns[i];
					if(headers[i].equals("Tool_Type")) {
						toolDetail.setType(record.toUpperCase());
					} if(headers[i].equals("Daily_Charge")) {
						try {
							toolDetail.setDailyCharge(BigDecimal.valueOf(Double.parseDouble(record)));
						} catch (Exception e) {
							br.close();
							throw new Exception("Given daily charge can't be parsed into a BigDecimal "+record);
						}
					} else if(headers[i].equals("Weekday_Charge")) {
						toolDetail.setWeekdayCharge(record.equalsIgnoreCase("Y"));
					} else if(headers[i].equals("Weekend_Charge")) {
						toolDetail.setWeekendCharge(record.equalsIgnoreCase("Y"));
					} else if(headers[i].equals("Holiday_Charge")) {
						toolDetail.setHolidayCharge(record.equalsIgnoreCase("Y"));
					}
					if(i == columns.length-1) {
						toolDetails.put(toolDetail.getType(), toolDetail);
					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return toolDetails;//map is returned to make look ups constant time
	}
	
	//gather checkout input and insert into a RentalAgreement object
	private static RentalAgreement gatherInput(HashMap<String, Tool> tools) throws Exception {
		var rentalAgreement = new RentalAgreement();
		var scanner = new Scanner(System.in);
		System.out.println("Enter Tool Code");
		var toolCode = scanner.nextLine().toUpperCase();
		while(!tools.keySet().contains(toolCode)) { //if entered value wasn't imported prompt for valid tool code and show list of valid codes
			System.out.println("Invalid Tool Code. Valids Codes : "+tools.keySet());
			toolCode = scanner.nextLine().toUpperCase();
		}
		rentalAgreement.setTool(tools.get(toolCode));
		System.out.println("Rental Day Count");
		var rentalDays = scanner.nextLine();
		while(!rentalDays.matches("[0-9]+")) { //if entered value contains any non digits prompt for a positive whole number
			System.out.println("Please enter a positive whole number for the rental day count.");
			rentalDays = scanner.nextLine();
		}
		rentalAgreement.setRentalDays(Integer.parseInt(rentalDays));
		if(rentalAgreement.getRentalDays() < 1) { //throw exception if only a 0 is entered
			scanner.close();
			throw new Exception("Tools must be rented for atleast one day.");
		}
		System.out.println("Discount Percent (0-100)");
		var discountPercent = scanner.nextLine();
		while(!discountPercent.matches("[0-9]+")) { //if entered value contains any non digits prompt for a positive whole number
			System.out.println("Please enter a positive whole number between 0 and 100 for the discount percent.");
			discountPercent = scanner.nextLine();
		}
		rentalAgreement.setDiscountPercent(Integer.parseInt(discountPercent));
		if(rentalAgreement.getDiscountPercent() < 0) { //throw exception if entered discount percent is outside of acceptable range
			scanner.close();
			throw new Exception("Discounts can't be less than 0%.");
		} else if(rentalAgreement.getDiscountPercent() > 100) {
			scanner.close();
			throw new Exception("Discounts can't be more than 100%.");
		}
		var formatter = DateTimeFormatter.ofPattern("M/d/[yyyy][yy]"); //allows for dates with one or two digit months and days, as well as years with four or two digits. So 1/1/20, 01/01/2020, 1/01/20, etc. are all valid
		LocalDate startDate = null;
		var dateParsed = false;
		System.out.println("Check Out Date (mm/dd/yyyy)");
		while(!dateParsed) {
			try {
				startDate = LocalDate.parse(scanner.nextLine(), formatter);
				dateParsed = true; //input was successfully parsed into a date, set flag as true to break out of the while loop.
			} catch (Exception e) {
				// entered date is in an invalid format. Stuff the error and prompt again.
				System.out.println("Entered date is in invalid format. Please enter a date with a format of mm/dd/yyyy.");
			}
		}
		rentalAgreement.setCheckOutDate(startDate);
		scanner.close();
		return rentalAgreement;
	}
	
	//insert passed data into RentalAgreement object
	private static RentalAgreement insertPassedInput(String toolCode, String checkoutDate, Integer rentalDays, Integer discountPercent, HashMap<String, Tool> tools) throws Exception {
		var rentalAgreement = new RentalAgreement();
		toolCode = toolCode.toUpperCase();
		if(!tools.keySet().contains(toolCode)) { //if passed tool code wasn't imported throw error
			throw new Exception("Invalid Tool Code ("+toolCode+"). Valids Codes : "+tools.keySet());
		}
		rentalAgreement.setTool(tools.get(toolCode));
		if(rentalDays < 1) { //if passed rental days is less than 1 throw error
			throw new Exception("Tools must be rented for atleast one day. Passed value is ("+rentalDays+")");
		}
		rentalAgreement.setRentalDays(rentalDays);
		if(discountPercent < 0) { //if passed discount percent isn't in range of 0-100 throw error
			throw new Exception("Discounts can't be less than 0%. Passed value is ("+discountPercent+")");
		} else if(discountPercent > 100) {
			throw new Exception("Discounts can't be over 100%. Passed value is ("+discountPercent+")");
		}
		rentalAgreement.setDiscountPercent(discountPercent);
		try {
			rentalAgreement.setCheckOutDate(LocalDate.parse(checkoutDate, DateTimeFormatter.ofPattern("M/d/[yyyy][yy]")));
		} catch (Exception e) { //if passed checkout date can't be parsed throw error.
			throw new Exception("Invalid date format ("+checkoutDate+"). Date must be in format of mm/dd/yyyy.");
		}
		return rentalAgreement;
	}
	
	//perform calculations on input data
	private static RentalAgreement checkOut(RentalAgreement rentalAgreement) throws Exception {
		rentalAgreement.setDueDate(rentalAgreement.getCheckOutDate().plusDays(rentalAgreement.getRentalDays()));
		var chargeDays = 0;
		var weekendDays = Stream.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).collect(Collectors.toCollection(HashSet::new)); //set of days in weekend
		var startDate = rentalAgreement.getCheckOutDate().plusDays(1); //start checking for charge days on the day following checkout
		while(startDate.isBefore(rentalAgreement.getDueDate()) || startDate.isEqual(rentalAgreement.getDueDate())) { //check for charge days up to and including the due date
			if(((rentalAgreement.getTool().getToolDetail().isWeekdayCharge() && !weekendDays.contains(startDate.getDayOfWeek())) || //If the date is a chargeable weekday or...
					(rentalAgreement.getTool().getToolDetail().isWeekendCharge() && weekendDays.contains(startDate.getDayOfWeek()))) && //the date is a chargeable weekend and...
					(rentalAgreement.getTool().getToolDetail().isHolidayCharge() || !isHoliday(startDate))) { //holidays can be charged or they can't and the date is a holiday
				chargeDays ++;
			}
			startDate = startDate.plusDays(1);
		}
		rentalAgreement.setChargeDays(chargeDays);
		rentalAgreement.setPreDiscountCharge(rentalAgreement.getTool().getToolDetail().getDailyCharge().multiply(BigDecimal.valueOf(rentalAgreement.getChargeDays())).setScale(2, RoundingMode.HALF_UP));
		rentalAgreement.setDiscountAmount(rentalAgreement.getPreDiscountCharge().multiply(BigDecimal.valueOf(rentalAgreement.getDiscountPercent()).multiply(BigDecimal.valueOf(0.01))).setScale(2, RoundingMode.HALF_UP));
		rentalAgreement.setFinalCharge(rentalAgreement.getPreDiscountCharge().subtract(rentalAgreement.getDiscountAmount()));
		return rentalAgreement;
	}
	
	//check if given date is a holiday
	private static Boolean isHoliday(LocalDate date) {
		return ((date.getMonth().equals(Month.SEPTEMBER) && date.getDayOfWeek().equals(DayOfWeek.MONDAY) && date.getDayOfMonth() <= 7) || //Labor day (month is september, day is monday, day is in the first 7 of september)
				date.getMonth().equals(Month.JULY) && //4th of July (month is July and...
				((date.getDayOfMonth() == 4 && !Stream.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).collect(Collectors.toCollection(HashSet::new)).contains(date.getDayOfWeek())) || //it is the 4th and is not a weekend or...
						(date.getDayOfMonth() == 3 && date.getDayOfWeek().equals(DayOfWeek.FRIDAY)) || //it is the 3rd and is a Friday or...
						(date.getDayOfMonth() == 5 && date.getDayOfWeek().equals(DayOfWeek.MONDAY)))); //it is the 5th and is a Monday)
	}
}