package TestNGTutorial;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class day2 {
    @Test(groups={"Smoke"})
    public void pLoan() {
        System.out.println("good");
    }

    @BeforeTest
    public void prerequiste()
    {
        System.out.println("Before first");
    }
}
