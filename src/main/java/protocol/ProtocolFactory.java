package protocol;

import utils.Util;

import java.io.File;

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

    public static BasicProtocol createFileSendRequest(File file){
        FileSendRequest fileSendRequest = new FileSendRequest();
        fileSendRequest.setFileName(file.getName());
        fileSendRequest.setFileLength(file.length());
        BasicProtocol basicProtocol = new BasicProtocol();
        basicProtocol.setMsgId(MsgId.FILE_SEND_REQUEST);
        basicProtocol.setDataArray(fileSendRequest.getMsgBytes());
        return basicProtocol;
    }
}
