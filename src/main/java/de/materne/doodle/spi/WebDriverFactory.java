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


import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

import de.materne.doodle.spi.drivers.FirefoxDriverFactory;

public interface WebDriverFactory {
	
	WebDriver create();
	
	static WebDriver createDriver() {
		WebDriverFactory factory = new FirefoxDriverFactory();
		WebDriver driver = factory.create();
		// hide the browser
		driver.manage().window().setSize(new Dimension(50, 50));
		driver.manage().window().setPosition(new Point(-500, 0));
		return driver;
	}

}
