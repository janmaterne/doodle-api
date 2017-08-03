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
package de.materne.doodle.spi;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import de.materne.doodle.api.Comment;
import de.materne.doodle.api.Description;
import de.materne.doodle.api.DoodleClient;
import de.materne.doodle.api.DoodleException;
import de.materne.doodle.api.Entry;
import de.materne.doodle.spi.handler.CommentHandler;
import de.materne.doodle.spi.handler.DescriptionHandler;
import de.materne.doodle.spi.handler.EntryHandler;
import de.materne.doodle.spi.util.PropertyUtil;
import de.materne.doodle.spi.util.URLCheck;
import de.materne.doodle.spi.util.WebDriverUtil;

public class DoodleClientImpl implements DoodleClient {
	
	private WebDriver driver;
	private WebDriverUtil driverUtil;
	
	private Description description;
	private List<Comment> comments;
	private List<Entry> entries;

	@Override
	public void open(String url) {
		checkUrl(url);
		loadProperties();
		createDriver();
		try {
			openSurvey(url);
			checkSurvey();
			extractDescription();
			extractComments();
			extractEntries();
		} finally {
			if (driver != null) {
				driver.close();
			}
		}
	}

	@Override
	public Description getDescription() {
		checkDriver("getDescription");
		return description;
	}

	@Override
	public List<Comment> getComments() {
		checkDriver("getComments");
		return comments;
	}

	@Override
	public List<Entry> getEntries() {
		checkDriver("getEntries");
		return entries;
	}

	private void loadProperties() {
		String fileName = "doodle-api.properties";
		PropertyUtil
			.create()
			.load(".", fileName)
			.load(System.getProperty("user.home"), fileName)
			.saveToSystem(false);
	}

	private void checkUrl(String url) {
		new URLCheck().check(url);
	}

	private void checkDriver(String method) {
		if (driver == null) {
			throw new DoodleException("must call open before %s", method);
		}
	}

	private void createDriver() {
		driver = WebDriverFactory.createDriver();
		driverUtil = new WebDriverUtil(driver);
	}

	private void openSurvey(String url) {
		driver.get(url);
	}

	private void checkSurvey() {
		String cssSelectorTable = "#d-participationTabSwitchView";
		driverUtil.requireElement(
			By.cssSelector(cssSelectorTable), 
			"'%s' does not seem to be a Doodle survey", driver.getCurrentUrl()
		);
		// Close welcome message: "Willkommen zum neuen Doodle"
		driverUtil.click(By.cssSelector(".d-continueButton"));
	}

	private void extractDescription() {
		description = new DescriptionHandler(driver).extractDescription();
	}

	private void extractComments() {
		comments = new CommentHandler(driver).extractComments();
	}

	private void extractEntries() {
		entries = new EntryHandler(driver).extractEntries();
	}

}
