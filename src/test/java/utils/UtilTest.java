package utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void isLoclePortUsing() {
        boolean res = Util.isLoclePortUsing(8080);
        assertFalse(res);
    }
}