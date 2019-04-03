package test;

import network.FileReceiver;
import network.FileReceiverCallback;
import utils.Log;
import utils.LogLevel;
import utils.Util;

import java.io.File;

public class TestReceiver {

    private static final String TAG = TestReceiver.class.getSimpleName();
    public static void main(String[] args){
        File file = Util.selectFile("choose a file");
        new Thread(new FileReceiver(file.getName(), file.length(), new FileReceiverCallback() {
            @Override
            public void ready(int port) {
                Log.log(TAG, LogLevel.INFO, "listen on port:" + port);
            }

            @Override
            public void currentProgress(int progress) {
                Log.log(TAG, LogLevel.INFO, "currentProgress:" + progress);
            }

            @Override
            public void finish() {

            }
        })).start();
    }
}
