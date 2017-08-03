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

import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DoodleClientFailingTest {
	
	@Rule
	public ExpectedException exc = ExpectedException.none();
	
	@Test
	public void factory() {
		assertNotNull(DoodleClient.create());
	}
	
	@Test
	public void openNull() {
		exc.expect(DoodleException.class);
		exc.expectMessage("'url' must not be null");
		DoodleClient.create().open(null);
	}

	@Test
	public void openNotExisting() {
		exc.expect(DoodleException.class);
		exc.expectMessage("does not seem to be a Doodle survey");
		DoodleClient.create().open("https://doodle.com/poll/notExisting");
	}
	
	@Test
	public void openNotDoodle() {
		exc.expect(DoodleException.class);
		exc.expectMessage("not a Doodle URL");
		DoodleClient.create().open("http://www.apache.org");
	}
	
	@Test
	public void getCommentsBeforeOpen() {
		exc.expect(DoodleException.class);
		exc.expectMessage("must call open before");
		DoodleClient.create().getComments();
	}
	
	@Test
	public void getDescriptionBeforeOpen() {
		exc.expect(DoodleException.class);
		exc.expectMessage("must call open before");
		DoodleClient.create().getDescription();
	}
	
	@Test
	public void getEntriesBeforeOpen() {
		exc.expect(DoodleException.class);
		exc.expectMessage("must call open before");
		DoodleClient.create().getEntries();
	}
	
}
