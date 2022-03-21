package org.elsys.ip.web.pageobjects;

import org.apache.tomcat.jni.Time;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.io.Console;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RoomPage extends AbstractPage {

    @FindBy(how = How.ID, using = "join_btn")
    private WebElement joinButton;

    @FindBy(how = How.ID, using = "leave_btn")
    private WebElement leaveButton;

    public RoomPage(WebDriver driver) {
        super(driver);
    }

    public RoomPage join() {
        joinButton.click();
        return PageFactory.initElements(driver, RoomPage.class);
    }

    public RoomPage leave() {
        leaveButton.click();
        return PageFactory.initElements(driver, RoomPage.class);
    }

    public String getRoomName() {
        return driver.findElement(By.cssSelector("p#room_name")).getText();
    }

    public String getParticipantName() {
        try {
            return driver.findElement(By.cssSelector("p#participant")).getText();
        }
        catch(WebDriverException e) {
            return null;
        }
    }
}
