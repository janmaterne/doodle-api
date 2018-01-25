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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

import de.materne.doodle.api.DoodleException;

public class URLCheck {
	
	public void check(String url) {
		checkIsNotNull(url);
		checkIsDoodle(url);
		checkIsAvailable(url);
	}

	private void checkIsNotNull(String url) {
		if (url == null) {
			throw new DoodleException("'url' must not be null");
		}
	}

	private void checkIsDoodle(String url) {
		if (!url.contains("http") || !url.toLowerCase(Locale.ENGLISH).contains("doodle.com")) {
			throw new DoodleException("%s is not a Doodle URL", url);
		}
	}

	private void checkIsAvailable(String url) {
		InputStream in = null;
		try {
			URL internetUrl = new URL(url);
			in = internetUrl.openStream();
		} catch (IOException e) {
			e.printStackTrace();
			throw new DoodleException("%s is not available", url);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

}
