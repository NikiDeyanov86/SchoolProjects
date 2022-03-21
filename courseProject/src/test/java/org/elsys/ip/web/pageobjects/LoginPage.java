package org.elsys.ip.web.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.stream.Collectors;

public class LoginPage extends AbstractPage {
    @FindBy(how = How.ID, using = "username")
    private WebElement username;

    @FindBy(how = How.ID, using = "password")
    private WebElement password;

    @FindBy(how = How.CLASS_NAME, using = "btn-lg")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public HomePage login(String username, String password) {
        this.username.sendKeys(username);
        this.password.sendKeys(password);
        loginButton.click();
        return PageFactory.initElements(driver, HomePage.class);
    }

    public void login(
            String username,
            String password,
            Boolean test) {
        this.username.clear();
        this.password.clear();

        this.username.sendKeys(username);
        this.password.sendKeys(password);

        loginButton.click();
    }

    public List<String> getErrors() {
        return driver.findElements(By.cssSelector("div.alert"))
                .stream().map(e -> e.getText()).collect(Collectors.toList());
    }

}
