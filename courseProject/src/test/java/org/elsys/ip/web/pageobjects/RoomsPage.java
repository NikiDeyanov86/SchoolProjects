package org.elsys.ip.web.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.stream.Collectors;

public class RoomsPage extends AbstractPage{
    @FindBy(how = How.ID, using = "name")
    private WebElement name;

    @FindBy(how = How.ID, using = "submit_btn")
    private WebElement submitButton;

    public RoomsPage(WebDriver driver) {
        super(driver);
    }

    public RoomPage createRoom(String name) {
        this.name.sendKeys(name);
        submitButton.click();
        return PageFactory.initElements(driver, RoomPage.class);
    }

    public void createRoom(
            String name,
            Boolean test) {
        this.name.clear();
        this.name.sendKeys(name);
        submitButton.click();
    }

    public List<String> getErrors() {
        return driver.findElements(By.cssSelector("#name ~ p.error"))
                .stream().map(e -> e.getText()).collect(Collectors.toList());
    }
}
