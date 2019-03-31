package ui;

import entity.DeviceInfo;

import java.io.File;

public interface NetWorkCallback {
    void sendFileRequest(DeviceInfo deviceInfo, File file);
}
