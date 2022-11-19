package RSA.Selenium;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class DataDriven {
    /*private static WebDriver driver;

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
    }*/

    /**
     * Strategy to Access Excel Data
     * Intslall the Maven dependencies: Apache poi-ooxml and poi
     *
     * 1. Create object for XSSFWorkbook class
     * 2. Get access to all sheets
     * 3. Get access to all rows of Sheet
     * 4. Access to specific row from all rows
     * 5. Get Access to all cells of Row
     * 6. Access the Data from Excel into Arrays
     */

    @Test
    public void test_ExcelData() throws IOException {
        /*Requirement :Identify Testcases column by scanning the entire first row
        once column is identified then scan the entire Testcases column to identify the Purchase testcase row
        after grabbing the testcase row, pull all the data of that row and feed into the test*/
        ArrayList<String> data = getData("Purchase");

        Assert.assertEquals(data.get(0), "Purchase");
        Assert.assertEquals(data.get(1), "purchase1");
        Assert.assertEquals(data.get(2), "purchase2");
        Assert.assertEquals(data.get(3), "purchase3");

        ArrayList<String> data1 = getData("Add profile");
        Assert.assertEquals(data1.get(2), "2");

    }

    public ArrayList<String> getData(String testCaseName) throws IOException {
        ArrayList<String> purchaseData = new ArrayList<String>();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\test-data\\demodata.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);

        int sheets = workbook.getNumberOfSheets();
        for (int i = 0; i < sheets; i++) {
            if (workbook.getSheetName(i).equalsIgnoreCase("testdata")) {
                XSSFSheet sheet = workbook.getSheetAt(i);

                //identify the Testcase column by scanning the first row (Header row)
                Iterator<Row> rows = sheet.iterator();
                Row firstRow = rows.next();
                Iterator<Cell> col = firstRow.cellIterator();
                int colIndex = 0;
                int j = 0;
                while (col.hasNext()) {
                    Cell value = col.next();
                    if (value.getStringCellValue().equalsIgnoreCase(testCaseName)) {
                        //desired column
                        colIndex = j;
                        break;
                    }
                    j++;
                }
                //System.out.println(colIndex);
                //Once column is identified then scan the entire Testcases column to identify the Purchase testcase row
                while (rows.hasNext()){
                    Row r = rows.next();
                    if (r.getCell(colIndex).getStringCellValue().equalsIgnoreCase(testCaseName)) {
                        //after grabbing the testcase row, pull all the data of that row and feed into the test
                        Iterator<Cell> cells  = r.cellIterator();
                        while (cells.hasNext()) {
                            Cell cell = cells.next();
                            if (cell.getCellType() == CellType.STRING) {
                                purchaseData.add(cell.getStringCellValue());
                            } else {
                                purchaseData.add(NumberToTextConverter.toText(cell.getNumericCellValue()));
                            }
                        }
                    }
                }
            }
        }
        return purchaseData;
    }
}
