import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Base64;

public class TestByteBuf {

    private final static Charset charset = Charset.forName("UTF-8");

    public static void main(String[] args) {


        File hmiImage = new File("C:\\Users\\leebn\\OneDrive\\문서\\LDM_프로젝트\\secure-03.PNG");

        try {
            FileInputStream  fis = new FileInputStream(hmiImage);

            long fileLength = hmiImage.length();
            byte[] contents = new byte[(int)fileLength];

            System.out.println("file length : "+fileLength);

            int readLength = fis.read(contents);

            System.out.println("read byte length : "+readLength);

            fis.close();

            // base64 로   bytes를 encoding.....
            byte[] encoded = Base64.getEncoder().encode(contents);
            System.out.println("encoded length : "+encoded.length);

            // base64 로   bytes를 decoding.....
            byte[] decoded = Base64.getDecoder().decode(encoded);
            System.out.println("decoded length : "+decoded.length);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
