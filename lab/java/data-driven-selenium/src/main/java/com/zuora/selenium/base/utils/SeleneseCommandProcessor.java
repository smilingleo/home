package com.zuora.selenium.base.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverCommandProcessor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.thoughtworks.selenium.CommandProcessor;
import com.zuora.selenium.base.enums.AccessorNameRootEnum;
import com.zuora.selenium.base.model.Command;

public class SeleneseCommandProcessor {

   private CommandProcessor processor;

   public SeleneseCommandProcessor(CommandProcessor processor) {
      super();
      this.processor = processor;
   }

   public String doCommand(Command interaction) {
      String command = interaction.command;
      String[] args = new String[] { interaction.target, interaction.value };

      boolean isAccessor = isAccessor(command);

      if (isAccessor) {
         boolean rtn = parseAccessor(command, args);
         return String.valueOf(rtn);
      }
      else {
         return parseAction(command, args);
      }
   }

   private String parseAction(String command, String[] args) {
      String cmd = command;
      if (command.endsWith("AndWait")) {
         cmd = cmd.replace("AndWait", "");
      }
      
      if (command.equals("mouseOver")){
    	 WebDriver driver = ((WebDriverCommandProcessor)processor).getWrappedDriver();
    	 WebElement toElement = null;
    	 if(args[0].startsWith("id=")){
    		 args[0] = args[0].replace("id=", "");
    		 toElement = driver.findElement(By.id(args[0]));
    	 }else if(args[0].startsWith("name=")){
    		 args[0] = args[0].replace("name=", "");
    		 toElement = driver.findElement(By.name(args[0]));
    	 }else if(args[0].startsWith("css=")){
    		 args[0] = args[0].replace("css=", "");
    		 toElement = driver.findElement(By.cssSelector(args[0]));
    	 }else if(args[0].startsWith("xpath")){
    		 args[0] = args[0].replace("xpath=", "");
    		 toElement = driver.findElement(By.xpath(args[0]));
    	 }else{//if no prefix
    		 try{
    			 toElement = driver.findElement(By.xpath(args[0]));
    		 }catch(Exception e1){
    			 try{
    				 toElement = driver.findElement(By.id(args[0]));
    			 }catch(Exception e2){
    				 try{
    					 toElement = driver.findElement(By.name(args[0]));
    				 }catch(Exception e3){
    					 try{
    						 toElement = driver.findElement(By.cssSelector(args[0])); 
    					 }catch(Exception e4){
    						 throw new RuntimeException("please check your mouse over locator,make sure it is id or name or css or xpath ");
    					 }
    				 }
    				 
    			 }
    		 }
    	 }
    		 

    	 Actions actions = new Actions(driver);
    	 actions.moveToElement(toElement).build().perform();       	
    	 return "";
      }else{
    	  String rtn = processor.doCommand(cmd, args);
    	  return rtn;
      }
   }

   private boolean parseAccessor(String command, String[] args) {
      if (command.startsWith("store")) {
         throw new RuntimeException("Accessor commands like 'storeXxx' are not supported yet.");
      }

      boolean rtn = false;
      if (command.startsWith("waitFor")) {
         rtn = handleWaitForCommand(command, args);
      }
      else {
         doAssert(command, args);
      }
      return rtn;
   }

   private void doAssert(String command, String[] args) {
      Matcher matcher = ACCESSOR_PATTERN.matcher(command);
      String name = getGroup(matcher, 2);
      String locator = args[0];
      boolean isNegative = isNegative(command);
      if (isNegative)
         name = name.replace("Not", "");

      String value = args[1];

      if (AccessorNameRootEnum.valueOf(name).getReturnType().equals(Boolean.class)) {
         // isXXX
         boolean bValue = processor.getBoolean("is" + name, new String[] { locator, "" });
         if (isNegative) Assert.assertFalse(bValue);
         else Assert.assertTrue(bValue);
      }
      else if (AccessorNameRootEnum.valueOf(name).getReturnType().equals(Number.class)) {
         Number numValue = processor.getNumber("get" + name, new String[] { locator, "" });
         try {
            if (isNegative) Assert.assertNotSame(NumberFormat.getInstance().parse(value), numValue);
            else Assert.assertEquals(NumberFormat.getInstance().parse(value), numValue);
         }
         catch (ParseException e) {
            Assert.assertTrue("invalid format:" + e.getMessage(), false);
            e.printStackTrace();
         }
      }
      else if (AccessorNameRootEnum.valueOf(name).getReturnType().equals(String.class)) {
         String strValue = processor.getString("get" + name, new String[] { locator, "" });
         if (isNegative) Assert.assertNotSame(value, strValue);
         else Assert.assertEquals(value, strValue);
      }

   }

   private boolean handleWaitForCommand(String command, String[] args) {
      Matcher matcher = ACCESSOR_PATTERN.matcher(command);
      String name = getGroup(matcher, 2);
      boolean isNegative = isNegative(command);
      if (isNegative)
         name = name.replace("Not", "");

      int count = 0;

      if (AccessorNameRootEnum.valueOf(name).getReturnType().equals(Boolean.class)) {
         count = waitForBoolean(args, name, isNegative, count);
      }
      else if (AccessorNameRootEnum.valueOf(name).getReturnType().equals(String.class)) {
         count = waitForString(args, name, isNegative, count);
      }
      else if (AccessorNameRootEnum.valueOf(name).getReturnType().equals(String[].class)) {
         count = waitForStringArray(args, name, isNegative, count);
      }
      else if (AccessorNameRootEnum.valueOf(name).getReturnType().equals(Number.class)) {
         count = waitForNumber(args, name, isNegative, count);
      }

      if (count >= 30) {
         throw new RuntimeException("30 seconds passed, time out.");
      }
      return true;
   }

   private int waitForNumber(String[] args, String name, boolean isNegative, int count) {
      while (count < 30) {
         Number rtn = processor.getNumber("get" + name, args);
         if (isNegative && !rtn.equals(0) || // waitForNot, but IS
            !isNegative && rtn.equals(0)) { // waitFor, but NOT
            count++;
            sleepFor(1000);
         }
         else {
            break;
         }
      }
      return count;
   }

   private int waitForStringArray(String[] args, String name, boolean isNegative, int count) {
      // getStringArray
      while (count < 30) {
         String[] rtn = processor.getStringArray("get" + name, args);
         if (isNegative && !ArrayUtils.isEmpty(rtn) || // waitForNot, but IS
            !isNegative && ArrayUtils.isEmpty(rtn)) { // waitFor, but NOT
            count++;
            sleepFor(1000);
         }
         else {
            break;
         }
      }
      return count;
   }

   private int waitForString(String[] args, String name, boolean isNegative, int count) {
      while (count < 30) {
         String rtn = processor.getString("get" + name, args);
         if (isNegative && StringUtils.isNotBlank(rtn) || // waitForNot, but IS
            !isNegative && StringUtils.isBlank(rtn)) { // waitFor, but NOT
            count++;
            sleepFor(1000);
         }
         else {
            break;
         }
      }
      return count;
   }

   private int waitForBoolean(String[] args, String name, boolean isNegative, int count) {
      while (count < 30) {
         boolean isPresent = processor.getBoolean("is" + name, args);
         if (isNegative && isPresent || // waitForNot, but IS
            !isNegative && !isPresent) { // waitFor, but NOT
            count++;
            sleepFor(1000);
         }
         else {
            break;
         }
      }
      return count;
   }

   private void sleepFor(long milliseconds) {
      try {
         TimeUnit.MILLISECONDS.sleep(milliseconds);
      }
      catch (Exception e) {

      }
   }

   private boolean isAccessor(String command) {
      Matcher matcher = ACCESSOR_PATTERN.matcher(command);
      return matcher.matches();
   }

   private boolean isNegative(String command) {
      return ACCESSOR_PATTERN.matcher(command).matches() && command.indexOf("Not") > 0;
   }

   private String getGroup(Matcher matcher, int index) {
      int groupCount = matcher.groupCount();
      if (index > groupCount) return null;
      while (matcher.find()) {
         for (int i = 0; i <= groupCount; i++) {
            if (i == index)
               return matcher.group(i);
         }
      }
      return null;
   }

   private static Pattern ACCESSOR_PATTERN = Pattern.compile("^(store|assert|verify|waitFor)(.*)$");
}
