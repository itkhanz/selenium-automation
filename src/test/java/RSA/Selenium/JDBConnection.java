package RSA.Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.*;
import java.time.Duration;

/**
 * JDBC is the standard Java API required for database-independent connectivity between the Java programming language and a wide range of databases.
 * This application program interface (API) lets users encode access request statements in a Structured Query Language (SQL).
 * They are then passed to the program that manages the database. It involves opening a connection, creating a SQL Database, executing SQL queries, and arriving at the output.
 * Readings:
 * * https://www.browserstack.com/guide/database-testing-using-selenium
 * *
 */
public class JDBConnection {

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
     * Pre-requisites: Download and install the MySQL Workbench.
     * Run the below script to create the database and table with example data
     * CREATE DATABASE Qadbt;
     * USE Qadbt;
     * CREATE TABLE credentials(scenario VARCHAR(20), username VARCHAR(20), password VARCHAR(20));
     * INSERT INTO credentials VALUES('zerobalancecard', 'zerobcc', '1234ee');
     * INSERT INTO credentials VALUES('outstbalancecard', 'osbbcc', '12344ee');
     * INSERT INTO credentials VALUES('rewardscard', 'rcbcc', '122234ee');
     * INSERT INTO credentials VALUES('shoppingcard', 'scbcc', '123444ee');
     * INSERT INTO credentials VALUES('basiccard', 'bsbcc', '123234ee');
     * SELECT * FROM credentials;
     *
     * Install the Maven dependency: mysql-connector-java
     * Create database connection, statement object and call the executeQuery method and save the result in ResultSet object.
     * @throws SQLException
     */
    @Test
    public void test_jdbconnection() throws SQLException {

        String host="localhost";
        String port= "3306";
        String user = "root"; //user of the database server for example root
        String password = ""; //password of the database server for example root

        //Connection URL
        //"jdbc:mysql://" + host + ":" + port + "/databasename";

        // Get connection to DB
        Connection con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/Qadbt", user, password);

        // Statement object to send the SQL statement to the Database
        Statement s=con.createStatement();

        ResultSet rs=s.executeQuery("select * from credentials where scenario ='rewardscard'");

        /*while (rs.next()) {
            System.out.println(rs.getString("username"));
            System.out.println(rs.getString("password"));
        }*/

        // Print the result untill all the records are printed
        // res.next() returns true if there is any next record else returns false
        while(rs.next())
        {
            driver.get("https://login.salesforce.com");
            driver.findElement(By.xpath(".//*[@id='username']")).sendKeys(rs.getString("username"));
            driver.findElement(By.xpath(".//*[@id='password']")).sendKeys(rs.getString("password"));
        }

        // Close DB connection
        if (con != null) {
            con.close();
        }
    }
}
