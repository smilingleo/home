package com.zuora.selenium.base.model;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

/**
 * represent an interact on UI, do what command on what target with what value.
 * It's a wrapper of selenium command, which might be one of :
 * <ul>
 * <li>Action, like click, open, type...</li>
 * <li>Accessor, like storeXXX, waitForXXX, </li>
 * <li>Assertion, like assertXXX, verifyXXX </li>
 * </ul>
 * Note: storeXXX is not supportted yet.
 * @author Leo
 *
 */
public class Command {
   public String command;
   public String target = "";
   public String value = "";
   
   public Command(String command, String target, String value) {
      super();
      this.command = command;
      this.target = target;
      this.value = value;
   }
   
   @Override
   public String toString(){
      return toJSONString();
   }
   
   public String toJSONString(){
      StringBuilder sb = new StringBuilder();
      sb.append("{\"command\":\"");
      sb.append(command);
      sb.append("\",\"target\":\"");
      sb.append(target);
      sb.append("\",\"value\":\"");
      sb.append(value == null ? "" : value);
      sb.append("\"}");
      return sb.toString();
   }
   public static Command fromJSONString(String jsonStr){
      Command rtn = null;
      try {
         JSONObject json = new JSONObject(jsonStr);
         Object cmdObj = json.get("command");
         Object targetObj = json.get("target");
         Object valueObj = json.get("value");
         if (cmdObj == null || targetObj == null){
            return null;
         }
         rtn = new Command(cmdObj.toString(), targetObj.toString(), StringUtils.isEmpty((String)valueObj) ? null : (String)valueObj);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return rtn;
   }
   
   public static void main(String[] args){
      String json = "{'command':'click','target':'targetEle','value':'lskdjfl'}";
      Command op = fromJSONString(json);
      System.out.println(op);
   }
}
