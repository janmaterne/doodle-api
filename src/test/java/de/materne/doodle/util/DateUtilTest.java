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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

import org.junit.Test;

public class DateUtilTest {
	
	@Test
	public void isoDateTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
		assertEquals(LocalDateTime.of(2011, 12, 3, 10, 15, 30), DateUtil.parse("2011-12-03T10:15:30", dtf));
	}

	@Test
	public void isoLocalDateTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		assertEquals(LocalDateTime.of(2017, 8, 1, 0, 0), DateUtil.parse("2017-08-01T00:00:00", dtf));
	}

	@Test
	public void dashes() {
		DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE;
		assertEquals(LocalDateTime.of(2017, 8, 1, 0, 0), DateUtil.parse("2017-08-01", dtf));
	}

	@Test
	public void yearAndTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy:HH:mm");
		assertEquals(LocalDateTime.of(2017, 8, 1, 0, 0), DateUtil.parse("01.08.2017:00:00", dtf));
	}

	@Test
	public void german() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		assertEquals(LocalDateTime.of(2017, 8, 1, 0, 0), DateUtil.parse("01.08.2017", dtf));
	}

	@Test
	public void germanWithoutYear() {
		int currentYear = LocalDateTime.now().getYear();
		DateTimeFormatter dtf = DateUtil.formatterWithDefaults("dd.MM.", LocalDateTime.now(), ChronoField.YEAR);
		assertEquals(LocalDateTime.of(currentYear, 8, 1, 0, 0), DateUtil.parse("01.08.", dtf));
	}
	
	@Test
	public void parse() {
		LocalDateTime defaultProvider = LocalDateTime.of(2020, 8, 1, 0, 0, 0);
		assertEquals(LocalDateTime.of(2011, 12, 3, 10, 15, 30), DateUtil.parse("2011-12-03T10:15:30"));
		assertEquals(LocalDateTime.of(2017, 8, 1, 0, 0), DateUtil.parse("01.08.2017:00:00", defaultProvider));
		assertEquals(LocalDateTime.of(2017, 8, 1, 0, 0), DateUtil.parse("2017-08-01"));
		assertEquals(LocalDateTime.of(2017, 8, 1, 0, 0), DateUtil.parse("01.08.2017", defaultProvider));
		assertEquals(LocalDateTime.of(2020, 8, 1, 0, 0), DateUtil.parse("01.08.", defaultProvider));
		assertNull(DateUtil.parse(null));
		assertNull(DateUtil.parse(""));
		assertNull(DateUtil.parse("no-matched"));
	}
	
}
