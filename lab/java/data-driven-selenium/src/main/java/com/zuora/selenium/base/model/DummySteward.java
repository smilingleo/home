package com.zuora.selenium.base.model;
/**
 * For test purpose only.
 * @author Leo
 *
 */
public class DummySteward {

   public static void before(String tenantId, String accountName){
      System.out.println("in dummy steward, before testing, " + tenantId + ", " + accountName);
   }
   
   public static void after(String tenantId, String accountName){
      System.out.println("in dummy steward, after testing, " + tenantId + ", " + accountName);
   }
}
