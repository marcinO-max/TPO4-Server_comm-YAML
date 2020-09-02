/**
 *
 *  @author Ossowski Marcin S16425
 *
 */

package zad1;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Time {
	
	public static String passed(String string, String string2) throws DateTimeParseException {
		// TODO Auto-generated method stub
		String result ="";
		
		try {
		if(string.contains("T") || string2.contains("T")) {
			LocalDateTime date1 = LocalDateTime.parse(string);
			LocalDateTime date2 = LocalDateTime.parse(string2);
			String format = "d MMMM yyyy (EEEE) 'godz.' HH:mm";
			String wartString1 =date1.format(DateTimeFormatter.ofPattern(format));
			String wartString2 =date2.format(DateTimeFormatter.ofPattern(format));
			ZonedDateTime zonedDateTime1 = ZonedDateTime.of(date1, ZoneId.of("Europe/Warsaw"));
			ZonedDateTime zonedDateTime2 = ZonedDateTime.of(date2, ZoneId.of("Europe/Warsaw"));
			
			Period period = Period.between(date1.toLocalDate(), date2.toLocalDate());
			int dni = period.getDays();
			int years = period.getYears();
			int months = period.getMonths();
			Long cUnitHours = ChronoUnit.HOURS.between(zonedDateTime1, zonedDateTime2);
			Long cUnitMinutes = ChronoUnit.MINUTES.between(zonedDateTime1, zonedDateTime2);
			
			Locale locale = new Locale("xx");
			long dniLonga = cUnitHours/24;
					if((cUnitHours%24!=0 || cUnitMinutes%60!=0) || (cUnitHours%24!=0 && cUnitMinutes%60!=0)) {
						dniLonga+=1;
					}
			
			
			String localeFormat = String.format(locale, "%.2f",dniLonga/7.0);
			
			String kal=kalendarzowo(years, months, dni);
			
			
	
					result = resultat(wartString1, wartString2, dniLonga, localeFormat.replace(",", "."), cUnitHours, cUnitMinutes,kal);
			
			return result;
			
		}else {
			//dni lata i miesiace
			
			LocalDate lDate = LocalDate.parse(string);
			LocalDate lDate2 = LocalDate.parse(string2);
		String format = "d MMMM yyyy (EEEE)";
		
		String wartString1 =lDate.format(DateTimeFormatter.ofPattern(format));
		String wartString2 =lDate2.format(DateTimeFormatter.ofPattern(format));
			Period period = Period.between(lDate, lDate2);	
			
			int dni = period.getDays();
			int years = period.getYears();
			int months = period.getMonths();
			long allDays = obliczanie_dni(lDate, lDate2);
			Locale locale = new Locale("xx");
			String localeFormat = String.format(locale, "%.2f",allDays/7.0);
				
					result = "Od " + wartString1 + " do " + wartString2 + "\n"+
							" - mija: " + allDays +" dni, " + "tygodni " + localeFormat.replace(",", ".") + "\n" + kalendarzowo(years, months, dni);
					
				
				}
				
		return result;
		
		}catch(DateTimeParseException e) {
			return "*** "+ e.fillInStackTrace();
			
		}
		
		
	}
	
	public static String resultat(String wartString1, String wartString2, long allDay, String localeFormat, long cUnitHours, long cUnitMinutes, String kal ) {
			String result = "Od " + wartString1 + " do " + wartString2 + "\n"
							+ " - mija: " ;
			
			if(allDay==1) {
				result+= allDay + " dzień, "+"tygodni " +localeFormat + "\n"+
						" - godzin: "+ cUnitHours +", minut: "+cUnitMinutes +"\n"+ kal;
			}else {
				result+= allDay + " dni, "+"tygodni " +localeFormat + "\n" +
						 " - godzin: "+ cUnitHours +", minut: "+cUnitMinutes +"\n"+ kal;
			}
		
		return result;
	}
	
	public static String kalendarzowo(int years, int months, int days) {
		String string = " - kalendarzowo: ";
		String roczek="";
		String miesiaczek="";
		String dzionek="";
		
		if(years != 0) {
			if(years == 1) {
				roczek += String.valueOf(years) + " rok";
			}else if(years >= 2 && years <= 4){
				roczek += String.valueOf(years) + " lata";
			}else {
				roczek += String.valueOf(years) + " lat";
			}
		}
		
		
		if(months != 0) {
			if(months == 1) {
				miesiaczek += String.valueOf(months) + " miesiąc";
			}else if(months >=2 && months <=4){
				miesiaczek += String.valueOf(months) + " miesiące";
			}else {
				miesiaczek += String.valueOf(months) + " miesięcy";
			}
		}
		
		if(days != 0) {
			if(days == 1) {
				dzionek += String.valueOf(days) + " dzień";
			}else {
				dzionek += String.valueOf(days) + " dni";
			}
		}
		
		
		
		
		if(years != 0) {
		string += roczek;
			if(months != 0 ) {
				string += ", " + miesiaczek;
					if(days != 0) {
						string += ", " + dzionek;
					}
			}
		}
		
		if(years == 0) {
			if(months > 0) {
				string += ", " + miesiaczek;
				if(days != 0) {
					string += ", " + dzionek;
				}
			}else {
				string += dzionek;
			}
		}
		
		if(years == 0 && months==0 && days==0) {
			string ="";
		}
		
		
		
		
		return string;
	}
	
	
	public static long obliczanie_dni(LocalDate l1, LocalDate l2) {
		Period okres = Period.between(l1, l2);
		long days = ChronoUnit.DAYS.between(l1, l2);
		return days;
		
	}
}
