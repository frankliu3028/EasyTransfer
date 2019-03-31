package protocol;

import utils.Util;

public class ProtocolFactory {

    public static BasicProtocol createServiceDiscoverRequest(){
        BasicProtocol res = new BasicProtocol();
        res.setMsgId(MsgId.SERVICE_DISCOVER_REQUEST);
        return res;
    }

    public static BasicProtocol createFileSendResponse(byte errorCode, int port){
        BasicProtocol res = new BasicProtocol();
        res.setMsgId(MsgId.FILE_SEND_RESPONSE);
        res.setErrorCode(errorCode);
        res.setDataArray(Util.int2ByteArrays(port));
        return res;
    }
}
