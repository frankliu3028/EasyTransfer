package protocol;

import org.junit.Test;
import utils.Log;
import utils.LogLevel;

import static org.junit.Assert.*;

public class FileSendRequestTest {

    private final String TAG = FileSendRequestTest.class.getSimpleName();
    @Test
    public void parse() {
        FileSendRequest f = new FileSendRequest("filename.txt", 18453);
        Log.log(TAG, LogLevel.INFO, "msg to str[before]:" + f.toString());
        byte[] raw = f.getMsgBytes();
        FileSendRequest fParse = new FileSendRequest();
        fParse.parse(raw);
        Log.log(TAG, LogLevel.INFO, "msg to str[after]:" + fParse.toString());
    }
}