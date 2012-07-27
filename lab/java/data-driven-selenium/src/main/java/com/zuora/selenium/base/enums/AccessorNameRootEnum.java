package com.zuora.selenium.base.enums;

public enum AccessorNameRootEnum {
   // String
   Speed(String.class),
   Log(String.class),
   Alert(String.class),
   Confirmation(String.class),
   Prompt(String.class),
   Location(String.class),
   Title(String.class),
   BodyText(String.class),
   Value(String.class),
   Text(String.class),
   Eval(String.class),
   Table(String.class),
   SelectedLabel(String.class),
   SelectedValue(String.class),
   SelectedIndexe(String.class),
   SelectedId(String.class),
   Attribute(String.class),
   HtmlSource(String.class),
   Expression(String.class),
   Cookie(String.class),
   CookieByName(String.class),

   // String Array
   SelectedLabels(String[].class),
   SelectedValues(String[].class),
   SelectedIndexes(String[].class),
   SelectedIds(String[].class),
   SelectOptions(String[].class),
   AllButtons(String[].class),
   AllLinks(String[].class),
   AllFields(String[].class),
   AttributeFromAllWindows(String[].class),
   AllWindowIds(String[].class),
   AllWindowNames(String[].class),
   AllWindowTitles(String[].class),
   
   // Number
   MouseSpeed(Number.class),
   ElementIndex(Number.class),
   ElementPositionLeft(Number.class),
   ElementPositionTop(Number.class),
   ElementWidth(Number.class),
   ElementHeight(Number.class),
   CursorPosition(Number.class),
   XpathCount(Number.class),
   
   // Boolean
   WhetherThisFrameMatchFrameExpression(Boolean.class),
   WhetherThisWindowMatchWindowExpression(Boolean.class),
   AlertPresent(Boolean.class),
   PromptPresent(Boolean.class),
   ConfirmationPresent(Boolean.class),
   Checked(Boolean.class),
   SomethingSelected(Boolean.class),
   TextPresent(Boolean.class),
   ElementPresent(Boolean.class),
   Visible(Boolean.class),
   Editable(Boolean.class),
   Ordered(Boolean.class),
   CookiePresent(Boolean.class),
   ;
   
   private Class returnType;
   private AccessorNameRootEnum(Class type){
      this.returnType = type;
   }
   
   public Class getReturnType(){
      return this.returnType;
   }
}
