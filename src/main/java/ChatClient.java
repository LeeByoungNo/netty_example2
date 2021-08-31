import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ChatClient {

    private final String host ;
    private final int port ;

    private final Charset charset = Charset.forName("UTF-8");

    public ChatClient(String host,int port){
        this.host = host ;
        this.port = port ;
    }

    public static void main(String[] args) throws Exception {
        new ChatClient("localhost",8000).run();
    }

    public void run() throws IOException, InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                     .group(group)
                     .channel(NioSocketChannel.class)
                     .handler(new ChatClientInitializer());

            Channel channel = bootstrap.connect(host,port).sync().channel();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            while(true){

                String  messageType = in.readLine() ;
                System.out.println("messageType : ["+messageType+"]");

                if(messageType.equals("T")){           // test

                    System.out.println("messageType : ["+messageType+"] : TEST ");

                    String strMessage = "HELLO, WORLD" ;
                    int strMessageLength = strMessage.getBytes(StandardCharsets.UTF_8).length;

                    ByteBuf buf = Unpooled.buffer();
                    buf  //  HEADER ============================
                            .writeByte(0x06)  // version
                            .writeByte(0x11)  // message type
                            .writeInt(100)  // sequence number
                            .writeInt(strMessageLength);  // body length  --------------   HEADER

                    // BODY ============================
                    buf.writeCharSequence(strMessage,charset);

                    // send
                    channel.writeAndFlush(buf);

                }else if(messageType.equals("A")){

                    System.out.println("messageType : ["+messageType+"] : LDM ");
                    // contents
                    byte[]  imageContents = getImageFileBytes("C:\\Users\\leebn\\OneDrive\\문서\\LDM_프로젝트\\secure-03.PNG");


                    ByteBuf buf = Unpooled.buffer();
                    buf  //  HEADER ============================
                            .writeByte(0x06)  // version
                            .writeByte(0x09)  // message type    -- COMPRESS_SUBMIT
                            .writeInt(100)  // sequence number

                            .writeInt(2+1+4+imageContents.length);  // body length  --------------   HEADER
                    // 2 (Policy Version) + 1 (Vehicle Message Type )  + 4 (Data Length) + n (Data)
                    // BODY ============================
                    // SUB BODY
                    //   SUB HEADER
                    buf.writeShort(0x0000)   //  Policy Version : Reserved
//                            .write                        //
                            .writeByte(35)                    // Vehicle Message Type (35: 차량LDM HMI 화면 이미지 데이터)
                            .writeInt(imageContents.length)  // Data Length
                            .writeBytes(imageContents);       // Data
                    //   SUB MESSAGE
                    //   image data ---------------

                    // SUB BODY

                    // send
                    channel.writeAndFlush(buf);

                }else{
                    System.out.println("messageType : ["+messageType+"] NOT KNOWN");
                    continue;
                }



                System.out.println("sent message....");

            }
        }finally {
            group.shutdownGracefully();
        }
    }

    private byte[] getImageFileBytes(String filePath){

        File hmiImage = new File(filePath);

        try {
            FileInputStream fis = new FileInputStream(hmiImage);

            long fileLength = hmiImage.length();

            byte[] contents = new byte[(int)fileLength];

            int readLength = fis.read(contents);
            return contents ;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null ;
    }
}
