package com.zuora.selenium.base.model;

import java.util.ArrayList;
import java.util.List;

import com.zuora.selenium.base.cases.DataDrivenSeleniumTestHelper;

public class DDTestCase implements Cloneable{
   public static final String SEPARATOR = "##";
   String testDataFileName;
   int rowNumber;
   String description;
   TestDataProvider precondition;
   List<Command> interactions = new ArrayList<Command>();
   TestDataCleaner postcondition;
   
   public String getTestDataFileName() {
      return testDataFileName;
   }
   public void setTestDataFileName(String testDataFileName) {
      this.testDataFileName = testDataFileName;
   }
   public int getRowNumber() {
      return rowNumber;
   }
   public void setRowNumber(int rowNumber) {
      this.rowNumber = rowNumber;
   }
   public String getDescription() {
      return description;
   }
   public void setDescription(String description) {
      this.description = description;
   }
   
   public void addInteraction(Command interaction){
      if (interaction != null)
         this.interactions.add(interaction);
   }
   
   public Command getInteractionAt(int index){
      if (index < 0 || index > interactions.size()) return null;
      
      return this.interactions.get(index);
   }
   
   public void replaceInteractionAt(int index, Command action){
      if (index < 0 || index > interactions.size()) return;
      interactions.set(index, action);
   }
   
   public int getInteractionAmount(){
      return this.interactions.size();
   }
   
   
   public TestDataProvider getDataProvider() {
      return precondition;
   }
   public void setPrecondition(TestDataProvider precondition) {
      this.precondition = precondition;
   }
   public TestDataCleaner getPostcondition() {
      return postcondition;
   }
   public void setPostcondition(TestDataCleaner postcondition) {
      this.postcondition = postcondition;
   }

   /**
    * <pre>
    * To serialize the test case and return a raw test data in form of String.
    * Format:
    *    <description>\t##\t<precondition>*\t##\t<operation>*\t##<expectation>*
    * </pre>
    * @return
    */
   public String toRawTestData(){
      char DEMILITER = DataDrivenSeleniumTestHelper.strategy.getDelimiter();
      StringBuilder sb = new StringBuilder();
      String[] csvRow = this.toCSVRow();
      for (int i=0; i<csvRow.length; i++){
         sb.append(csvRow[i]);
         if (i != csvRow.length - 1)
            sb.append(DEMILITER);
      }
      return sb.toString();
   }
   
   public String[] toCSVRow(){
      List<String> list = new ArrayList<String>();
      list.add(this.description);
      
      list.add(SEPARATOR);
      if (this.precondition != null)
         list.add(this.precondition.toJSONString());
      
      list.add(SEPARATOR);
      for (int i=0; i<this.interactions.size(); i++)
         list.add(interactions.get(i).toJSONString());
      
      list.add(SEPARATOR);
      if (this.postcondition != null)
         list.add(this.postcondition.toJSONString());
      
      return list.toArray(new String[list.size()]);
   }
   
   public static DDTestCase fromCSVRow(String[] row){
      // col 1: description
      DDTestCase testCase = new DDTestCase();
      
      int sepSequence = 0;
      for (int i=0; i<row.length; i++){

         if (SEPARATOR.equals(row[i].trim())){
            sepSequence++;
            continue;
         }
         
         switch (sepSequence) {
            case 0: // for description
               testCase.setDescription(row[i]);
               break;
            case 1: // for precondition
               testCase.setPrecondition(TestDataProvider.fromJSONString(row[i]));
               break;
            case 2: // interactions
               testCase.addInteraction(Command.fromJSONString(row[i]));
               break;
            case 3: // postcondition
               testCase.setPostcondition(TestDataCleaner.fromJSONString(row[i]));
               break;
            default:
               break;
         }
      }
      
      return testCase;
   }
   
   @Override
   public String toString(){
      return this.testDataFileName + "," + this.rowNumber + "," + this.description;
   }
   
   public DDTestCase clone(){
      DDTestCase newCase = new DDTestCase();
      newCase.setDescription(this.description);
      newCase.setPrecondition(TestDataProvider.fromJSONString(precondition.toJSONString()));
      for (Command act : interactions){
         newCase.interactions.add(Command.fromJSONString(act.toJSONString()));
      }
      return newCase;
   }
}
