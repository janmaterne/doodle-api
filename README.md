Doodle API
==========

Overview
--------

This is an API for dealing with Doodle Surveys.
Actually only reading them is provided.

A [Doodle](http://doodle.com) survey is provided to find the "best"
date. Whereby it is not defined what "best" means ... 

A survey consists of three main parts: 
- a description of the survey
- a table where members could vote for their attendance
- a comments section

The main class of this API is [DoodleClient](https://github.com/janmaterne/doodle-api/blob/master/src/main/java/de/materne/doodle/api/DoodleClient.java).


Description
-----------

The description itself contains mainly a free text for describing the intent of 
the survey. This is captured by [Description](https://github.com/janmaterne/doodle-api/blob/master/src/main/java/de/materne/doodle/api/Description.java).


Voting Table
------------

The administrator creates the survey and during that the columns of the table.
Members add themselves by providing their name and vote about the given dates.
Depending on the kind of survey you could vote YES/NO or YES/NO/MAYBE.  
If the admin adds additional columns after members have voted, you have missing votes
for that date.

The table is represented as a list of a triple - [Entry](https://github.com/janmaterne/doodle-api/blob/master/src/main/java/de/materne/doodle/api/Entry.java) -
containing the name, the date and the vote [Choice](https://github.com/janmaterne/doodle-api/blob/master/src/main/java/de/materne/doodle/api/Choice.java).


Comments
--------

Members could post comments with their name and a free text. The representation
if a comment is [Comment](https://github.com/janmaterne/doodle-api/blob/master/src/main/java/de/materne/doodle/api/Comment.java). 
If you are interested in written dates in a comment, you could use the 
[CommentWithTimestamps](https://github.com/janmaterne/doodle-api/blob/master/src/main/java/de/materne/doodle/api/addon/CommentWithTimestamps.java)
decorator. For example you could write a report with all information to a give date.


Usage
-----

First step is getting the client and opening the survey.
If you try to get survey information before opening it you will get a 
[DoodleException](https://github.com/janmaterne/doodle-api/blob/master/src/main/java/de/materne/doodle/api/DoodleException.java).

```java
DoodleClient client = DoodleClient.create();
client.open("http://doodle.com/....");
```

After this you could get the information:
```java
String title = client.getDescription().getTitle();
String description = client.getDescription().getDescription();
```

If you are interested in the votes you could use Java8 stream filters:
```java
List<Entry> johnsEntries = entries
    .stream()
    .filter( e -> e.getName().equals("John Doe"))
    .collect(Collectors.toList());
```

All data for a given date:
```java
LocalDateTime search = LocalDateTime.of(...);
List<Entry> attendees = client.getEntries()
    .stream()
    .filter( e -> search.equals(e.getTstamp()) )
    .filter( e -> e.getChoice() == Choice.YES )
    .collect(Collectors.toList());
List<CommentWithTimestamps> comments = CommentWithTimestamps.from(client.getComments())
    .stream()
    .filter( c -> c.getDates().contains(search) )
    .collect(Collectors.toList());
```


Hints
-----

The only implementation of the 
[DoodleClient](https://github.com/janmaterne/doodle-api/blob/master/src/main/java/de/materne/doodle/api/DoodleClient.java) 
is [DoodleClientImpl](https://github.com/janmaterne/doodle-api/blob/master/src/main/java/de/materne/doodle/spi/DoodleClientImpl.java)
which depends on Firefox and Selenium. Therefore you have to ensure the compatibility of these two.

The survey website is using lots of JavaScript for its representation. That's why
it is more difficult to parse, so we use a standard browser.

Using Selenium and Firefox means depending on the Gecko marionette driver. You have
to specify the system property `webdriver.gecko.driver` pointing to its executable.
You could that by providing a `doodle-api.properties` file defining that value
in the current directory or in the home directory.
