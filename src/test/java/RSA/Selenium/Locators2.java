package RSA.Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * This class teaches the use of locators in selenium webdriver
 * ID, classname, xpath, cssSelector, text, linkText, link, tag, partialText
 * we can also use the relative selectors like
 * traversing from child to parent xpath with parent::tagName
 * traversing to sibling xpath  with following-sibling::tagname
 */
public class Locators2 {

    private static WebDriver driver;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        //waits for 5 seconds for elements to load on page
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @Test
    public void loginTest() throws InterruptedException {
        String name = "rahul";
        String password = getPassword();

        driver.get("https://rahulshettyacademy.com/locatorspractice/");
        driver.findElement(By.id("inputUsername")).sendKeys(name);
        driver.findElement(By.name("inputPassword")).sendKeys(password);

        //following two lines demonstrate the use of relative selectors
        System.out.println(driver.findElement(By.xpath("//header/div/button[1]/following-sibling::button[1]")).getText());
        System.out.println(driver.findElement(By.xpath("//header/div/button[1]/parent::div/button[2]")).getText());

        driver.findElement(By.className("signInBtn")).click();

        //adding a wait because implicit wait won't work here because we go on different page
        Thread.sleep(2000);

        System.out.println(driver.findElement(By.tagName("p")).getText());
        Assert.assertEquals(driver.findElement(By.tagName("p")).getText(), "You are successfully logged in.");
        Assert.assertEquals(driver.findElement(By.cssSelector("div[class='login-container'] h2")).getText(), "Hello " + name + ",");
    }

    @Test
    public void logoutTest() {
        driver.findElement(By.xpath("//*[text()='Log Out']")).click();
    }

    @AfterClass
    public void tearDown() {
        if(driver!=null)
            driver.quit();
    }

    public static String getPassword() throws InterruptedException {

        driver.get("https://rahulshettyacademy.com/locatorspractice/");
        driver.findElement(By.linkText("Forgot your password?")).click();

        Thread.sleep(1000);
        driver.findElement(By.cssSelector(".reset-pwd-btn")).click();

        String passwordText = driver.findElement(By.cssSelector("form p")).getText();
        //Please use temporary password 'rahulshettyacademy' to Login.

        String[] passwordArray = passwordText.split("'");
        //0th index - Please use temporary password
        //1st index - rahulshettyacademy' to Login.

        String password = passwordArray[1].split("'")[0];
        //0th index - rahulshettyacademy
        //1st index - to Login.

        return password;
    }

}
