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
/**
 * <h1>This is the API for dealing with Doodle Surveys.</h1>
 * Actually only reading them is provided.
 * 
 * <p>A <a href="http://doodle.com">Doodle</a> survey is provided to find the "best"
 * date. Whereby it is not defined what "best" means ... <br>
 * A survey consists of three main parts: <ul>
 * <li>a description of the survey
 * <li>a table where members could vote for their attendance
 * <li>a comments section
 * </ul>
 * The main class of this API is {@linkplain de.materne.doodle.api.DoodleClient}.
 * </p>
 * 
 * <h2>description</h2>
 * The description itself contains mainly a free text for describing the intent of 
 * the survey. This is captured by {@linkplain de.materne.doodle.api.Description}.
 * 
 * <h2>voting table</h2>
 * The administrator creates the survey and during that the columns of the table.
 * Members add themselves by providing their name and vote about the given dates.
 * Depending on the kind of survey you could vote YES/NO or YES/NO/MAYBE.  
 * If the admin adds additional columns after members have voted, you have missing votes
 * for that date.<br>
 * The table is represented as a list of a triple ({@linkplain de.materne.doodle.api.Entry} 
 * containing the name, the date and the vote {@linkplain de.materne.doodle.api.Choice}.
 * 
 * <h2>comments</h2>
 * Members could post comments with their name and a free text. The representation
 * if a comment is {@linkplain de.materne.doodle.api.Comment}. If you are interested
 * in written dates in a comment, you could use the {@linkplain de.materne.doodle.api.addon.CommentWithTimestamps}
 * decorator. For example you could write a report with all information to a give date.
 * 
 * <h2>usage</h2>

 * First step is getting the client and opening the survey.
 * If you try to get survey information before opening it you will get a {@linkplain de.materne.doodle.api.DoodleException}.
 * <pre>
 * DoodleClient client = DoodleClient.create();
 * client.open("http://doodle.com/....");
 * </pre>
 * 
 * After this you could get the information:
 * <pre>
 * String title = client.getDescription().getTitle();
 * String description = client.getDescription().getDescription();
 * </pre>
 * 
 * If you are interested in the votes you could use Java8 stream filters:
 * <pre>
 * List&lt;Entry> johnsEntries = entries
 *     .stream()
 *     .filter( e -> e.getName().equals("John Doe"))
 *     .collect(Collectors.toList());
 * </pre>
 * 
 * All data for a given date:
 * <pre>
 * LocalDateTime search = LocalDateTime.of(...);
 * List&lt;Entry> attendees = client.getEntries()
 *     .stream()
 *     .filter( e -> search.equals(e.getTstamp()) )
 *     .filter( e -> e.getChoice() == Choice.YES )
 *     .collect(Collectors.toList());
 * List&lt;CommentWithTimestamps> comments = CommentWithTimestamps.from(client.getComments())
 *     .stream()
 *     .filter( c -> c.getDates().contains(search) )
 *     .collect(Collectors.toList());
 * </pre>
 * 
 * <h2>hints</h2>
 * 
 * <p>The only implementation of the {@linkplain de.materne.doodle.api.DoodleClient} is
 * {@linkplain de.materne.doodle.spi.DoodleClientImpl} which depends on Firefox and Selenium.
 * Therefore you have to ensure the compatibility of these two.</p>
 * 
 * <p>The survey website is using lots of JavaScript for its representation. That's why
 * it is more difficult to parse, so we use a standard browser.</p>
 * 
 * <p>Using Selenium and Firefox means depending on the Gecko marionette driver. You have
 * to specify the system property <tt>webdriver.gecko.driver</tt> pointing to its executable.
 * You could that by providing a <tt>doodle-api.properties</tt> file defining that value
 * in the current directory or in the home directory.</p>
 */
package de.materne.doodle.api;
