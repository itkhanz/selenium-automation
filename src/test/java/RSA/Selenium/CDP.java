package RSA.Selenium;

import com.google.common.collect.ImmutableList;
import org.openqa.selenium.By;
import org.openqa.selenium.HasAuthentication;
import org.openqa.selenium.Keys;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v106.emulation.Emulation;
import org.openqa.selenium.devtools.v106.fetch.Fetch;
import org.openqa.selenium.devtools.v106.fetch.model.RequestPattern;
import org.openqa.selenium.devtools.v106.network.Network;
import org.openqa.selenium.devtools.v106.network.model.ConnectionType;
import org.openqa.selenium.devtools.v106.network.model.ErrorReason;
import org.openqa.selenium.devtools.v106.network.model.Request;
import org.openqa.selenium.devtools.v106.network.model.Response;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.*;
import java.util.function.Predicate;

/**
 * This class contains different standalone tests to demonstrate the ChromeDevTools protocol automation with Selenium.
 * Chrome DevTools: https://www.selenium.dev/documentation/webdriver/bidirectional/chrome_devtools/
 * Documentation: https://developer.chrome.com/docs/devtools/
 * Selenium DevTools: https://www.selenium.dev/selenium/docs/api/java/org/openqa/selenium/devtools/DevTools.html
 * More Reading:
 * * https://applitools.com/blog/selenium-4-chrome-devtools/
 * * https://rahulshettyacademy.com/blog/index.php/2021/11/04/selenium-4-feature-chrome-dev-tools-protocol/
 *
 *
 * ChromeDevTools
 * Chrome Dev Tools is a set of web developer tools build directly into the Google Chrome browser. With Chrome DevTools, developers have deeper access
 * to the applications rendering on browser
 *
 * Chrome Devtools Protocol (CDP)
 * The Chrome Devtools protocol provide tools to instrument, inspect, debug and profile Chromium, Chrome and other Blink-related browsers.
 * Selenium 4 introduces powerful commands which are wrapper around teh CDP Domains to grant access to Chrome Devtools directly from automated tests.
 * With this CDP support, Selenium opens up the possibility of out of box testing with complete access and control to the browser features within Test.
 * Examples:
 * * Capture, Monitor and Stub the Network Requests
 * * Inject session cookies and perform basic Auth
 * * Mock Device coordinates for Mobile/Tabs view
 * * Check and monitor the site's performance
 * * Mock geolocations of the user
 * * Block the network requests
 * * Mock faster / slower network speeds
 * * Execute and debug Javascript
 *
 * How to initialize ChromeDevTools connection with Selenium
 * Chromium driver class has predefined methods to access Dev Tools
 * Chrome Driver and Edge Driver are inherited from the Chromium Driver. So we can access to Devtools with Chrome and edge browsers.
 * Step 1: Initiate Chromium Driver
 * Step 2: Create the object for Chrome Dev tools with getDevTools() Method
 * Step 3: Initiate Dev tools session to send the commands from Selenium devTools.createSession()
 * Step 4: devTools.send(COMMAND). This selenium inBuilt commands are wrapper methods that invoke CDP Domain functions.  Syntax: {Domain}.{Command}
 * Selenium has built-in commands for most of the commonly used CDP Methods. For unimplemented built-in commands, you can call the CDP methods directly
 * from selenium code with driver.executeCdpCommand(Method name, parameters)
 *
 * The DevTools imports should match to the latest browser and selenium version installed in your system to avoid any mismatch.
 */
public class CDP {
    public ChromeDriver driver;
    public DevTools devTools;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();

        //The getDevTools() method returns the new DevTools object which allows you to send() the built-in Selenium commands for CDP.
        // These commands are wrapper methods that make it cleaner and easier to invoke CDP functions.
        devTools = driver.getDevTools();

        //Create CDP session on given window/tab (aka target).
        devTools.createSession();
    }

    @AfterClass
    public void tearDown() {
        if(driver!=null)
            driver.quit();
    }

    /**
     * [WebDriver Code] -> send(command) -> [Selenium server interprets command] -> [Chrome DevTools Protocol] -> [Mimic the behavior of browser under Test]
     * https://chromedevtools.github.io/devtools-protocol/tot/Emulation/
     * https://chromedevtools.github.io/devtools-protocol/tot/Emulation/#method-setDeviceMetricsOverride
     * @throws InterruptedException
     */
    @Test
    public void test_MobileEmulatorTest() throws InterruptedException {

        //send command to CDP Methods-> CDP Methods will invoke and get access to Chrome dev tools
        //Overrides the values of device screen dimensions
        //The CDP command to modify the device’s metrics is Emulation.setDeviceMetricsOverride,
        // and this command requires input of width, height, mobile, and deviceScaleFactor.
        // These four keys are mandatory for this scenario, but there are several optional ones as well.
        devTools.send(Emulation.setDeviceMetricsOverride(600, 1000, 50, true,
                                                        Optional.empty(),
                                                        Optional.empty(),
                                                        Optional.empty(),
                                                        Optional.empty(),
                                                        Optional.empty(),
                                                        Optional.empty(),
                                                        Optional.empty(),
                                                        Optional.empty(),
                                                        Optional.empty()));

        openLibraryNavigation(driver);
    }


    /**
     * This method will show you how to write the custom CDP code if the class is not implemented by Selenium and you can invoke devTools method directly.
     * In the earlier case, the Emulation.setDeviceMetricsOverride() was already implemented, so we used devTools.send() method.
     * In case, no such method exists in selenium, you can write custom code with driver.executeCdpCommand("COMMAND_NAME", parameters)
     * COMMAND_NAME and exact parameters will be found on Chrome Dev Tools documentation
     *
     * [WebDriver Code] -> executeCdpCommand(CDP)  -> [Chrome DevTools Protocol] -> [Mimic the behavior of browser under Test]
     */
    @Test
    public void test_CdpCommandsTest() throws InterruptedException {

        Map<String, Object> deviceMetrics = new HashMap<String, Object>();
        deviceMetrics.put("width",600);
        deviceMetrics.put("height",1000);
        deviceMetrics.put("deviceScaleFactor",50);
        deviceMetrics.put("mobile",true);

        //The executeCdpCommand() method also allows you to execute CDP methods but in a more raw sense.
        // It does not use the wrapper APIs but instead allows you to directly pass in a Chrome DevTools command and the parameters for that command.
        // The executeCdpCommand() can be used if there isn’t a Selenium wrapper API for the CDP command,
        // or if you’d like to make the call in a different way than the Selenium APIs provide.
        driver.executeCdpCommand("Emulation.setDeviceMetricsOverride", deviceMetrics);
        openLibraryNavigation(driver);

    }


    /**
     * This method opens the website and clicks on hamburger navigation icon and click on the Library link
     * @param driver chromium driver object with cdp commands
     * @throws InterruptedException
     */
    public void openLibraryNavigation(ChromeDriver driver) throws InterruptedException {
        driver.get("https://rahulshettyacademy.com/angularAppdemo/");

        driver.findElement(By.cssSelector(".navbar-toggler")).click();
        Thread.sleep(3000);

        driver.findElement(By.linkText("Library")).click();
    }


    /**
     * Useful in Localization testing
     * This method Overrides the Geolocation Position or Error. Omitting any of the parameters emulates position unavailable.
     * https://chromedevtools.github.io/devtools-protocol/tot/Emulation/#method-setGeolocationOverride
     * This method find the heading of the Netflix website which is shown in different languages depending on the user's geolocation
     */
    @Test
    public void test_GeoLocationOverride() {
        //Coordinates of Berlin, Germany 52 13
        Map<String,Object>coordinates = new HashMap<String,Object>();
        coordinates.put("latitude", 52);
        coordinates.put("longitude", 13);
        coordinates.put("accuracy", 1);

        driver.executeCdpCommand("Emulation.setGeolocationOverride", coordinates);
        driver.get("http://google.com");
        driver.findElement(By.xpath("//button/div[normalize-space()='Alle akzeptieren']")).click();
        driver.findElement(By.name("q")).sendKeys("netflix", Keys.ENTER);
        driver.findElements(By.cssSelector(".LC20lb")).get(0).click();
        String title =driver.findElement(By.cssSelector(".our-story-card-title")).getText();
        System.out.println(title);
    }


    /**
     * https://chromedevtools.github.io/devtools-protocol/tot/Network/#method-enable
     * Enables network tracking, network events will now be delivered to the client (client is selenium in our case).
     *
     */
    @Test
    public void test_NetworkLogActivity() {
        //The CDP command to start capturing the network traffic is Network.enable.
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        //Event Fired when page is about to send HTTP request.
        //https://chromedevtools.github.io/devtools-protocol/tot/Network/#event-requestWillBeSent
        devTools.addListener(Network.requestWillBeSent(), request ->
        {
            Request req = request.getRequest();
            System.out.println("Request URL: " + req.getUrl());
            //req.getHeaders()
        });

        //Event Fired when HTTP response is available.
        //https://chromedevtools.github.io/devtools-protocol/tot/Network/#event-responseReceived
        devTools.addListener(Network.responseReceived(), response ->
        {
            //https://chromedevtools.github.io/devtools-protocol/tot/Network/#type-Response
            Response res = response.getResponse();  //HTTP response data
            System.out.println("Response URL: " + res.getUrl());
            System.out.println("Response Status Code: " + res.getStatus());
            if(res.getStatus().toString().startsWith("4"))
            {
                System.out.println(res.getUrl() + " is failing with status code " + res.getStatus());
            }
        });

        driver.get("https://rahulshettyacademy.com/angularAppdemo");
        driver.findElement(By.cssSelector("button[routerlink*='library']")).click();

        //Request URL: https://rahulshettyacademy.com/Library/GetBook.php?AuthorName=shetty
    }


    /**
     * Mock the network request to validate the response based on the mocked request. In this case, we will mock the user to change the request.
     * BadGuy user has only 1 book in library, so we will validate the warning message displayed on top when AuthorName parameter is mocked with it.
     * https://chromedevtools.github.io/devtools-protocol/tot/Fetch/
     *
     * @throws InterruptedException
     */
    @Test
    public void test_NetworkMocking() throws InterruptedException {
        //Fetch Domain: A domain for letting clients substitute browser's network layer with client code.
        //https://chromedevtools.github.io/devtools-protocol/tot/Fetch/#method-enable
        //Enables issuing of requestPaused events. A request will be paused until client calls one of failRequest, fulfillRequest or continueRequest/continueWithAuth.
        devTools.send(Fetch.enable(Optional.empty(), Optional.empty()));

        //https://chromedevtools.github.io/devtools-protocol/tot/Fetch/#event-requestPaused
        //Issued when the domain is enabled and the request URL matches the specified filter.
        // The request is paused until the client responds with one of continueRequest, failRequest or fulfillRequest.
        devTools.addListener(Fetch.requestPaused(), request->
        {
            if(request.getRequest().getUrl().contains("shetty"))
            {
                String mockedUrl =request.getRequest().getUrl().replace("=shetty", "=BadGuy");
                System.out.println(mockedUrl);
                //https://chromedevtools.github.io/devtools-protocol/tot/Fetch/#method-continueRequest
                //Continues the request, optionally modifying some of its parameters.
                devTools.send(Fetch.continueRequest(request.getRequestId(), Optional.of(mockedUrl), Optional.of(request.getRequest().getMethod()),
                        Optional.empty(), Optional.empty(), Optional.empty()));
            }
            else {

                devTools.send(Fetch.continueRequest(request.getRequestId(), Optional.of(request.getRequest().getUrl()), Optional.of(request.getRequest().getMethod()),
                        Optional.empty(), Optional.empty(), Optional.empty()));

            }

        });
        driver.get("https://rahulshettyacademy.com/angularAppdemo/");
        driver.findElement(By.cssSelector("button[routerlink*='library']")).click();
        Thread.sleep(3000);

        System.out.println(driver.findElement(By.cssSelector("p")).getText());
    }


    /**
     * This method is helpful if you want to fail the network requests to validate any error messages that should display.
     * It will fail the network request that matches the pattern GetBook
     */
    @Test
    public void test_NetworkFailedRequest() {
        //https://chromedevtools.github.io/devtools-protocol/tot/Fetch/#method-enable
        //https://chromedevtools.github.io/devtools-protocol/tot/Fetch/#type-RequestPattern
        //array[ RequestPattern ] If specified, only requests matching any of these patterns will produce fetchRequested event and will be paused until clients response.
        // If not set, all requests will be affected.
        Optional<List<RequestPattern>>	patterns = Optional.of(Collections.singletonList(new RequestPattern(Optional.of("*GetBook*"), Optional.empty(), Optional.empty())));

        devTools.send(Fetch.enable(patterns, Optional.empty()));

        devTools.addListener(Fetch.requestPaused(), request ->
        {
            //Since we already applied request pattern, we do not need to set filter to match specific requests again
            //https://chromedevtools.github.io/devtools-protocol/tot/Fetch/#method-failRequest
            devTools.send(Fetch.failRequest(request.getRequestId(), ErrorReason.FAILED));   //Causes the request to fail with specified reason.
        });

        driver.get("https://rahulshettyacademy.com/angularAppdemo/");
        driver.findElement(By.cssSelector("button[routerlink*='library']")).click();
    }


    /**
     * Functional automation can be slowed because of loading of large stylesheets, javascript and images that we can block to increase the test execution speed
     */
    @Test
    public void test_BlockNetworkRequests() {
        //Before performing any operation in the Network domain, we have to call Network.enable()
        //Enables network tracking, network events will now be delivered to the client.
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        //https://chromedevtools.github.io/devtools-protocol/tot/Network/#method-setBlockedURLs
        //Blocks URLs from loading.
        devTools.send(Network.setBlockedURLs(ImmutableList.of("*.jpg","*.css")));

        long startTime = System.currentTimeMillis();

        driver.get("https://rahulshettyacademy.com/angularAppdemo/");
        driver.findElement(By.linkText("Browse Products")).click();
        driver.findElement(By.linkText("Selenium")).click();
        driver.findElement(By.cssSelector(".add-to-cart")).click();
        System.out.println(driver.findElement(By.cssSelector("p")).getText());

        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }


    /**
     * It may be important to test how your application behaves under such conditions where the internet connection is slow (2G) or goes offline intermittently.
     * The CDP command to fake a network connection is Network.emulateNetworkConditions.
     * https://chromedevtools.github.io/devtools-protocol/tot/Network/#method-emulateNetworkConditions
     */
    @Test
    public void test_NetworkSpeed() {
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        //Activates emulation of network conditions.
        devTools.send(Network.emulateNetworkConditions(false, 3000, 20000, 100000, Optional.of(ConnectionType.ETHERNET)));

        //Fired when HTTP request has failed to load., set the offline parameter to true for testing the loadingFailed() Event
        devTools.addListener(Network.loadingFailed(), loadingFailed->
        {
            System.out.println(loadingFailed.getErrorText());   //net::ERR_INTERNET_DISCONNECTED
            System.out.println(loadingFailed.getTimestamp());


        });
        long startTime = System.currentTimeMillis();
        driver.get("http://google.com");
        driver.findElement(By.xpath("//button/div[normalize-space()='Alle akzeptieren']")).click();
        driver.findElement(By.name("q")).sendKeys("netflix",Keys.ENTER);
        driver.findElements(By.cssSelector(".LC20lb")).get(0).click();
        String title =driver.findElement(By.cssSelector(".our-story-card-title")).getText();
        System.out.println(title);
		//driver.get("https://rahulshettyacademy.com/angularAppdemo/");
		//driver.findElement(By.cssSelector("button[routerlink*='library']")).click();
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }


    /**
     * Interacting with browser popups is not supported in Selenium, as it is only able to engage with DOM elements.
     * This poses a challenge for pop-ups such as authentication dialogs.
     * https://www.selenium.dev/selenium/docs/api/java/index.html?org/openqa/selenium/HasAuthentication.html
     * https://www.selenium.dev/selenium/docs/api/java/org/openqa/selenium/class-use/Credentials.html
     * https://www.selenium.dev/ja/documentation/webdriver/bidirectional/bidi_api/#register-basic-auth
     */
    @Test
    public void test_BasicAuthentication() {
        //The getHost() function is a part of URI class. The function getHost() returns the host of a specified URI.
        Predicate<URI> uriPredicate = uri ->  uri.getHost().contains("httpbin.org");

        //Interface HasAuthentication: Indicates that a driver supports authenticating to a website in some way.
        //register(Predicate<URI> whenThisMatches, Supplier<Credentials> useTheseCredentials)
        // Registers a check for whether a set of Credentials should be used for a particular site, identified by its URI.
        //static java.util.function.Supplier<Credentials>	UsernameAndPassword.of(java.lang.String username, java.lang.String password)
        ((HasAuthentication)driver).register(uriPredicate, UsernameAndPassword.of("foo", "bar"));

        driver.get("http://httpbin.org/basic-auth/foo/bar");
    }


    /**
     * While testing and working on an application with specific data or specific conditions.
     * logs help us in debugging and capturing the error messages, giving more insights that are published in the Console tab of the Chrome DevTools.
     * We can capture the console logs through our Selenium scripts by calling the CDP Log commands as demonstrated below.
     * https://chromedevtools.github.io/devtools-protocol/tot/Log/
     * https://www.selenium.dev/ja/documentation/webdriver/bidirectional/bidi_api/#listen-to-consolelog-events
     */
    @Test
    public void test_ConsoleLogsCapture() {

        /*devTools.send(Log.enable());
        devTools.addListener(Log.entryAdded(),
                logEntry -> {
                    System.out.println("log: "+logEntry.getText());
                    System.out.println("level: "+logEntry.getLevel());
                });
        */

        driver.get("https://rahulshettyacademy.com/angularAppdemo/");
        driver.findElement(By.linkText("Browse Products")).click();
        driver.findElement(By.partialLinkText("Selenium")).click();
        driver.findElement(By.cssSelector(".add-to-cart")).click();
        driver.findElement(By.linkText("Cart")).click();
        driver.findElement(By.id("exampleInputEmail1")).clear();
        driver.findElement(By.id("exampleInputEmail1")).sendKeys("2");

        LogEntries entry= driver.manage().logs().get(LogType.BROWSER); //Get LogEntries object
        List<LogEntry>logs = entry.getAll();  //LogEntryobject- getAll method return all logs in list

        for(LogEntry e : logs)//iterating through list and printing each log message
        {
            System.out.println(e.getMessage());  //Log4j
        }


    }

}
