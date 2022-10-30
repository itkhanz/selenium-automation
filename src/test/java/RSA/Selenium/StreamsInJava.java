package RSA.Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class StreamsInJava {
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
    public void test_SortTable() {
        driver.get("https://rahulshettyacademy.com/seleniumPractise/#/offers");

        // click on column
        driver.findElement(By.xpath("//tr/th[1]")).click();

        // capture all webelements into list
        List<WebElement> elementsList = driver.findElements(By.xpath("//tr/td[1]"));

        // capture text of all webelements into new(original) list
        List<String> originalList = elementsList.stream().map(s -> s.getText()).collect(Collectors.toList());

        // sort on the original list of step 3 -> sorted list
        List<String> sortedList = originalList.stream().sorted().collect(Collectors.toList());

        // compare original list vs sorted list
        Assert.assertTrue(originalList.equals(sortedList));
    }

    @Test
    public void test_paginationTable() {
        driver.get("https://rahulshettyacademy.com/seleniumPractise/#/offers");
        List<String> price;

        // scan the name column with getText ->Beans->print the price of the Rice
        do
        {
            List<WebElement> rows = driver.findElements(By.xpath("//tr/td[1]"));
            price = rows.stream().filter(s -> s.getText().contains("Rice")).map(s -> getPriceVeggie(s)).collect(Collectors.toList());
            price.forEach(a -> System.out.println(a));
            if(price.size()<1)
            {
                driver.findElement(By.cssSelector("[aria-label='Next']")).click();
            }
        } while (price.size()<1);

    }

    private static String getPriceVeggie(WebElement s) {
        String pricevalue = s.findElement(By.xpath("following-sibling::td[1]")).getText();
        return pricevalue;
    }

    @Test
    public void test_filterTable() {
        driver.get("https://rahulshettyacademy.com/seleniumPractise/#/offers");

        //search for the item in text field
        driver.findElement(By.id("search-field")).sendKeys("Rice");

        //get all the results
        List<WebElement> veggies=driver.findElements(By.xpath("//tr/td[1]"));

        //1 results
        List<WebElement> filteredList= veggies.stream().filter(veggie->veggie.getText().contains("Rice")).collect(Collectors.toList());

        //The filtered list should also be of size 1 since in this use case Rice should only bring 1 search result
        //1 result
        Assert.assertEquals(veggies.size(), filteredList.size());
    }
}
