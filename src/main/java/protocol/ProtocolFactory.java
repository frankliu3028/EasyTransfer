package protocol;

public class ProtocolFactory {

    public static BasicProtocol createServiceDiscoverRequest(){
        BasicProtocol res = new BasicProtocol();
        res.setMsgId(MsgId.SERVICE_DISCOVER_REQUEST);
        return res;
    }
}
