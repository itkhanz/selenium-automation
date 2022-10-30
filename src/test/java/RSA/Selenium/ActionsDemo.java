package RSA.Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class ActionsDemo {
    private static WebDriver driver;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        //waits for 5 seconds for elements to load on page
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterClass
    public void tearDown() {
        if(driver!=null)
            driver.quit();
    }

    @Test
    public void test_actions() {
        driver.get("https://www.amazon.com/");
        driver.manage().window().maximize();

        Actions actionObj = new Actions(driver);

        WebElement move = driver.findElement(By.cssSelector("a[id='nav-link-accountList']"));

        actionObj.moveToElement(driver.findElement(By.id("twotabsearchtextbox"))).click().keyDown(Keys.SHIFT).sendKeys("hello").doubleClick().build().perform();

        actionObj.moveToElement(move).contextClick().build().perform();
    }
}
