import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ChatClientInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        //pipeline.addLast("framer", new DelimiterBasedFrameDecoder(1892, Delimiters.lineDelimiter()));
        //pipeline.addLast("decoder", new StringDecoder());
        //pipeline.addLast("encoder",new StringEncoder());

        pipeline.addLast("handler",new ChatClientHandler());
    }
}
