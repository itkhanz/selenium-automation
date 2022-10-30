package RSA.TestNG;

import org.testng.annotations.*;

public class day3 {
    //In TestNG the order of methods execution depends on the alphabetical order
    @BeforeSuite
    public void Bfsuite()
    {
        System.out.println("Before Suite");
    }
    @BeforeClass
    public void befoclas()
    {
        System.out.println("Before class day3");
    }
    @AfterClass
    public void afteclas()
    {
        System.out.println("After class day3");
    }
    /*@BeforeMethod
    public void beforeevery()
    {
        System.out.println(" I will execute before every test method in day 3 class");
    }
    @AfterMethod
    public void afterevery()
    {
        System.out.println(" I will execute after every test method in day 3 class");
    }*/
    @Parameters({ "URL","APIKey/usrname" })
    @Test
    public void WebLoginCarLoan(String urlname,String key) {
        //selenium
        System.out.println("weblogincar");
        System.out.println(urlname);
        System.out.println(key);
    }

    @Test(groups={"Smoke"})
    public void MobileLoginCarLoan() {
        //Appium
        System.out.println("Mobilelogincar");
    }

    @Test(enabled=false)
    public void MobilesigninCarLoan() {
        //Appium
        System.out.println("Mobile SIGIN");
    }

    @Test(dataProvider="getData")
    public void MobilesignoutCarLoan(String username,String password) {
        //Appium
        System.out.println("Mobile SIGOUT");
        System.out.println(username);
        System.out.println(password);
    }

    @Test(dependsOnMethods={"WebLoginCarLoan"})
    public void APIcarLoan()
    {
        //Rest API automation
        System.out.println("APIlogincar");
    }

    @DataProvider       //This annotation is helpful if you want to pass values to methods and run methods multiple times with different set of data
    public Object[][] getData()
    {
        //1st combiantion - username password - good credit history= row
        //2nd - username password  - no crdit history
        // 3rd - fraudelent credit history
        Object[][] data= new Object[3][2];
        //1st set
        data[0][0]="firstsetusername";
        data[0][1]="firstpassword";
        //couloumns in the row are nothing but values for that particualar combination(row)

        //2nd set
        data[1][0]= "secondsetusername";
        data[1][1]= "secondpassword";

        //3rd set
        data[2][0]="thirdsetusername";
        data[2][1]="thirdpassword";
        return data;
    }

}
