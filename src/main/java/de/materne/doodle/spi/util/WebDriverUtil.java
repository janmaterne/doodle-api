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
package de.materne.doodle.spi.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import de.materne.doodle.api.DoodleException;

public class WebDriverUtil {
	
	private WebDriver driver;
	
	public WebDriverUtil(WebDriver driver) {
		this.driver = driver;
	}
	
	public void requireElement(By selector, String errorMessageFormat, Object... errorArgs) {
		if (driver.findElements(selector).isEmpty()) {
			throw new DoodleException(errorMessageFormat, errorArgs);
		}
	}
	
	public void click(By selector) {
		try {
			driver.findElement(selector).click();
		} catch (NoSuchElementException e) {
			// ignore
		}
	}
	
	public void click(String cssSelector) {
		click(By.cssSelector(cssSelector));
	}

	public void clickIfPossible(By selector) {
		try {
			driver.findElement(selector).click();
		} catch (NoSuchElementException | ElementNotInteractableException e) {
			// ignore
		}
	}

	public void clickIfPossible(String cssSelector) {
		clickIfPossible(By.cssSelector(cssSelector));
	}
	
	public String getText(By selector) {
		return driver.findElement(selector).getText();
	}
	
	public String getText(String cssSelector) {
		return getText(By.cssSelector(cssSelector));
	}
	
	public Optional<WebElement> findElement(WebElement webElement, By selector) {
		List<WebElement> list = webElement.findElements(selector);
		return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	public String getText(WebElement webElement, String cssSelector) {
		return getText(webElement.findElement(By.cssSelector(cssSelector)));
	}

	public String getText(WebElement el) {
		String txt = el.getText();
		if (!StringUtils.isEmpty(txt)) {
			return txt;
		} else {
			return getSource(el);
		}
	}
	
	public String getText(WebElement webElement, String cssSelector, String defaultText) {
		List<WebElement> list = webElement.findElements(By.cssSelector(cssSelector));
		if (list.isEmpty()) {
			return defaultText;
		} else {
			return getText(list.get(0), cssSelector);
		}
	}
	
	public Map<String, String> getMeta() {
		Map<String, String> meta = new HashMap<>();
		String source = driver.getPageSource();
		String head = StringUtils.substringBetween(source, "<head>", "</head>");
		String[] split = StringUtils.substringsBetween(head, "<meta", ">");
		if (split != null) {
			for(String s : split) {
				String name = StringUtils.substringBetween(s, "name=\"", "\"");
				String prop = StringUtils.substringBetween(s, "property=\"", "\"");
				String http = StringUtils.substringBetween(s, "http-equiv=\"", "\"");
				String content = StringUtils.substringBetween(s, "content=\"", "\"");
				if (name != null) {
					meta.put(name, content);
				}
				if (prop != null) {
					meta.put(prop, content);
				}
				if (http != null) {
					meta.put(http, content);
				}
			}
		}
		return meta;
	}

    public String getSource(WebElement element) {
        return element.getAttribute("innerHTML");
    }

}
