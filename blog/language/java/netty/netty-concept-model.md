## Channel
* Channel是一个抽象逻辑概念，描述一个Socket连接通道。
    + channel有状态，open, connected, closed, etc
    + channel的操作read, write, connect, bind
    + channel的配置选项
    + ChannelPipeline (handler set)，其负责处理所有I/O event和请求
    + Channel有两端SocketAddress，分别是LocalAddress and RemoteAddress
* ChannelPipeline相比Channel是一个具体的、物理的管道，该管道由多个ChannelHandler构成。

## ChannelEvent
Channel的Pipeline里有很多handler，从第一个handler传导到最后一个handle的，是upstream，从最后到第一个的是downstream。
这里的upstream和downstream与组件本身所处的位置有关，如果是server端，那么请求来了之后的处理过程就是一个upstream，响应过程是一个downstream，相反，如果是在客户端，那么发送请求就是downstream，接受响应就是upstream。
    + Upstream event
    + Downstream event

一个子接口是：MessageEvent，其有一个getMessage()方法，类似一个返回payload的逻辑。该方法返回Object类型，不过很多情况下可以类型转换为：ChannelBuffer。（为什么不直接定义为返回ChannelBuffer ??)
因为TcpIP中Socket Buffer只是一个简单的byte数组，没有任何的边界用来区分哪些字节是哪个数据包的，非常可能因为取数据时取到错误的边界导致IndexOutOfBoundsException。
+---+---+---+
|ABC|DEF|GHI|
+---+---+---+
+--+-----+-+-+
|AB|CDEFG|H|I|
+--+-----+-+-+
解决办法有两个，但基本思路都是在两边都满足一个共同的协议，比如确定每次发送的数据包大小；或者发送端设计一个encoder，接收端相应地设计一个decoder。着就是特殊的Handler FrameDecoder的目的

## ChannelFuture
所有Channle I/O操作都是异步的，ChannelFuture描述了异步操作的结果。
```java

                                      +---------------------------+
                                      | Completed successfully    |
                                      +---------------------------+
                                 +---->      isDone() = true      |
 +--------------------------+    |    |   isSuccess() = true      |
 |        Uncompleted       |    |    +===========================+
 +--------------------------+    |    | Completed with failure    |
 |      isDone() = false    |    |    +---------------------------+
 |   isSuccess() = false    |----+---->   isDone() = true         |
 | isCancelled() = false    |    |    | getCause() = non-null     |
 |    getCause() = null     |    |    +===========================+
 +--------------------------+    |    | Completed by cancellation |
                                 |    +---------------------------+
                                 +---->      isDone() = true      |
                                      | isCancelled() = true      |
                                      +---------------------------+
```
* 用事件监听器模型addListener(ChannelFutureListener)，不要用await()，因为是异步non-blocking模型。用await就会block当前线程。
* 习惯异步模式编程是个艰苦的过程，很多原来顺序执行的逻辑都要写在callback中，而且要理解closure的影响。

