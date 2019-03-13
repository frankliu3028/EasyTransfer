package test;

import entity.DeviceInfo;
import sd.SDClient;
import sd.SDClientCallback;
import utils.Log;
import utils.LogLevel;

import java.util.List;

public class SDClientTest {

    private static final String TAG = SDClientTest.class.getSimpleName();

    public static void main(String[] args){
        SDClient sdClient = new SDClient(new SDClientCallback() {
            @Override
            public void discoverDevices(List<DeviceInfo> deviceInfoList) {
                if(deviceInfoList.size() == 0){
                    Log.log(TAG, LogLevel.INFO, "未发现任何设备");
                }
                for(DeviceInfo d:deviceInfoList){
                    Log.log(TAG, LogLevel.INFO, "device info:" + d);
                }

            }
        });
        sdClient.start();
    }
}
