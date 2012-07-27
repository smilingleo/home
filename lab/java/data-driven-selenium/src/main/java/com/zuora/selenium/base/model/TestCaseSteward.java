package com.zuora.selenium.base.model;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A helper class to facilitate the test, such as prepare some data 
 * at the beginning of the test and clean up some data after the test case finished. 
 * @author Leo
 *
 */
public class TestCaseSteward {

   protected String preparationMethodName;
   protected String[] parameters;
   protected Method method;
   protected Class clazz;

   public String[] getParameters() {
	return parameters;
}


public void setParameters(String[] parameters) {
	this.parameters = parameters;
}


public TestCaseSteward(String dataPreparationClassName, String dataPreparationMethodName, String... parameters) {
      try {
         clazz = Class.forName(dataPreparationClassName);
         this.method = getMethod(clazz, dataPreparationMethodName, parameters);
         this.parameters = parameters;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      
      checkMethod(this.method);
   }


   public TestCaseSteward(Class dataPreparationClazz, Method dataPreparationMethod, String... parameters) {
      this.clazz = dataPreparationClazz;
      this.method = dataPreparationMethod;
      this.parameters = parameters;
      
      checkMethod(this.method);
   }

   public String toJSONString() {

      String rtn = null;
      try {
         JSONObject json = new JSONObject();
         json.put("class", clazz.getName());
         json.put("method", method.getName());
         JSONArray array = new JSONArray();
         for (String param : parameters) {
//        	 json.append("parameters", param);
            array.put(param);
         }
         json.put("parameters", array);
         rtn = json.toString();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return rtn;
   }

   public static TestCaseSteward fromJSONString(String jsonString) {
      TestCaseSteward rtn = null;
      try {
         JSONObject json = new JSONObject(jsonString);
         String classStr = json.getString("class");
         String methodStr = json.getString("method");
         JSONArray paramArray = json.getJSONArray("parameters");
         List<String> paramList = new ArrayList<String>();
         for (int i = 0; i < paramArray.length(); i++) {
            paramList.add(paramArray.getString(i));
         }
         rtn = new TestCaseSteward(classStr, methodStr, paramList.toArray(new String[paramList.size()]));
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return rtn;
   }

   private Method getMethod(Class clazz, String methodName, String[] parameters) {
      Method[] methods = clazz.getDeclaredMethods();
      Method rtn = null;

      for (Method method : methods) {
         if (!method.getName().equals(methodName)) continue;

         Class<?>[] parameterTypes = method.getParameterTypes();
         if (parameterTypes.length != parameters.length) continue;

         boolean match = true;
         for (Class c : parameterTypes) {
            if (!c.equals(String.class)) {
               match = false;
               break;
            }
         }

         if (match) {
            rtn = method;
            break;
         }
      }
      return rtn;
   }

   private void checkMethod(Method method) {
      if (!Modifier.isStatic(method.getModifiers())){
         throw new RuntimeException("This framework only allow static method...");
      }
      
      if (method.getParameterTypes().length != parameters.length) 
         throw new RuntimeException("The number of parameter of the preparation method doesn't match!");
      
      if (!Modifier.isPublic(method.getModifiers())){
         throw new RuntimeException("the method is inaccessible, please set it to public.");
      }
   }   
}
