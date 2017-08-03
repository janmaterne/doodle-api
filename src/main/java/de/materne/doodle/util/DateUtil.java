/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.materne.doodle.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public final class DateUtil {

	private DateUtil() {}
	
	public static List<LocalDateTime> parseText(String text) {
		List<LocalDateTime> list = new ArrayList<>();
		for(String word : text.split("[, ]+")) {
			LocalDateTime ldt = parse(word.trim());
			if (ldt != null) {
				list.add(ldt);
			} else {
				ldt = parse(word.trim().replaceAll("[^0-9]$", ""));
    			if (ldt != null) {
    				list.add(ldt);
    			} 
			}
		}
    	return list;
	}
	
	public static LocalDateTime parse(String value) {
		return parse(value, LocalDateTime.now());
	}
	
	public static LocalDateTime parse(String value, LocalDateTime defaultProvider) {
		return parse(value, 
			DateTimeFormatter.ISO_DATE_TIME,
			DateTimeFormatter.ISO_LOCAL_DATE_TIME,
			DateTimeFormatter.ofPattern("dd.MM.yyyy:HH:mm"),
			DateTimeFormatter.ISO_DATE,
			DateTimeFormatter.ofPattern("dd.MM.yyyy"),
			formatterWithDefaults("dd.MM.yyyy", defaultProvider, ChronoField.HOUR_OF_DAY, ChronoField.MINUTE_OF_HOUR, ChronoField.SECOND_OF_MINUTE),
			formatterWithDefaults("dd.MM.", defaultProvider, ChronoField.YEAR)
		);
	}
	
	public static LocalDateTime parse(String string, DateTimeFormatter... formatters) {
		if (string == null) {
			return null;
		}
		for (DateTimeFormatter dtf : formatters) {
			try {
				return LocalDateTime.parse(string, dtf);
			} catch (DateTimeParseException e) {
				// no-op
			}
		}
		for (DateTimeFormatter dtf : formatters) {
			try {
				LocalDate localDate = LocalDate.parse(string, dtf);
				return LocalDateTime.of(localDate, LocalTime.of(0, 0));
			} catch (DateTimeParseException e) {
				// no-op
			}
		}
		return null;
	}
	
	public static DateTimeFormatter formatterWithDefaults(String pattern, LocalDateTime defaultProvider) {
		return formatterWithDefaults(pattern, defaultProvider,
			ChronoField.YEAR, ChronoField.MONTH_OF_YEAR, ChronoField.DAY_OF_MONTH, 
			ChronoField.HOUR_OF_DAY, ChronoField.MINUTE_OF_HOUR, ChronoField.SECOND_OF_MINUTE  
		);
	}

	public static DateTimeFormatter formatterWithDefaults(String pattern, LocalDateTime defaultProvider, TemporalField... defaultFields) {
		List<TemporalField> fields = Arrays.asList(defaultFields);
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder().appendPattern(pattern);
		applyDefault(builder, fields, ChronoField.YEAR, () -> defaultProvider.getYear() );
		applyDefault(builder, fields, ChronoField.MONTH_OF_YEAR, () -> defaultProvider.getMonthValue() );
		applyDefault(builder, fields, ChronoField.DAY_OF_MONTH, () -> defaultProvider.getDayOfMonth() );
		applyDefault(builder, fields, ChronoField.HOUR_OF_DAY, () -> defaultProvider.getHour() );
		applyDefault(builder, fields, ChronoField.MINUTE_OF_HOUR, () -> defaultProvider.getMinute() );
		applyDefault(builder, fields, ChronoField.SECOND_OF_MINUTE, () -> defaultProvider.getSecond() );
		return builder.toFormatter();
	}

	private static void applyDefault(DateTimeFormatterBuilder builder, List<TemporalField> fields, TemporalField field,
			Supplier<Integer> valueSupplier) {
		builder.parseDefaulting(field, fields.contains(field) ? valueSupplier.get() : 0);
	}

}
