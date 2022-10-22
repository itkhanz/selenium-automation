package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class DynamicDropDown {
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
     * This method works with the dynamic dropdowns.Options get loaded only when click on the parent tag.
     * The options in the destination dropdown are based on the option selected in the origin dropdown.
     * @throws InterruptedException
     */
    @Test
    public void test_departureArrival() throws InterruptedException {
        driver.get("https://rahulshettyacademy.com/dropdownsPractise/");

        driver.findElement(By.id("ctl00_mainContent_ddl_originStation1_CTXT")).click();

        driver.findElement(By.xpath("//a[@value='BLR']")).click();
        Thread.sleep(2000);

        //better to use parent child relationship instead of index in xpath
        //driver.findElement(By.xpath("(//a[@value='MAA'])[2]")).click();
        driver.findElement(By.xpath("//div[@id='glsctl00_mainContent_ddl_destinationStation1_CTNR'] //a[@value='MAA']")).click();

        //select the current date
        driver.findElement(By.cssSelector(".ui-state-default.ui-state-highlight")).click();
    }

    /**
     * This method shows the selection of auto suggestive dropdown values as you type
     * @throws InterruptedException
     */
    @Test
    public void test_country() throws InterruptedException {
        driver.get("https://rahulshettyacademy.com/dropdownsPractise/");
        driver.findElement(By.id("autosuggest")).sendKeys("ind");
        Thread.sleep(3000);
        List<WebElement> options = driver.findElements(By.cssSelector("li[class='ui-menu-item'] a"));

//        options.size() will return the count of all the elements found

        for (WebElement option : options) {
            if (option.getText().equalsIgnoreCase("India")) {
                option.click();
                break;
            }
        }

    }

    /**
     * This method shows you the testing of checkboxes
     */
    @Test
    public void test_checkboxes() {
        driver.get("https://rahulshettyacademy.com/dropdownsPractise/");    //URL in the browser

        //This is a way to use regular expressions with cssSelector, it returns any element where id contains this text
        Assert.assertFalse(driver.findElement(By.cssSelector("input[id*='SeniorCitizenDiscount']")).isSelected());

        driver.findElement(By.cssSelector("input[id*='SeniorCitizenDiscount']")).click();

        System.out.println(driver.findElement(By.cssSelector("input[id*='SeniorCitizenDiscount']")).isSelected());

        Assert.assertTrue(driver.findElement(By.cssSelector("input[id*='SeniorCitizenDiscount']")).isSelected());
    }

    /**
     * Selenium native isEnabled() method clicks on element to see if it is enabled or disabled. If disabled, click should not work.
     * But in modern websites, clicking on disable element will enable the element.
     * In our case, we can get the style attribute and check the opacity value
     */
    @Test
    public void test_enabledDisabled() {
        driver.get("https://rahulshettyacademy.com/dropdownsPractise/");

//        System.out.println(driver.findElement(By.name("ctl00$mainContent$rbtnl_Trip")).isEnabled());
        System.out.println(driver.findElement(By.id("Div1")).getAttribute("style"));
        driver.findElement(By.id("ctl00_mainContent_rbtnl_Trip_1")).click();
//        System.out.println(driver.findElement(By.name("ctl00$mainContent$rbtnl_Trip")).isEnabled());
        System.out.println(driver.findElement(By.id("Div1")).getAttribute("style"));
        if (driver.findElement(By.id("Div1")).getAttribute("style").contains("1")) {
            System.out.println("its enabled");
            Assert.assertTrue(true);
        } else {
            Assert.assertTrue(false);
        }

    }

    @Test
    public void test_assignment1() {
        driver.get("https://rahulshettyacademy.com/angularpractice/");

        driver.findElement(By.name("name")).sendKeys("rahul");

        driver.findElement(By.name("email")).sendKeys("hello@abc.com");

        driver.findElement(By.id("exampleInputPassword1")).sendKeys("123456");

        driver.findElement(By.id("exampleCheck1")).click();

        WebElement dropdown = driver.findElement(By.id("exampleFormControlSelect1"));

        Select abc = new Select(dropdown);

        abc.selectByVisibleText("Female");

        driver.findElement(By.id("inlineRadio1")).click();

        driver.findElement(By.name("bday")).sendKeys("02/02/1992");

        driver.findElement(By.cssSelector(".btn-success")).click();

        System.out.println(driver.findElement(By.cssSelector(".alert-success")).getText());
    }


}
