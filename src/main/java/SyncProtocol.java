import lombok.Data;

@Data
public class SyncProtocol {


    public static final int HEAD_SIZE = 10;

    private final short version;
    private final short messageType;
    private final long sequenceNumber;


    public SyncProtocol(short version, short messageType, long sequenceNumber) {
        this.version = version;
        this.messageType = messageType;
        this.sequenceNumber = sequenceNumber;
    }


}
