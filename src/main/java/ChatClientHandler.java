import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ChatClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf returnMsg = (ByteBuf)msg ;

        char c = (char) returnMsg.readByte();
        System.out.println("received msg : ["+c+"]");
    }
}
