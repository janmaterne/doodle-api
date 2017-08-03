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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

public class PropertyUtil {

	private Properties props = new Properties();
	
	private PropertyUtil() {}
	
	public static PropertyUtil create() {
		return new PropertyUtil(); 
	}
	
	public PropertyUtil load(String dir, String file) {
		try {
			props.load(new FileReader(new File(dir, file)));
		} catch (IOException e) {
			// ignore
		}
		return this;
	}
	
	public PropertyUtil saveToSystem(boolean overrideExistingKeys) {
		for(Entry<Object, Object> e : props.entrySet()) {
			boolean save = overrideExistingKeys || !System.getProperties().contains(e.getKey());
			if (save) {
				System.setProperty(String.valueOf(e.getKey()), String.valueOf(e.getValue()));
			}
		}
		return this;
	}
	
}
