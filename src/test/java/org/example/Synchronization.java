package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class Synchronization {
    private static WebDriver driver;
    private WebDriverWait w;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        //waits for 5 seconds for elements to load on page
        //Implicit wait applies globally and wait for  seconds max before failing test
        //If condition is met before  seconds,  then it continue but since it applies globally, it causes performance issues
        //Unlike Thread.sleep() which halts the program execution to exactly the time specified despite element located
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        w = new WebDriverWait(driver,Duration.ofSeconds(5));
    }

    @AfterClass
    public void tearDown() {
        if(driver!=null)
            driver.quit();
    }

    @Test
    public void test_addTocartAndApplyCoupon() throws InterruptedException {
        driver.get("https://rahulshettyacademy.com/seleniumPractise/");
        String[] itemsNeeded= {"Cucumber","Brocolli","Beetroot"};

        Thread.sleep(3000);

        addItems(driver,itemsNeeded);

        driver.findElement(By.cssSelector("img[alt='Cart']")).click();
        driver.findElement(By.xpath("//button[contains(text(),'PROCEED TO CHECKOUT')]")).click();
        //explicit wait
        w.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.promoCode")));


        driver.findElement(By.cssSelector("input.promoCode")).sendKeys("rahulshettyacademy");
        driver.findElement(By.cssSelector("button.promoBtn")).click();

        //explicit wait
        w.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.promoInfo")));
        System.out.println(driver.findElement(By.cssSelector("span.promoInfo")).getText());
    }

    public static void addItems(WebDriver driver, String[] itemsNeeded) {

        int j = 0;
        List<WebElement> products = driver.findElements(By.cssSelector("h4.product-name"));
        for (int i = 0; i < products.size(); i++) {

            //Brocolli - 1 Kg
            //Brocolli,    1 kg
            String[] name = products.get(i).getText().split("-");
            String formattedName = name[0].trim();

            //format it to get actual vegetable name
            //convert array into array list for easy search
            //  check whether name you extracted is present in arrayList or not-

            List itemsNeededList = Arrays.asList(itemsNeeded);

            if (itemsNeededList.contains(formattedName)) {
                j++;
                //click on Add to cart
                driver.findElements(By.xpath("//div[@class='product-action']/button")).get(i).click();
                if (j == itemsNeeded.length) {
                    break;
                }
            }

        }

    }




}
