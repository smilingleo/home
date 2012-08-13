package leo.test.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * Hello world!
 *
 */
public class TimeServer 
{
    public static void main( String[] args )
    {
        ChannelFactory factory = new NioServerSocketChannelFactory(
        		// boss threads
        		Executors.newCachedThreadPool(),
        		// worker threads
        		Executors.newCachedThreadPool());
        
        
        ServerBootstrap bootstrap = new ServerBootstrap(factory);
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new TimeServerHandler());
			}
		});
        
        bootstrap.setOption("child.keepAlive", true);
        
        bootstrap.bind(new InetSocketAddress(8080));
    }
}
