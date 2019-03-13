package protocol;

import org.junit.Test;
import utils.Log;
import utils.LogLevel;

import static org.junit.Assert.*;

public class ProtocolFactoryTest {

    public static final String TAG = ProtocolFactoryTest.class.getSimpleName();
    @Test
    public void createServiceDiscoverRequest() {
        BasicProtocol p = ProtocolFactory.createServiceDiscoverRequest();
        Log.log(TAG, LogLevel.INFO, "protocol string:" + p.toString());
        assertEquals(p.getMsgId(), MsgId.SERVICE_DISCOVER_REQUEST);
    }
}