package RSA.Selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class SeleniumGrid {
    //private static WebDriver driver;

/*    @BeforeClass
    public void setUp() throws MalformedURLException {
        //we will not use local webdriver instances because we want to run our tests on remote webdriver
        //WebDriverManager.chromedriver().setup();
        //driver = new ChromeDriver();

        //these capabilities will set the location and properties of which machine and configuration are to be used for running tests
        //Distributor will find the node with the desired capabilities and delegate client requests to the node
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName("chrome");  //as we want to run our test on chrome browser in a node, so we will set the capability to chrome
//      caps.setPlatform(Platform.WIN10);
//		caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
//		caps.setCapability(CapabilityType.BROWSER_NAME, "chrome");

        //This is the URL of the hub on which selenium hub is running
        String hubUrl = "http://100.82.255.117:4444";
        driver = new RemoteWebDriver(new URL(hubUrl), caps);

        //waits for 5 seconds for elements to load on page
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
    }

    @AfterClass
    public void tearDown() {
        if(driver!=null)
            driver.quit();
    }*/


    /**
     * Selenium Grid is a smart proxy server that makes it easy to run tests in parallel on multiple machines.
     *
     * Steps to run tests in parallel on Selenium Grid
     * Download the Selenium Server and Browser Binaries. Set the path of browser binaries in systen environment variables.
     * Start Hub: java -jar <SeleniumJarname> hub
     * Start Node: java -jar <SeleniumJarname> node --detect-drivers true
     * If you are starting the node on diffferent machine than hub, then you have to give:
     * java -jar <SeleniumJarname> node --detect-drivers true --publish-events tcp://<ipaddressofhub> --subscribe-events tcp:// <ipaddressofhub>
     * Check the Status of Grid with http://localhost:4444/
     * Create Multiple Selenium TestNG Tests with the ability of parallel run
     * Run tests with testGRid.xml which will run both methods in parallel on chrome and firefox browsers.
     *
     */

    /**
     * Detailed Steps
     * First configure the Hub
     *
     * 1) Download the latest Selenium Server file from https://www.selenium.dev/downloads/
     * 2) You can place the Selenium Server jar file anywhere in your hard drive,Lets place it on the C: Drive
     * 3) Open command prompt and navigate to the C: Drive
     * 4) On the command prompt, type java -jar selenium-4.1.0.jar hub
     *
     *
     * Setup Node Machine
     *
     * 1)we need to find out the IP Address of the Hub Machine. Go to Command Prompt and type IPCONFIG to find out the IP Address.
     * This means that the IP Address of the Hub Machine will become http:// + Hub Machine IP Address + Hub Port =  http://192.168.1.164:4444
     * 2)It is required to download the Selenium Server jar on the Node Machine as well.
     * 3) Open Command Prompt. If you are setting up Node on different machine, log on to that machine and open Command Prompt.
     * 4) To register Hub Machine with Node Machine, type;
     * java -jar selenium-server-4.1.0.jar node
     * 5) After executing the command then return to the Hub and navigate the URL http://localhost:4444 or http://192.168.1.164:4444 and
     * the hub will now display the node which is attached to it.
     *
     * java -jar selenium-server-4.1.0.jar node --hub http://localhost:4444/grid/register port 5656
     * java -Dwebdriver.chromedriver.driver=chromedriver.exe -jar selenium-server-4.1.0.jar node http://10.247.240.20:4444/grid/register  browser browserName=chrome port 5566
     *
     */

    @Test
    public void GoogleTest() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName("chrome");
        String hubUrl = "http://100.82.255.117:4444";
        WebDriver driver = new RemoteWebDriver(new URL(hubUrl), caps);

        driver.get("http://google.com");
        System.out.println(driver.getTitle());
        driver.findElement(By.xpath("//button/div[normalize-space()='Alle akzeptieren']")).click();
        driver.findElement(By.name("q")).sendKeys("rahul shetty");
        driver.close();
    }

    @Test
    public void RSATest() throws MalformedURLException {

        DesiredCapabilities caps = new DesiredCapabilities();
        //caps.setBrowserName("firefox");
        caps.setCapability(CapabilityType.BROWSER_NAME, "firefox");

        String hubUrl = "http://100.82.255.117:4444";
        WebDriver driver = new RemoteWebDriver(new URL(hubUrl), caps);
        driver.get("http://rahulshettyacademy.com");
        System.out.println(driver.getTitle());
        driver.close();
    }
}
