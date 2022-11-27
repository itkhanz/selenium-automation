package RSA.Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;

/**
 * AutoIt is a freeware scripting language designed for automating windows GUI and general scripting.
 * It uses a combination of mouse movement, keystrokes and window control manipulation to automate a task which is not possible by selenium webdriver.
 * Readings:
 * * https://www.guru99.com/use-autoit-selenium.html
 * * https://www.toolsqa.com/selenium-webdriver/autoit-selenium-webdriver/
 * * https://www.softwaretestinghelp.com/handle-windows-pop-up-in-selenium-using-autoit/
 */
public class AutoIT {

    private static WebDriver driver;
    private String downloadPath=System.getProperty("user.dir") + "\\src\\test\\resources\\downloads";

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        //The following code can be used to configure Chrome to download files to a specific directory.
        //https://chromedriver.chromium.org/capabilities
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", downloadPath);

        ChromeOptions options=new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);

        driver = new ChromeDriver(options);

        //waits for 5 seconds for elements to load on page
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterClass
    public void tearDown() {
        if(driver!=null)
            driver.quit();
    }

    /**
     * Open the AutoIT Script editor and write the script (drag and drop the FinderTool in AutoIT to locate the element i.e. input field and open button)
     * Save the script, right click and compile the script, and lastly invoke this fileupload.exe script through selenium
     * This script will paste the file path to upload box and click on  the open button and then handover control back to selenium
     * Write the script in AutoIt editor
     *      ControlFocus("Open", "", "Edit1")
     *      ControlSetText("Open", "", "Edit1", "full path to file on local system")
     *      ControlClick("Open", "", "Button1")
     *
     * Au3info- record window component objects
     *      Build Script -scite.exe
     *      Save it- .au3 extenstion
     *      Convert file into .exe by compiling .au3 file
     *
     * Call .exe file with Runtime class in java into your selenium tests
     *
     * Download and Install the AutoIT and Script Editor
     * https://www.autoitscript.com/site/autoit/downloads/
     * https://www.autoitscript.com/site/autoit-script-editor/downloads/
     *
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void test_FileUploadAndDownload() throws InterruptedException, IOException {
        //This method uploads demo.pdf file which gets converted into jpg online and then it is downloaded into the download path specified
        driver.get("https://www.ilovepdf.com/pdf_to_jpg");
        driver.findElement(By.xpath("//a[@id='pickfiles']")).click();   //Click on Select PDF file button
        Thread.sleep(3000);

        //Runtime class allows the script to interface with the environment in which the script is running.
        //getRuntime() get the current runtime associated with this process.
        //exec() methods execute the AutoIT script ( FileUpload.exe ) .
        Runtime.getRuntime().exec(System.getProperty("user.dir") + "\\src\\test\\resources\\scripts\\fileupload.exe");

        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@id='processTask']"))); //wait for file to be uploaded
        driver.findElement(By.xpath("//button[@id='processTask']")).click();     //click on Convert to JPG button

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[normalize-space()='PDF has been converted to JPG images']")));  //wait for file conversion
        driver.findElement(By.xpath("//a[@id='pickfiles']")).click();       //click on Download JPG images

        Thread.sleep(5000); //wait for file to be downloaded

        //validate if the file is successfully downloaded
        File f =new File(downloadPath+"/demo_page-0001.jpg");
        if(f.exists())
        {
            Assert.assertTrue(f.exists());
            if(f.delete())
                System.out.println("file deleted");
        }
    }
}
