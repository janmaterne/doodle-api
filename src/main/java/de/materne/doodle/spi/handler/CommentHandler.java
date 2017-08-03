package de.materne.doodle.spi.handler;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import de.materne.doodle.api.Comment;
import de.materne.doodle.spi.util.WebDriverUtil;

public class CommentHandler {

	private WebDriver driver;
	private WebDriverUtil driverUtil;

	public CommentHandler(WebDriver driver) {
		this.driver = driver;
		driverUtil = new WebDriverUtil(driver);
	}

	public List<Comment> extractComments() {
		List<Comment> list = new ArrayList<>();
		// show comments
		driverUtil.clickIfPossible("#d-showComments > div:nth-child(2)");
		
		String css = "section#d-commentsView ul li";
		for(WebElement we : driver.findElements(By.cssSelector(css))) {
			Comment comment = new Comment();
			comment.setName(driverUtil.getText(we, ".d-commenterName"));
			comment.setText(driverUtil.getText(we, ".d-commentText"));
			comment.setTstamp(driverUtil.getText(we, ".d-commentTimestamp"));
			list.add(comment);
		}
		
		return list;
	}
	
}
