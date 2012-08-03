Developing a MapReduce Application
==============================

1. 对于分布式环境下如何测试？
基本方式是通过模拟cluster，然后小数据量环境，最后大数据量环境下进行分别测试。
运行相应环境的方式是在启动hadoop的时候指定其配置文件，可以通过local(file system, no server at all)、localhost(local server)以及cluster的三种级别来启动。
