在loop中，@操作符有很多用法，这里简单总结一下：
* orb tags, 用来在一个字符串中嵌套一个表达式，比如：
``` ruby
print("hello, @{name}")
```

* 作为一个前缀，用来表示具有字面精度的数字
``` ruby 
print(@1234123412341234123423523)
```
另外，loop里面数字运算要求所有参与运算的精度必须一致，否则将报错

* interned string called symbol
```javascript
{
    @name: 'Leo Liu',
    @age : '100'
}
```

* calling functions
loop允许将一个function作为参数传递，那么接收这个function的代码需要调用这个function的时候，就可以通过`func.@call(args)`来调用了。

* module scope
如果在一个module里，想定义一些私有function，也就是不想让其他function能够访问的，可以在这个module里定义function的时候在其前面加上@, e.g.
```javascript
    @last(ls) ->
        ls[ls.length() - 1]

    [1,2,3].@last()
```
这里，`@last`只能在自己的module里可见。

* 匿名函数anonymous function
```javascript
    manipulate([1,2,3], @(x) -> { x * 5 })
```

