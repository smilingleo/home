package com.zuora.selenium.base.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

public class TestDataCleaner extends TestCaseSteward{
   
   public TestDataCleaner(Class dataPreparationClazz, Method dataPreparationMethod, String[] parameters) {
      super(dataPreparationClazz, dataPreparationMethod, parameters);
   }
   public TestDataCleaner(String dataPreparationClassName, String dataPreparationMethodName, String... parameters) {
      super(dataPreparationClassName, dataPreparationMethodName, parameters);
   }
   /**
    * prepare data has side effect.
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws InvocationTargetException
    */
   public void cleanup() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
      method.invoke(clazz, parameters);
   }
   
   public static TestDataCleaner fromJSONString(String jsonString) {
      if (StringUtils.isBlank(jsonString)) return null;
      TestCaseSteward parent = TestCaseSteward.fromJSONString(jsonString);
      return new TestDataCleaner(parent.clazz, parent.method, parent.parameters);
   }
 
}
