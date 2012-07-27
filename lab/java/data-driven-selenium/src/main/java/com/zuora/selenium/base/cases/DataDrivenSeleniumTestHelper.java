package com.zuora.selenium.base.cases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverCommandProcessor;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.zuora.selenium.base.model.Command;
import com.zuora.selenium.base.model.DDTestCase;
import com.zuora.selenium.base.model.TestDataCleaner;
import com.zuora.selenium.base.model.TestDataProvider;
import com.zuora.selenium.base.utils.SeleneseCommandProcessor;
import com.zuora.utils.ResourceUtils;

public class DataDrivenSeleniumTestHelper {
   // \t as Delimiter
   public static CSVStrategy strategy = CSVStrategy.TDF_STRATEGY;

   private DDTestCase testCase;
   private static SeleneseCommandProcessor processor;
   private static Properties testProperties = new Properties();

   private static String baseURL = getWebAppBaseURL();
   private static WebDriverCommandProcessor webDriverCommandProcessor;
   
   public DataDrivenSeleniumTestHelper(){
      WebDriver driver = getWebDriver();
      webDriverCommandProcessor = new WebDriverCommandProcessor(baseURL, driver);
      processor = new SeleneseCommandProcessor(webDriverCommandProcessor);
   }
   
   public void setTestCase(DDTestCase testCase) {
      this.testCase = testCase;
   }



   static List<DDTestCase[]> loadTestDataUnder(File dir) throws IOException {
      List<DDTestCase[]> casesInAllFiles = new ArrayList<DDTestCase[]>();
      String[] fileNames = dir.list(new FilenameFilter() {
         public boolean accept(File dir, String name) {
            return new File(dir, name).isFile() && name.endsWith(".csv");
         }
      });
      
      for (int i = 0; i < fileNames.length; i++) {
         String filePath = dir + System.getProperty("file.separator") + fileNames[i];
         File file = new File(filePath);
         casesInAllFiles.addAll(loadTestDataFrom(file));
      }
      return casesInAllFiles;
   }

   static List<DDTestCase[]> loadTestDataFrom(File file) throws IOException {
      List<DDTestCase[]> rows = new ArrayList<DDTestCase[]>();
      List<String> rowData = new ArrayList<String>();
      CSVParser parser = new CSVParser(new InputStreamReader(new FileInputStream(file.getPath())), strategy);

      int lineNumber = 0;
      do {
         rowData.clear();
         String[] row = parser.getLine();
         if (row == null || StringUtils.isBlank(row[0])) break;

         DDTestCase testCase = DDTestCase.fromCSVRow(row);
         testCase.setTestDataFileName(file.getName());
         testCase.setRowNumber(++ lineNumber);
         rows.add(new DDTestCase[]{testCase});

      } while (true);
      return rows;
   }
   
   static Collection<DDTestCase[]> prepareData(String pathInClasspath) throws Exception{
      List<DDTestCase[]> casesInAllFiles = new ArrayList<DDTestCase[]>();
      try {
         File dir = ResourceUtils.getFile("classpath:" + pathInClasspath);
         casesInAllFiles = DataDrivenSeleniumTestHelper.loadTestDataUnder(dir);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return casesInAllFiles;
   }
   
   public void executeTestCase() throws Exception{
      System.out.println("--------------------------------------------------------\n" + testCase.toRawTestData());
      
      // interpret the interactions.
      for (int i = 0; i < testCase.getInteractionAmount(); i++) {
         Command interact = testCase.getInteractionAt(i);
         processor.doCommand(interact);
         TimeUnit.MICROSECONDS.sleep(100);
         System.out.println("Done:" + interact.toString());
      }
   
   }
   public void prepareForTest() throws Exception {
      TestDataProvider precondition = this.testCase.getDataProvider();
      if (precondition != null) {
         System.out.println("preparing for test....");
         precondition.prepare();
      }
   }  
   public void cleanupForTest() throws Exception {
      TestDataCleaner postcondition = testCase.getPostcondition();
      if (postcondition != null) {
         System.out.println("cleaning up for test....");
         postcondition.cleanup();
      }

   }
   
   public void teardown() {
	   webDriverCommandProcessor.stop();
   }

   static String getDefaultPassword() {
      String pwdInProperties = StringUtils.isBlank(getTestProperties().getProperty("test.password")) ? "Zuora123" : getTestProperties().getProperty("test.password");
      return StringUtils.isBlank(System.getProperty("TEST_PASSWORD")) ? pwdInProperties : System.getProperty("TEST_PASSWORD").trim();
   }
   
   static String getWebAppBaseURL(){
      String protocol = StringUtils.isBlank(getTestProperties().getProperty("selenium.protocol")) ? "http" : getTestProperties().getProperty("selenium.protocol");
      String host = StringUtils.isBlank(getTestProperties().getProperty("selenium.host")) ? "localhost" : getTestProperties().getProperty("selenium.host");
      String port = StringUtils.isBlank(getTestProperties().getProperty("selenium.port")) ? "8080" : getTestProperties().getProperty("selenium.port");
      String projectName = StringUtils.isBlank(getTestProperties().getProperty("selenium.projectName")) ? "apps" : getTestProperties().getProperty("selenium.projectName");
      return protocol + "://" + host + ":" + port + "/" + projectName + "/";
   }

   protected WebDriver getWebDriver() {
      String command = StringUtils.isBlank(getTestProperties().getProperty("selenium.browserStartCommand")) ? "*firefox" : getTestProperties().getProperty("selenium.browserStartCommand");
      WebDriver rtn = null;
      if (command.indexOf("firefox") > -1){
//         rtn = new FirefoxDriver();
         FirefoxProfile profile = new FirefoxProfile();
         profile.setEnableNativeEvents(true);
//         WebDriver driver = new FirefoxDriver(profile);
         rtn = new FirefoxDriver(profile);
      }else if (command.indexOf("iexplore") > -1){
         rtn = new InternetExplorerDriver();
      }else if (command.indexOf("chrome") > -1){
         rtn = new InternetExplorerDriver();
      }
      return rtn;
   }
   
   private static Properties getTestProperties(){
      if (!testProperties.isEmpty())
         return testProperties;
      
      synchronized (testProperties) {
         try {
            testProperties.load(new FileReader(ResourceUtils.getFile("classpath:config/test.properties")));
         }
         catch (FileNotFoundException e) {
            e.printStackTrace();
         }
         catch (IOException e) {
            e.printStackTrace();
         }
      }
      return testProperties;
   }
}