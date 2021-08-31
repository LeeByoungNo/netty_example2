import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class SyncProtocolDecoder extends ReplayingDecoder<SyncProtocol> {

    private final Charset charset = Charset.forName("UTF-8");

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        // HEADER
        short version = byteBuf.readByte() ;
        short messageType = byteBuf.readByte() ;
        long sequenceNumber = byteBuf.readInt() ;
        long bodyLength = byteBuf.readInt() ;

        System.out.println("version : "+version);
        System.out.println("messageType : "+messageType);
        System.out.println("sequenceNumber : "+sequenceNumber);
        System.out.println("body length : "+bodyLength);

        // BODY
        byte[] contentBytes = new byte[(int)bodyLength];
        byteBuf.readBytes(contentBytes);


        SyncProtocol syncProtocol = new SyncProtocol(version,messageType,sequenceNumber);

        if(messageType == 0x11){        // test code

            ByteBuf targetBuf = Unpooled.buffer();

            targetBuf.writeBytes(contentBytes);

            CharSequence str = targetBuf.readCharSequence((int)bodyLength,charset);

            System.out.println("str : "+str.toString());
        }else if(messageType == 0x09){  // COMPRESS_SUBMIT

            ByteBuf targetBuf = Unpooled.buffer();

            targetBuf.writeBytes(contentBytes);

            // SUB HEADER parsing
            short policyVersion = targetBuf.readShort() ;
            int vehicleMessageType =  targetBuf.readByte() ;
            int dataLength = targetBuf.readInt() ;

            // SUB MESSAGE
            byte[] dataBytes = new byte[(int)dataLength];
            targetBuf.readBytes(dataBytes);

            FileOutputStream fos = null ;
            try {
                File outImage = new File("C:\\Users\\leebn\\OneDrive\\문서\\LDM_프로젝트\\secure-03_test1.PNG");
                if (!outImage.exists()) {
                    outImage.createNewFile();
                }
                fos = new FileOutputStream(outImage);
                fos.write(dataBytes);

                System.out.println("file copied ...."+dataBytes.length);

            }catch (IOException ie){
                System.err.println("File create Error!!");
            }finally {
                fos.close();
            }

        }else{
            System.out.println("unknown message type");
        }

        list.add(syncProtocol);
    }
}
