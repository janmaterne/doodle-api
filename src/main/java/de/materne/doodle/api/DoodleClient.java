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

import java.util.List;

import de.materne.doodle.spi.DoodleClientImpl;

/**
 * Entry class of the Doodle API.
 */
public interface DoodleClient {

	/**
	 * Opens the survey.
	 * @param url url of the survey
	 */
	void open(String url);
	
	/**
	 * Get the description of the survey.
	 * @return description
	 */
	Description getDescription();
	
	/**
	 * Returns all comments of the survey.
	 * @return comments
	 */
	List<Comment> getComments();
	
	/**
	 * Returns all entries (the triples of name, date and choice).
	 * @return entries
	 */
	List<Entry> getEntries();
	
	/**
	 * Factory method.
	 * Currently only a client using Firefox and Selenium is supported.
	 * @return an instance of the DoodleClient
	 */
	static DoodleClient create() {
		return new DoodleClientImpl();
	}
	
}
