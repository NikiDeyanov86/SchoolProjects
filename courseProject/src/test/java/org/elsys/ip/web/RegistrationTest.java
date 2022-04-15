package org.elsys.ip.web;

import org.elsys.ip.selenium.SeleniumConfig;
import org.elsys.ip.web.pageobjects.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class RegistrationTest {
    private WebDriver driver;

    @LocalServerPort
    private int port;

    private String baseAddress;

    @BeforeEach
    public void setUp() {
        driver = new SeleniumConfig().getDriver();
        baseAddress = "http://localhost:" + port + "/";
    }

    @AfterEach
    public void tearDown() {
        driver.close();
    }

    @Test
    public void registration() throws InterruptedException {
        driver.get(baseAddress);
        HomePage homePage = PageFactory.initElements(driver, HomePage.class);

        RegistrationPage registrationPage = homePage.register();
        homePage = registrationPage.register("First Name", "Last Name", "email@email.com", "password");
        LoginPage loginPage = homePage.login();
        homePage = loginPage.login("email@email.com", "password");
        assertThat(homePage.isAuthenticated()).isTrue();
    }

    @Test
    public void registrationErrors() throws InterruptedException {
        driver.get(baseAddress);
        HomePage homePage = PageFactory.initElements(driver, HomePage.class);

        RegistrationPage registrationPage = homePage.register();
        registrationPage.register("", "", "", "", "1");
        assertThat(registrationPage.getFirstNameErrors()).containsExactly("must not be empty");
        assertThat(registrationPage.getLastNameErrors()).containsExactly("must not be empty");
        assertThat(registrationPage.getEmailNameErrors()).containsExactlyInAnyOrder("must not be empty", "Invalid email");
        assertThat(registrationPage.getPasswordErrors()).containsExactly("must not be empty");
        assertThat(registrationPage.getGlobalErrors()).containsExactly("Passwords don't match");

        registrationPage.register("First Name", "", "", "", "1");

        assertThat(registrationPage.getFirstNameErrors()).hasSize(0);
        assertThat(registrationPage.getLastNameErrors()).containsExactly("must not be empty");
        assertThat(registrationPage.getEmailNameErrors()).containsExactlyInAnyOrder("must not be empty", "Invalid email");
        assertThat(registrationPage.getPasswordErrors()).containsExactly("must not be empty");
        assertThat(registrationPage.getGlobalErrors()).containsExactly("Passwords don't match");

        registrationPage.register("First Name", "Last Name", "invalid", "1", "1");
        assertThat(registrationPage.getFirstNameErrors()).hasSize(0);
        assertThat(registrationPage.getLastNameErrors()).hasSize(0);
        assertThat(registrationPage.getEmailNameErrors()).containsExactly("Invalid email");
        assertThat(registrationPage.getPasswordErrors()).hasSize(0);
        assertThat(registrationPage.getGlobalErrors()).hasSize(0);

    }

    @Test
    public void registerExistingEmail() throws InterruptedException {
        driver.get(baseAddress);
        HomePage homePage = PageFactory.initElements(driver, HomePage.class);
        RegistrationPage registrationPage = homePage.register();

        homePage = registrationPage.register("First Name", "Last Name", "example@gmail.com", "password");

        RegistrationPage registrationFailPage = homePage.register();
        registrationFailPage.register("Name", "AnotherName", "example@gmail.com", "password", "password");
        assertThat(registrationFailPage.getFirstNameErrors()).hasSize(0);
        assertThat(registrationFailPage.getLastNameErrors()).hasSize(0);
        assertThat(registrationFailPage.getEmailNameErrors()).containsExactly("An account for that username/email already exists.");
        assertThat(registrationFailPage.getPasswordErrors()).hasSize(0);
        assertThat(registrationFailPage.getGlobalErrors()).hasSize(0);
    }

    @Test
    public void invalidLogin() throws InterruptedException {
        driver.get(baseAddress);
        HomePage homePage = PageFactory.initElements(driver, HomePage.class);

        RegistrationPage registrationPage = homePage.register();
        homePage = registrationPage.register("First Name", "Last Name", "email@email.com", "password");

        LoginPage loginPage = homePage.login();
        loginPage.login("invalid", "password");
        assertThat(loginPage.getErrors()).hasSize(1);
        assertThat(loginPage.getErrors()).containsExactly("Bad credentials");

    }

    @Test
    public void createRoomAndTestJoinAndLeave() throws InterruptedException {
        driver.get(baseAddress);
        HomePage homePage = PageFactory.initElements(driver, HomePage.class);

        RegistrationPage registrationPage = homePage.register();
        homePage = registrationPage.register("First Name", "Last Name", "email@email.com", "password");
        LoginPage loginPage = homePage.login();
        homePage = loginPage.login("email@email.com", "password");

        RoomsPage roomsPage = homePage.rooms();
        RoomPage roomPage = roomsPage.createRoom("room1");
        assertThat(roomPage.getRoomName()).isEqualTo("room1");
        assertThat(roomPage.getParticipantName()).isEqualTo("email@email.com");

        roomPage = roomPage.leave();
        assertThat(roomPage.getParticipantName()).isEqualTo(null);

        roomPage = roomPage.join();
        assertThat(roomPage.getParticipantName()).isEqualTo("email@email.com");
    }

    @Test
    public void createInvalidRoom() throws InterruptedException {
        driver.get(baseAddress);
        HomePage homePage = PageFactory.initElements(driver, HomePage.class);

        RegistrationPage registrationPage = homePage.register();
        homePage = registrationPage.register("First Name", "Last Name", "email@email.com", "password");
        LoginPage loginPage = homePage.login();
        homePage = loginPage.login("email@email.com", "password");

        RoomsPage roomsPage = homePage.rooms();
        RoomPage roomPage = roomsPage.createRoom("room1");
        assertThat(roomPage.getRoomName()).isEqualTo("room1");

        RoomsPage roomsFailPage = homePage.rooms();
        roomsFailPage.createRoom("room", true);
        assertThat(roomsFailPage.getErrors()).hasSize(1);
        assertThat(roomsFailPage.getErrors()).containsExactly("The name should be more than 4 letters long.");

        roomsFailPage.createRoom("room1", true);
        assertThat(roomsFailPage.getErrors()).hasSize(1);
        assertThat(roomsFailPage.getErrors()).containsExactly("A room with that name already exists.");
    }


}
