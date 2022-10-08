package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SelfIntroduction {
    public static void main(String[] args) {

//        System.setProperty("webdriver.chrome.driver", "C:\\selenium\\webdrivers");
        WebDriverManager.chromedriver().setup();
        WebDriver driver  = new ChromeDriver();

//        System.setProperty("webdriver.gecko.driver", "C:\\selenium\\webdrivers");
//        WebDriverManager.firefoxdriver().setup();
//        WebDriver driver  = new FirefoxDriver();

//        System.setProperty("webdriver.edge.driver", "C:\\selenium\\webdrivers");
//        WebDriverManager.edgedriver().setup();
//        WebDriver driver = new EdgeDriver();

        driver.get("https://rahulshettyacademy.com/");
        System.out.println(driver.getCurrentUrl());
        System.out.println(driver.getTitle());

        driver.quit();
    }
}
