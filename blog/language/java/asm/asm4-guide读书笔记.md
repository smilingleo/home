* Overall structure of a compiled class
```java
+----------------------------------------------------------------------+
|Modifiers, name, super class, interfaces (of class)                   |
|Constant pool: numeric, string and type constants                     |
|Source file name *                                                    |
|Enclosing class reference                                             |
|Annotation *                                                          |
|Attribute *                                                           |
+-----------------+----------------------------------------------------+
|Inner Class*     | Name                                               |
+-----------------+----------------------------------------------------+
|Field*           | Modifiers, name, type                              |
|                 | Annotation*                                        |
|                 | Attribute*                                         |
+-----------------+----------------------------------------------------+
|Method*          | Modifiers, name, return and parameter types        |
|                 | Annotation*                                        |
|                 | Attribute*                                         |
|                 | Compiled code                                      |
+-----------------+----------------------------------------------------+
```

* Internal names
used to represent class/interface references, *fully qualified* class name with dot replaced by slash.
for example, internal name of GregorianCalendar is java/util/GregorianCalendar

* Type descriptors
field types, Java types are represented with *type descriptors*
```java
+-------------+-------------------------+
|Java Type    | Type descriptor         |
+-------------+-------------------------+
|boolean      | Z                       |
|char         | C                       |
|byte         | B                       |
|short        | S                       |
|int          | I                       |
|float        | F                       |
|int[]        | [I                      |
|Object       | Ljava/lang/Object;      |
|Object[][]   | [[Ljava/lang/Object;    |
+-------------+-------------------------+
```

* Method descriptors
```java
+-------------------------------------+-------------------------+
|Method declaration in source file    | Method descriptor       |
+-------------------------------------+-------------------------+
|void m(int i, float f)               | (IF)V                   |
|int m(Object o)                      | (Ljava/lang/Object;)I   |
|int[] m(int i, String s)             | (ILjava/lang/String;)[I |
+-------------------------------------+-------------------------+
```
(<param type descriptors>)<return type descriptor>
Note:
    No method name or argument names

* ClassVisitor must be called in the following order
visit visitSource? visitOuterClass? (visitAnnotation | visitAttribute)* (visitInnerClass | visitField | visitMethod)* visitEnd
Basically, this order is just natural flow represented by the compiled class structure.

* ClassVisitor就是一个典型的Visitor模式，同时还运用了delegate模式，这样就可以通过代理，进行装饰，从而实现class transforming
```java
byte[] b1 = ...;
ClassWriter cw = new ClassWriter(0);
// cv forwards all events to cw, cv acts as a filter here.
ClassVisitor cv = new ClassVisitor(ASM4, cw) { }; 
ClassReader cr = new ClassReader(b1);
cr.accept(cv, 0);
byte[] b2 = cw.toByteArray(); // b2 represents the same class as b1, since cv does nothing
```

