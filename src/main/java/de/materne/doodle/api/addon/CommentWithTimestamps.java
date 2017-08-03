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
package de.materne.doodle.api.addon;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.materne.doodle.api.Comment;
import de.materne.doodle.util.DateUtil;

/**
 * A decorator for a comment.
 * <p>The comment text is parsed for written dates. These dates are
 * available via {@linkplain #getDates()}.</p> 
 */
public final class CommentWithTimestamps {

	private Comment comment;
	private List<LocalDateTime> dates = new ArrayList<>();
	
	public static List<CommentWithTimestamps> from(List<Comment> comments) {
		return comments.stream().map(CommentWithTimestamps::new).collect(Collectors.toList());
	}
	
	public CommentWithTimestamps(Comment comment) {
		this.comment = comment;
		this.dates = DateUtil.parseText(comment.getText());
	}

	public List<LocalDateTime> getDates() {
		return dates;
	}
	
	public void setDates(List<LocalDateTime> dates) {
		this.dates = dates;
	}
	
	public String getName() {
		return comment.getName();
	}
	
	public String getTstamp() {
		return comment.getTstamp();
	}
	
	public String getText() {
		return comment.getText();
	}
	
}
