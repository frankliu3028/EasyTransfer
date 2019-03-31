package test;

import network.FileSender;
import network.FileSenderCallback;
import utils.Log;
import utils.LogLevel;
import utils.Util;

import java.io.File;
import java.net.InetAddress;

public class TestSender {

    private static final String TAG = TestSender.class.getSimpleName();

    public static void main(String[] args) throws Exception{
        File file = Util.selectFile("choose a file");
        int port = 10001;
        new Thread(new FileSender(InetAddress.getByName("localhost"), port, file, new FileSenderCallback() {
            @Override
            public void currentProgress(int progress) {
                Log.log(TAG, LogLevel.INFO, "currentProgress:" + progress);
            }
        })).start();
    }
}
