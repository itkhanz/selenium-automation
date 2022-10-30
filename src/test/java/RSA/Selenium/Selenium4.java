package RSA.Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Iterator;
import java.util.Set;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

public class Selenium4 {
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

    /**
     * As of now, Selenium realtobe locators only work with By.tagname
     */
    @Test
    public void test_RelativeLocators() {
        driver.get("https://rahulshettyacademy.com/angularpractice/");

        WebElement nameEditBox =driver.findElement(By.cssSelector("[name='name']"));
        System.out.println(driver.findElement(with(By.tagName("label")).above(nameEditBox)).getText());

        //relative locators does not support the flex HTML elements so it goes and locate the next input element that is submit
        WebElement dateofBirth = driver.findElement(By.cssSelector("[for='dateofBirth']"));
        driver.findElement(with(By.tagName("input")).below(dateofBirth)).click();

        WebElement iceCreamLabel =driver.findElement(By.xpath("//label[text()='Check me out if you Love IceCreams!']"));
        driver.findElement(with(By.tagName("input")).toLeftOf(iceCreamLabel)).click();

        WebElement rdb = driver.findElement(By.id("inlineRadio1"));
        System.out.println(driver.findElement(with(By.tagName("label")).toRightOf(rdb)).getText());
    }


    /**
     * This method opens a new window(tab) and get the course name from this child window, and fill it in the name element of parent window
     */
    @Test
    public void test_NewWindowTab(){
        driver.get("https://rahulshettyacademy.com/angularpractice/");
        driver.switchTo().newWindow(WindowType.TAB);    //replace TAB with WINDOW to open a new window

        Set<String> handles = driver.getWindowHandles();
        Iterator<String> handlesIterator = handles.iterator();

        String parentWindowId = handlesIterator.next();
        String childWindowId = handlesIterator.next();

        driver.switchTo().window(childWindowId);
        driver.get("https://rahulshettyacademy.com/");

        //added explicit wait for the website content to load
        WebDriverWait w = new WebDriverWait(driver,Duration.ofSeconds(20));
        w.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.logo")));

        String courseName = driver.findElements(By.cssSelector("a[href*='https://courses.rahulshettyacademy.com/p']")).
                get(1).getText();
        System.out.println(courseName);

        driver.switchTo().window(parentWindowId);
        WebElement name = driver.findElement(By.cssSelector("[name='name']"));
        name.sendKeys(courseName);


    }

    /**
     * This method demonstrates how you can take the partial screenshot of any web element
     */
    @Test
    public void test_ElementScreenshot() {
        driver.get("https://rahulshettyacademy.com/angularpractice/");

        WebElement headerImage = driver.findElement(By.xpath("//div[@class='jumbotron']/parent::div[@class='container']"));
        File srcFile = headerImage.getScreenshotAs(OutputType.FILE);
        String projectPath = System.getProperty("user.dir");      //It will give you your current project path like
        try {
            FileUtils.copyFile(srcFile, new File(projectPath + "/src/test/resources/screenshots/screenshot-element.png"));  //path should be either user directory in C drive or drive should not be C
        } catch(IOException e) {
            System.out.println(e);
        }
    }

    /**
     * It is used to fetch the dimensions and coordinates of the referenced element.
     */
    @Test
    public void test_SizeAndPosition() {
        // Navigate to url
        driver.get("https://www.example.com");

        // Returns height, width, x and y coordinates referenced element
        Rectangle res =  driver.findElement(By.cssSelector("h1")).getRect();

        // Rectangle class provides getX,getY, getWidth, getHeight methods
        System.out.println(res.getX()); //X-axis position from the top-left corner of the element
        System.out.println(res.getY()); //y-axis position from the top-left corner of the element
        System.out.println(res.getHeight());
        System.out.println(res.getWidth());
        System.out.println(res.getDimension());
    }


}
