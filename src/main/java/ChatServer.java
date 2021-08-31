import com.sun.security.ntlm.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ChatServer {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("server starting...");
        new ChatServer(8000).run();

    }

    private  final  int port ;

    public ChatServer(int port){
        this.port = port ;
    }

    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap  bootstrap = new ServerBootstrap()
               .group(bossGroup,workerGroup)
               .channel(NioServerSocketChannel.class)
               .childHandler(new ChatServerInitailizer());

            bootstrap.bind(port).sync().channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
