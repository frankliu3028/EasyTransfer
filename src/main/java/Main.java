import entity.DeviceInfo;
import entity.TaskListItem;
import network.Client;
import network.ClientCallback;
import network.Server;
import sd.SDServer;
import sd.SDServerCallback;
import ui.MainWindow;
import ui.NetWorkCallback;
import utils.Config;
import utils.Log;
import utils.LogLevel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final String TAG = Main.class.getSimpleName();

    private static SDServer sdServer;
    private static Server server;

    private static MainWindow mainWindow;

    private static NetWorkCallback netWorkCallback = new NetWorkCallback() {
        @Override
        public void sendFileRequest(DeviceInfo deviceInfo, File file) {
            try {

                Client client = new Client(deviceInfo, file, new ClientCallback() {
                    @Override
                    public void startSendFile(TaskListItem item) {
                        mainWindow.addTask(item);
                    }

                    @Override
                    public void currentProgress(TaskListItem item) {
                        mainWindow.updateProgress(item);
                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };


    public static void main(String[] args){
        Log.log(TAG, LogLevel.INFO,"main start");
        init();
    }

    public static void init(){
        sdServer = new SDServer(new SDServerCallback() {
            @Override
            public void serviceStartResults(int errorCode) {

            }
        });
        sdServer.start();
        server = new Server(Config.FILE_TRANSFER_SERVICE_LISTEN_PORT);
        mainWindow = new MainWindow(netWorkCallback);
        mainWindow.setVisible(true);
    }

}
