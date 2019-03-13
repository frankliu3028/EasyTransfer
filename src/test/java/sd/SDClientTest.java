package sd;

import entity.DeviceInfo;
import org.junit.Test;
import utils.Log;
import utils.LogLevel;

import java.util.List;

import static org.junit.Assert.*;

public class SDClientTest {

    private final String TAG = SDClient.class.getSimpleName();
    @Test
    public void run() {
        SDClient sdClient = new SDClient(new SDClientCallback() {
            @Override
            public void discoverDevices(List<DeviceInfo> deviceInfoList) {
                for(DeviceInfo d:deviceInfoList){
                    Log.log(TAG, LogLevel.INFO, "device info:" + d);
                }

            }
        });
        sdClient.start();
    }
}