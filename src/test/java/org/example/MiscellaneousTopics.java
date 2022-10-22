package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.List;

public class MiscellaneousTopics {
    private static WebDriver driver;

    @BeforeClass
    public void setUp() {

        //Read for more capabilities: https://chromedriver.chromium.org/capabilities
        //ChromeOptions options = new ChromeOptions();
        // FirefoxOptions options1 = new FirefoxOptions();
        // options1.setAcceptInsecureCerts(true);
        // EdgeOptions options2 = new EdgeOptions();

        //setup a proxy
        /*Proxy proxy = new Proxy();
        proxy.setHttpProxy("ipaddress:4444");
        options.setCapability("proxy", proxy);*/

        //configure the path for files downloaded by selenium webdriver
        /*Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("download.default_directory", "/directory/path");
        options.setExperimentalOption("prefs", prefs);*/

        //bypass the SSL Security check
        /*options.setAcceptInsecureCerts(true);*/

        //System.setProperty("webdriver.chrome.driver", "path to webdriver binary");
        //WebDriver driver = new ChromeDriver(options);


        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        //driver = new ChromeDriver(options);

        //As of version 5, WebDriverManager allows instantiating WebDriver objects (e.g. ChromeDriver, FirefoxDriver, etc.) using the WebDriverManager API.
        // This feature is available using each manager’s method create().
        //driver = WebDriverManager.chromedriver().capabilities(options).create();

        //waits for 5 seconds for elements to load on page
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterClass
    public void tearDown() {
        if(driver!=null)
            driver.quit();
    }

    @Test
    public void test_SSLCheck() {
        driver.get("https://expired.badssl.com/");
        System.out.println(driver.getTitle());
    }

    @Test
    public void test_MaximizeWindowDeleteCookies() {
        driver.get("https://google.com/");

        driver.manage().window().maximize();

        //driver.manage().deleteAllCookies(); //deletes all cookies
        //driver.manage().deleteCookieNamed("cookieName");    //deletes specific cookie

        //To delete the session, delete the sessionKey cookie and click on any link, it will redirect to the login page

        System.out.println(driver.getTitle());
    }

    @Test
    public void test_Screenshot() throws IOException {
        driver.get("https://google.com/");
        File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String projectPath = System.getProperty("user.dir");      //It will give you your current project path like
        FileUtils.copyFile(src, new File(projectPath + "/src/test/resources/screenshots/screenshot.png"));  //path should be either user directory in C drive or drive should not be C
    }

    /**
     * This test will fail because of the one broken link but it does not skip execution of next links and instead performs assertion at the end
     * @throws IOException
     */
    @Test
    public void test_BrokenLinks() throws IOException {
        driver.get("https://rahulshettyacademy.com/AutomationPractice/");

        List<WebElement> links=   driver.findElements(By.cssSelector("li[class='gf-li'] a"));

        SoftAssert softAssert =new SoftAssert();

        for(WebElement link : links)
        {
            String url= link.getAttribute("href");
             HttpURLConnection conn= (HttpURLConnection)new URL(url).openConnection();
             conn.setRequestMethod("HEAD");
             conn.connect();
            int respCode = conn.getResponseCode();
            System.out.println(respCode);
            //This is a hard assert and will fail the test if any broken link is found
            //Assert.assertTrue(respCode<400, "The link with Text"+link.getText()+" is broken with code" +respCode);

            //This is a soft assert, and it will continue to iterate through links and save the asserts
            softAssert.assertTrue(respCode<400, "The link with Text"+link.getText()+" is broken with code" +respCode);

        }
        //This method checks to see if any of the assertion is failed in soft assertions
        softAssert.assertAll();
    }
}
