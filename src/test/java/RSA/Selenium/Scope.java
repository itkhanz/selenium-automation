package RSA.Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Iterator;
import java.util.Set;

public class Scope {
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
    public void test_limitDriverScope() throws InterruptedException {

        driver.get("http://qaclickacademy.com/practice.php");

        System.out.println(driver.findElements(By.tagName("a")).size());

        WebElement footerdriver=driver.findElement(By.id("gf-BIG"));// Limiting webdriver scope

        System.out.println(footerdriver.findElements(By.tagName("a")).size());

        //3-
        WebElement coloumndriver=footerdriver.findElement(By.xpath("//table/tbody/tr/td[1]/ul"));
        System.out.println(coloumndriver.findElements(By.tagName("a")).size());

        //4- click on each link in the coloumn and check if the pages are opening-
        for(int i=1;i<coloumndriver.findElements(By.tagName("a")).size();i++)
        {
            //opens the link in a new tab
            //The Keys.chord method allows you to pass multiple keys simultaneously.
            String clickonlinkTab= Keys.chord(Keys.CONTROL,Keys.ENTER);
            coloumndriver.findElements(By.tagName("a")).get(i).sendKeys(clickonlinkTab);
            Thread.sleep(5000L);

        }// opens all the tabs
        Set<String> abc=driver.getWindowHandles();//4
        Iterator<String> it=abc.iterator();

        while(it.hasNext())
        {
            driver.switchTo().window(it.next());
            System.out.println(driver.getTitle());
        }
    }
}
