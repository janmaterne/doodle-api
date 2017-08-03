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
package de.materne.doodle.spi.handler;

import static org.junit.Assert.assertEquals;

import java.time.DateTimeException;
import java.time.LocalDateTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class EntryHandlerTest {
	
	@Rule
	public ExpectedException exc = ExpectedException.none();

	@Test
	public void createTStamp() {
		EntryHandler handler = new EntryHandler(null);
		int currentYear = LocalDateTime.now().getYear();
		assertEquals(
			LocalDateTime.of(currentYear, 8, 2, 20, 0),
			handler.createTStamp("August", "2", "20:00")
		);
		assertEquals(
			LocalDateTime.of(currentYear, 10, 2, 4, 20),
			handler.createTStamp("Oktober", "2", "04:20")
		);
		assertEquals(
			LocalDateTime.of(currentYear, 10, 2, 20, 0),
			handler.createTStamp("October", "2", "20:00")
		);
		assertEquals(
			LocalDateTime.of(currentYear, 2, 15, 20, 0),
			handler.createTStamp("Februar", "15", "20:00")
		);
		assertEquals(
			LocalDateTime.of(currentYear, 2, 2, 20, 0),
			handler.createTStamp("February", "2", "20:00")
		);
	}
	
	@Test
	public void invalidMonth() {
		exc.expect(DateTimeException.class);
		exc.expectMessage("Invalid value");
		exc.expectMessage("MonthOfYear");
		new EntryHandler(null).createTStamp("invalid", "1", "2:0");
	}
	
}
