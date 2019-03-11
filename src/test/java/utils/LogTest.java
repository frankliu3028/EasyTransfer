package utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class LogTest {

    public static final String TAG = LogTest.class.getSimpleName();

    @Test
    public void log() {
        Log.log(TAG, LogLevel.INFO, "logTest");
    }
}