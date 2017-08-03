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
import org.openqa.selenium.firefox.FirefoxProfile;

import de.materne.doodle.api.DoodleException;
import de.materne.doodle.spi.WebDriverFactory;

public class FirefoxDriverFactory implements WebDriverFactory {

	@Override
	public WebDriver create() {
		checkSystemProperties();
		FirefoxProfile profile = createProfile();
		return new FirefoxDriver(profile);
	}

	private void checkSystemProperties() {
		if (!System.getProperties().containsKey("webdriver.gecko.driver")) {
			throw new DoodleException("'%s' required for defining the Gecko Marionette Driver for Firefox", "webdriver.gecko.driver");
		}
	}

	private FirefoxProfile createProfile() {
		FirefoxProfile profile = new FirefoxProfile();
		// Disable CSS
		profile.setPreference("permissions.default.stylesheet", 2); 
		// Disable images
		profile.setPreference("permissions.default.image", 2); 
		// Disable Flash
		profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so", false); 
		// Disable sound
		profile.setPreference("media.volume_scale", "0.0");

		// http://stackoverflow.com/questions/33937067/firefox-webdriver-opens-first-run-page-all-the-time
		profile.setPreference("browser.startup.homepage_override.mstone", "ignore");
		profile.setPreference("startup.homepage_welcome_url.additional", "about:blank");
		profile.setPreference("startup.homepage_welcome_url", "about:blank");
		profile.setPreference("browser.startup.homepage", "about:blank");
		
		return profile;
	}

}
