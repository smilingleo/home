package leo.test.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class TimeClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String host = "localhost";
		int port = 8080;
		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		ClientBootstrap bootstrap = new ClientBootstrap(factory);
		
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new TimeClientHandler());
			}
		});
		
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
		
		ChannelFuture f = bootstrap.connect(new InetSocketAddress(host, port));
		// wait for the connection attempt succeeded, 
		// if succeeded, close() will be invoked,and CloseFuture will be created.
		f.awaitUninterruptibly();
		// if not succeeded, print error.
		if (!f.isSuccess()){
			f.getCause().printStackTrace();
		}
		
		// close synchroniously.
		f.getChannel().getCloseFuture().awaitUninterruptibly();
		// quit boss/worker threads.
		factory.releaseExternalResources();
	}

}
