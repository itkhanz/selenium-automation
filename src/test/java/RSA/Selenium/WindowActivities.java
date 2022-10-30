package RSA.Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WindowActivities {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver  = new ChromeDriver();

        driver.manage().window().maximize();

        driver.get("http://google.com");

        driver.navigate().to("https://rahulshettyacademy.com");

        driver.navigate().back();

        driver.navigate().forward();
    }
}
