package TestNGTutorial;

import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class day1 {
    @BeforeTest
    public void bfTest()
    {
        System.out.println("Before test");

    }
    @AfterTest
    public void afTest()
    {
        System.out.println("After test");

    }

    @AfterSuite
    public void afSyite()
    {
        System.out.println("After Suite");
    }

    @Test
    public void Demo() {
        System.out.println("hello");
        Assert.assertTrue(false);
    }

    @Test
    public void SecondTest() {
        System.out.println("bye");
    }
}
