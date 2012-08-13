## Channel
* Channel是一个抽象逻辑概念，描述一个Socket连接通道。
    + channel有状态，open, connected, closed, etc
    + channel的操作read, write, connect, bind
    + channel的配置选项
    + ChannelPipeline (handler set)，其负责处理所有I/O event和请求
    + Channel有两端SocketAddress，分别是LocalAddress and RemoteAddress
* ChannelPipeline相比Channel是一个具体的、物理的管道，该管道由多个ChannelHandler构成。

* ChannelEvent
Channel的Pipeline里有很多handler，从第一个handler传导到最后一个handle的，是upstream，从最后到第一个的是downstream。
这里的upstream和downstream与组件本身所处的位置有关，如果是server端，那么请求来了之后的处理过程就是一个upstream，响应过程是一个downstream，相反，如果是在客户端，那么发送请求就是downstream，接受响应就是upstream。
    + Upstream event
    + Downstream event
