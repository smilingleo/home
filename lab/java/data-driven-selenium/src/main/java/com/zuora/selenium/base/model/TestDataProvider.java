package com.zuora.selenium.base.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

public class TestDataProvider extends TestCaseSteward{
   
   public TestDataProvider(Class dataPreparationClazz, Method dataPreparationMethod, String[] parameters) {
      super(dataPreparationClazz, dataPreparationMethod, parameters);
   }

   public TestDataProvider(String dataPreparationClassName, String dataPreparationMethodName, String... parameters) {
      super(dataPreparationClassName, dataPreparationMethodName, parameters);
   }
   /**
    * prepare data has side effect.
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws InvocationTargetException
    */
   public void prepare() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
      method.invoke(clazz, parameters);
   }
   
   public static TestDataProvider fromJSONString(String jsonString) {
      if (StringUtils.isBlank(jsonString)) return null;
      TestCaseSteward parent = TestCaseSteward.fromJSONString(jsonString);
      return new TestDataProvider(parent.clazz, parent.method, parent.parameters);
   }
 
}
