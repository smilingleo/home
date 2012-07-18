#Program Structure
+ module
+ require
+ function & type definitions
+ expression

#Data Structures
+ Symbol == interned string , `@name`
+ manipulating string is just like doing so against array/list

#Basic Expressions
+ no void functions in loop
+ `func.@call(...)` for closure
+ call-as-method,  first argument as `this`, `triple(i)` ====> `i.triple()`
+ if-then-else,  not support elseif
+ `<expression> for <var> in <list> [if filter]`
+ Nothing inherits all classes, global singleton

#Function
+ sequences is separated with comma
+ use `where` for variable setting, note: there is one more level of indent, and the variables is 'function scope' like JavsScript does.
+ expose one function in a module by prefix the function name with `@`
+ `@(arg)` for anonymous function

#Pattern Matching
+ string  patterns:
	* make it extremely easy to multipulate string, like substring, first, last, indexof, regexp matching etc.
* `[]` for array/list
* `()` for string
* and `Guards` to refine the condition. 

#Exception handling
+ not support checked exception
+ clean stack trace? definition of clear?

#classes and objects
+ classes in loop is purely static, aka, no method/verb
+ immutable keyword.
+ duck typing
+ [my guess] : classes in loop is just a map

#Java interop
+ add \`\` around static class of java to invoke static method `\`java.lang.Date\` == java.lang.Date.class`
+ use '::' to separate static class and static field

