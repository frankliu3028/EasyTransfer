import entity.DeviceInfo;
import entity.TaskListItem;
import network.Client;
import network.ClientCallback;
import network.Server;
import network.ServerCallback;
import sd.SDServer;
import sd.SDServerCallback;
import ui.MainWindow;
import ui.NetWorkCallback;
import ui.WindowClosedListener;
import utils.Config;
import utils.Log;
import utils.LogLevel;

import java.io.File;

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

                    @Override
                    public void sendFinish(TaskListItem item) {
                        mainWindow.removeTask(item);
                    }
                });
                client.start();

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };

    private static ServerCallback serverCallback = new ServerCallback() {
        @Override
        public void receiveFile(TaskListItem item) {
            mainWindow.addTask(item);
        }

        @Override
        public void updateProgress(TaskListItem item) {
            mainWindow.updateProgress(item);
        }

        @Override
        public void receiveFinish(TaskListItem item) {
            mainWindow.removeTask(item);
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
        server = new Server(Config.FILE_TRANSFER_SERVICE_LISTEN_PORT, serverCallback);
        new Thread(new Runnable() {
            @Override
            public void run() {
                server.start();
            }
        }).start();

        mainWindow = new MainWindow(netWorkCallback);
        mainWindow.setVisible(true);

        mainWindow.setWindowClosedListener(new WindowClosedListener() {
            @Override
            public void windowClosed() {
                server.close();
            }
        });
    }

}
