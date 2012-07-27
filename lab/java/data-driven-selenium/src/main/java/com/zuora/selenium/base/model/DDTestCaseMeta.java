package com.zuora.selenium.base.model;

import java.util.HashMap;
import java.util.Map;

public class DDTestCaseMeta {
   
   public DDTestCase testCase;
   public Map<Integer, Class> fieldTypeInOperations = new HashMap<Integer, Class>();
   public Map<Integer, Class> fieldTypeInExpectations = new HashMap<Integer, Class>();
   
   public DDTestCaseMeta(DDTestCase testCase) {
      super();
      this.testCase = testCase;
   }
   
}
