package com.zuora.selenium.base.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.zuora.selenium.base.model.Command;
import com.zuora.selenium.base.model.DDTestCase;
import com.zuora.selenium.base.model.DDTestCaseMeta;

public class TestDataGenerateUtils {

   public static List<DDTestCase> generateForAllSupportedLocales(DDTestCaseMeta sampleMeta) throws Exception{
      List<DDTestCase> generated = new ArrayList<DDTestCase>();
      for (Locale locale : Locale.getAvailableLocales()){
         DDTestCase newCase = generateForLocale(sampleMeta, locale);
         
         generated.add(newCase);
      }
      return generated;
   }

   private static DDTestCase generateForLocale(DDTestCaseMeta sampleMeta, Locale locale) throws ParseException {
      NumberFormat nf = NumberFormat.getNumberInstance(locale);
      
      DDTestCase newCase = sampleMeta.testCase.clone();
      for (Map.Entry<Integer, Class> entry: sampleMeta.fieldTypeInOperations.entrySet()){
         updateInteractionWithLocalizedNumber(sampleMeta.testCase, newCase, nf, entry);
      }
      
      for (Map.Entry<Integer, Class> entry: sampleMeta.fieldTypeInExpectations.entrySet()){
         updateExpectationWithLocalizedNumber(sampleMeta.testCase, newCase, nf, entry);
      }
      return newCase;
   }

   private static void updateInteractionWithLocalizedNumber(DDTestCase sample, DDTestCase newCase, NumberFormat nf, Map.Entry<Integer, Class> entry) throws ParseException {
      Class clazz = entry.getValue();
      int index = entry.getKey();
      Command action = sample.getInteractionAt(index);
      
      if (Long.class.equals(clazz)){
         Long num = nf.parse(action.value).longValue();
         String grouped = nf.format(num);
         //TODO: Ungrouped.
         
         Command newAction = newCase.getInteractionAt(index);
         newAction.value = grouped;
         newCase.replaceInteractionAt(index, newAction);
      }else if (Float.class.equals(clazz)){
         
      }else if (Currency.class.equals(clazz)){
         
      }
   }
   
   private static void updateExpectationWithLocalizedNumber(DDTestCase sample, DDTestCase newCase, NumberFormat nf, Map.Entry<Integer, Class> entry) throws ParseException {
      //TODO:
   }
   
   public static void main(String[] args) throws Exception{
      DDTestCase testCase = new DDTestCase();
      testCase.setDescription("login to zuora");
      
      Command openLogin = new Command("openAndWait", "https://maintest.zuora.com/maintest/newlogin.do", "");
      Command typeUsername = new Command("type", "id=id_username", "superadmin@zuora.com");
      Command typePasswd = new Command("type", "id=id_password", "123456");
      Command submitLogin = new Command("clickAndWait", "id=login_button", "");
      
      Command waitPageToLoad = new Command("waitForPageToLoad", "60000", "");
      
      testCase.addInteraction(openLogin);
      testCase.addInteraction(typeUsername);
      testCase.addInteraction(typePasswd);
      testCase.addInteraction(submitLogin);
      testCase.addInteraction(waitPageToLoad);
      
      System.out.println(testCase.toRawTestData());
      
      
      DDTestCase calloutOptionCase = new DDTestCase();
      calloutOptionCase.setDescription("callout retry interval locale test");
      Command openSetting = new Command("openAndWait", "/CalloutRetryOption.do?method=viewOptions&category=ZPayments&backToFlag=ZPayments", "");
      Command clickEdit = new Command("click", "//a/span[text()='edit']", "");
      Command waitForm = new Command("waitForElementPresent", "id=intervalTime", "");
      Command typeNumber = new Command("type", "id=intervalTime", "1234");
      Command clickSave = new Command("click", "//a/span[text()='save']", "");
      Command waitView = new Command("waitForElementPresent", "id=ST_description", "");
      
      calloutOptionCase.addInteraction(openSetting);
      calloutOptionCase.addInteraction(clickEdit);
      calloutOptionCase.addInteraction(waitForm);
      calloutOptionCase.addInteraction(typeNumber);
      calloutOptionCase.addInteraction(clickSave);
      calloutOptionCase.addInteraction(waitView);
      
//      TestDataProvider precondition = new TestDataProvider(UserLoginUtil.class.getName(), "changeLocale", "zh_CN","");
//      calloutOptionCase.setPrecondition(precondition);
      
      System.out.println(calloutOptionCase.toRawTestData());
      
//      DDTestCaseMeta meta = new DDTestCaseMeta(calloutOptionCase);
//      meta.fieldTypeInOperations.put(3, Long.class);
//      meta.fieldTypeInExpectations.put(0, Long.class);
//      
//      List<DDTestCase> generated = generateForAllSupportedLocales(meta);
//      for(DDTestCase newCase : generated){
//         System.out.println(newCase.toRawTestData());
//      }
   }
}
