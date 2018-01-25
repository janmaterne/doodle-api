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
package de.materne.doodle.api;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.materne.doodle.api.addon.CommentWithTimestamps;

@Ignore // Survey was deleted for unknown reasons ...
public class DoodleClientTest {
	
	/** 
	 * Doodley Survey created only for this test. 
	 */
	private static final String TEST_DOODLE = "https://beta.doodle.com/poll/eh9gapf9wtefh82z";

	private static DoodleClient client;
	
	@BeforeClass
	public static void open() {
		client = DoodleClient.create();
		client.open(TEST_DOODLE);
	}
	
	@Test
	public void descriptionAge() {
		// no fixed creation date, only a "before ... minutes/days" - so bad for testing.
		assertNotNull(client.getDescription().getAge());
	}
	
	@Test
	public void descriptionAuthor() {
		assertEquals("Administrator", client.getDescription().getAuthor());
	}
	
	@Test
	public void descriptionTitle() {
		assertEquals("API-Test", client.getDescription().getTitle());
	}
	
	@Test
	public void descriptionText() {
		String description = client.getDescription().getDescription();
		assertNotNull(description);
		assertThat(description, allOf(
			containsString("multiline note"), 
			containsString("added after"),
			containsString("missing entry.")
		));
	}

	@Test
	public void entries() {
		List<Entry> entries = client.getEntries();
		// number of dates (7) multiplied by number of persons (2)
		assertEquals(14, entries.size());
		List<Entry> johnsEntries = entries
			.stream()
			.filter( e -> e.getName().equals("John Doe"))
			.collect(Collectors.toList());
		assertEquals(7, johnsEntries.size());
		assertEquals(2, count(johnsEntries, e->e.getChoice()==Choice.NO) );
		assertEquals(1, count(johnsEntries, e->e.getChoice()==Choice.NO_ENTRY) );
		assertEquals(1, count(johnsEntries, e->e.getChoice()==Choice.NOT_SURE) );
		assertEquals(3, count(johnsEntries, e->e.getChoice()==Choice.YES) );
	}
	
	private long count(List<Entry> entries, Predicate<Entry> filter) {
		return entries.stream().filter(filter).count();
	}

	@Test
	public void comments() {
		List<Comment> comments = client.getComments();
		assertEquals(2, comments.size());
		Comment comment = comments.stream().filter(c->c.getText().startsWith("Even")).findFirst().get();
		assertEquals("Administrator", comment.getName());
		// also here we have not fixed date
		assertNotNull(comment.getTstamp());
	}
	
	@Test
	public void timestampedComments() {
		List<CommentWithTimestamps> comments = CommentWithTimestamps.from(client.getComments());
		assertEquals(2, comments.size());
		CommentWithTimestamps comment = comments.stream().filter(c->c.getText().startsWith("Even")).findFirst().get();
		assertEquals(2, comment.getDates().size());
	}
	
	@Test
	public void givenDate() {
		LocalDate searchDate = LocalDate.of(2017, 8, 8);
		
		LocalDateTime searchEntries  = LocalDateTime.of(searchDate, LocalTime.of(20, 0));
		LocalDateTime searchComments = LocalDateTime.of(searchDate, LocalTime.of( 0, 0));
		
		List<Entry> attendees = client.getEntries()
			.stream()
			.filter( e -> searchEntries.equals(e.getTstamp()) )
			.filter( e -> e.getChoice() == Choice.YES )
			.collect(Collectors.toList());
		List<CommentWithTimestamps> comments = CommentWithTimestamps.from(client.getComments())
			.stream()
			.filter( c -> c.getDates().contains(searchComments) )
			.collect(Collectors.toList());
		assertEquals(2, comments.size());
		assertEquals(1, attendees.size());
	}
	
}
