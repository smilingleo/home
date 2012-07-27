package com.zuora.selenium.base.cases;

import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.zuora.selenium.base.DDTestCaseRunner;
import com.zuora.selenium.base.DDTestCaseRunner.Parameters;
import com.zuora.selenium.base.model.DDTestCase;

@RunWith(DDTestCaseRunner.class)
public class DataDrivenSeleniumSimpleTest {


   private static DataDrivenSeleniumTestHelper helper;
   private DDTestCase testCase;

   public DataDrivenSeleniumSimpleTest(DDTestCase testCase) {
      this.testCase = testCase;
      helper.setTestCase(testCase);
   }

   @Test
   public void test() throws Exception {

      helper.executeTestCase();

   }


   /**
    * Hook method, extend this method to prepare data for the test case.
    * 
    * @param preconditions
    */
   @Before
   public void prepareForTest() throws Exception {
      helper.prepareForTest();
   }

   @After
   public void cleanupForTest() throws Exception {
      helper.cleanupForTest();
   }

   @BeforeClass
   public static void runBeforeClass() throws Exception {
      helper = new DataDrivenSeleniumTestHelper();
//      String username = helper.createNewTenantAndUser();
//      helper.login(username, helper.getDefaultPassword());
//      helper.loginAsAdmin();
   }

   /**
    * will read all data file under certain folder, and read data from data file and parse into DDTestCase.
    * 
    * @return
    * @throws Exception
    */
   @Parameters
   public static Collection<DDTestCase[]> data() throws Exception {
      return helper.prepareData("test_data");
   }

   @AfterClass
   public static void runAfterClass() {

   }
}
