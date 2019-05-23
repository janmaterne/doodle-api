package de.materne.doodle.spi.handler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import de.materne.doodle.api.Choice;
import de.materne.doodle.api.Entry;
import de.materne.doodle.spi.util.ChoiceFactory;
import de.materne.doodle.spi.util.WebDriverUtil;

public class EntryHandler {

	private WebDriver driver;
	private WebDriverUtil driverUtil;

	public EntryHandler(WebDriver driver) {
		this.driver = driver;
		driverUtil = new WebDriverUtil(driver);
	}

	public List<Entry> extractEntries() {
		WebElement table = driver.findElement(By.cssSelector("div.d-expandableScrollContainer div.d-expandableScrollContentWrapper div.d-expandableScrollContent"));
		
		// Currently we have a table with data and a separate '<aside>' column with names.
		List<String> names = extractNames(table);
		Map<LocalDateTime, List<Choice>> data = extractChoices(table);
		
		// So we have to combine them.
		List<Entry> entries = combine(names, data);
		return entries;
	}

	protected Map<LocalDateTime, List<Choice>> extractChoices(WebElement table) {
		Map<LocalDateTime, List<Choice>> data = new HashMap<>();
		for(WebElement we : table.findElements(By.className("d-option"))) {
			String month = driverUtil.getText(we, ".d-monthFull");
			String day   = driverUtil.getText(we, ".d-date");
			String time  = driverUtil.getText(we, ".d-time", "20:00"); 
			LocalDateTime tstamp = createTStamp(month, day, time); 
			List<Choice> choices = we.findElements(By.cssSelector(".d-preferences li"))
				.stream()
				.map( w -> w.getAttribute("class") )
				.map( s -> s.replace("d-preference", "") )
				.map( s -> s.trim() )
				.map( ChoiceFactory::getFor )
				.collect(Collectors.toList());
			data.put(tstamp, choices);
		}
		return data;
	}
	
	protected List<String> extractNames(WebElement table) {
		List<String> names = table.findElements(By.className("d-participantInfo"))
			.stream()
			.map( we -> driverUtil.findElement(we, By.className("d-text")) )
			.filter(Optional::isPresent)
			.map(Optional::get)
			.map( we -> driverUtil.getText(we) )
			.filter(StringUtils::isNotEmpty)
			.map(String::trim)
			.collect(Collectors.toList());
		return names;
	}
	
	private List<Entry> combine(List<String> names, Map<LocalDateTime, List<Choice>> data) {
		List<Entry> list = new LinkedList<>();
		for(LocalDateTime ldt : data.keySet()) {
			List<Choice> choices = data.get(ldt);
			for(int i=0; i<names.size(); i++) {
				list.add(new Entry(ldt, names.get(i), choices.get(i)));
			}
		}
		return list;
	}

	protected LocalDateTime createTStamp(String month, String day, String time) {
		int _year = LocalDate.now().getYear();
		int _month = parseMonth(month);
		int _day = Integer.parseInt(day);
		int _hour = Integer.parseInt(time.split(":")[0]);
		int _minute = Integer.parseInt(time.split(":")[1]);
		return LocalDateTime.of(_year, _month, _day, _hour, _minute);
	}

	private int parseMonth(String month) {
		// The Doodle website could return the fully written month in different locales,
		// so we have to try them while parsing.
		for(Locale loc : Arrays.asList(Locale.getDefault(), Locale.ENGLISH, Locale.GERMAN)) {
			try {
				return DateTimeFormatter
					.ofPattern("MMMM", loc)
					.parse(month)
					.get(ChronoField.MONTH_OF_YEAR);
			} catch (DateTimeParseException e) {
				// try next Locale
			}
		}
		return 0;
	}
	
}
