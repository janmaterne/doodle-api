package de.materne.doodle.spi.handler;

import java.util.Map;

import org.openqa.selenium.WebDriver;

import de.materne.doodle.api.Description;
import de.materne.doodle.spi.util.WebDriverUtil;

public class DescriptionHandler {
	
	private WebDriverUtil driverUtil;

	public DescriptionHandler(WebDriver driver) {
		driverUtil = new WebDriverUtil(driver);
	}

	public Description extractDescription() {
		Description description = new Description();
		// open the description
		driverUtil.clickIfPossible(".d-descriptionButton");
		
		Map<String, String> meta = driverUtil.getMeta();
		description.setTitle(meta.getOrDefault("og:title", "").replaceAll("Doodle:", "").trim());
		description.setDescription(meta.get("og:description"));
		description.setAge(
			driverUtil.getText("div.d-pollHeader h2.d-pollSubTitle span")
				.trim()
		);
		description.setAuthor(
			driverUtil.getText("div.d-pollHeader h2.d-pollSubTitle span")
				.replaceFirst("von", "")
				.replaceFirst("from", "")
				.trim()
		);

		return description;
	}

}
