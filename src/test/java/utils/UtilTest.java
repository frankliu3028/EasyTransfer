package utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilTest {

    private final String TAG = UtilTest.class.getSimpleName();

    @Test
    public void isLoclePortUsing() {
        boolean res = Util.isLocalPortUsing(8080);
        assertFalse(res);
    }

    @Test
    public void getAValidPort() {
        Log.log(TAG, LogLevel.INFO, "valid port:" + Util.getAValidPort());
    }


    @Test
    public void getLocalHostname() {
        Log.log(TAG, LogLevel.BRIEF, "hostname"+ Util.getLocalHostname());
    }
}