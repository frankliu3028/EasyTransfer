package sd;

import entity.DeviceInfo;

import java.util.List;

public interface SDClientCallback {
    void discoverDevices(List<DeviceInfo> deviceInfoList);
}
