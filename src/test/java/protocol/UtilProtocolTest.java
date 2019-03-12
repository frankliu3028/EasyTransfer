package protocol;

import org.junit.Test;
import utils.Log;
import utils.LogLevel;

import static org.junit.Assert.*;

public class UtilProtocolTest {

    public static final String TAG = UtilProtocolTest.class.getSimpleName();

    @Test
    public void readFromBytes() {
        BasicProtocol protocol = new BasicProtocol();
        protocol.setMsgId((byte)1);
        protocol.setDataFormat((byte)2);
        protocol.setErrorCode((byte)3);
        String content = "hello, testprotocol";
        protocol.setDataArray(content.getBytes());

        byte[] sourceBytes = protocol.getBytes();

        Log.log(TAG, LogLevel.INFO, "source content:" + protocol.toString());

        BasicProtocol trgProtocol = UtilProtocol.readFromBytes(sourceBytes);
        Log.log(TAG, LogLevel.INFO, "source content:" + trgProtocol.toString());

        String trgContent = new String(trgProtocol.getDataArray(), 0, trgProtocol.getDataArray().length);


        assertEquals(trgContent, content);

    }
}