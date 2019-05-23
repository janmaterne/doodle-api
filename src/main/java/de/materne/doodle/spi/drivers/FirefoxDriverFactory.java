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
package de.materne.doodle.spi.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import de.materne.doodle.api.DoodleException;
import de.materne.doodle.spi.WebDriverFactory;

public class FirefoxDriverFactory implements WebDriverFactory {

	@Override
	public WebDriver create() {
		checkSystemProperties();
		FirefoxOptions options = createFirefoxOptions();
		return new FirefoxDriver(options);
	}

	private void checkSystemProperties() {
		if (!System.getProperties().containsKey("webdriver.gecko.driver")) {
			throw new DoodleException("'%s' required for defining the Gecko Marionette Driver for Firefox", "webdriver.gecko.driver");
		}
	}

	private FirefoxOptions createFirefoxOptions() {
		FirefoxOptions options = new FirefoxOptions();
		// Disable CSS
		options.addPreference("permissions.default.stylesheet", 2); 
		// Disable images
		options.addPreference("permissions.default.image", 2); 
		// Disable Flash
		options.addPreference("dom.ipc.plugins.enabled.libflashplayer.so", false); 
		// Disable sound
		options.addPreference("media.volume_scale", "0.0");

		// http://stackoverflow.com/questions/33937067/firefox-webdriver-opens-first-run-page-all-the-time
		options.addPreference("browser.startup.homepage_override.mstone", "ignore");
		options.addPreference("startup.homepage_welcome_url.additional", "about:blank");
		options.addPreference("startup.homepage_welcome_url", "about:blank");
		options.addPreference("browser.startup.homepage", "about:blank");
		return options;
	}
	
}
