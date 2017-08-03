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

import java.time.LocalDateTime;

/**
 * Triple containing the information about a given vote.
 */
public class Entry {
	
	/** Timestamp of a date. */
	private LocalDateTime tstamp;
	
	/** Name of the voting member. */
	private String name;
	
	/** The give vote. */
	private Choice choice;
	
	// Generated Code
	
	public Entry(LocalDateTime tstamp, String name, Choice choice) {
		this.tstamp = tstamp;
		this.name = name;
		this.choice = choice;
	}

	public LocalDateTime getTstamp() {
		return tstamp;
	}

	public void setTstamp(LocalDateTime tstamp) {
		this.tstamp = tstamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Choice getChoice() {
		return choice;
	}

	public void setChoice(Choice choice) {
		this.choice = choice;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((choice == null) ? 0 : choice.hashCode());
		result = prime * result + ((tstamp == null) ? 0 : tstamp.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entry other = (Entry) obj;
		if (choice != other.choice)
			return false;
		if (tstamp == null) {
			if (other.tstamp != null)
				return false;
		} else if (!tstamp.equals(other.tstamp))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Entry [tstamp=" + tstamp + ", name=" + name + ", choice=" + choice + "]";
	}
	
}
