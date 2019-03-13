package sd;

import protocol.ErrorCode;
import utils.Config;
import utils.Log;
import utils.LogLevel;
import utils.Util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class SDServer extends Thread{

    private String TAG = SDServer.class.getSimpleName();
    private MulticastSocket socket;
    private SDServerCallback callback;

    public SDServer(SDServerCallback callback){
        if(callback == null){
            Log.log(TAG, LogLevel.ERROR, "SDServerCallback can not be null");
        }
        this.callback = callback;
    }

    @Override
    public void run() {
        if(Util.isLocalPortUsing(Config.SERVICE_DISCOVER_LISTEN_PORT)){
            Log.log(TAG, LogLevel.ERROR, "service_discover_listen_port" + Config.SERVICE_DISCOVER_LISTEN_PORT + "is using");
            callback.serviceStartResults(ErrorCode.SD_START_ERROR_PORT_USED);
            return;
        }
        try{
            socket = new MulticastSocket(Config.SERVICE_DISCOVER_LISTEN_PORT);
            InetAddress multicastInetAddress = InetAddress.getByName(Config.multicastAddress);
            socket.joinGroup(multicastInetAddress);
            byte[] buffer = new byte[1024];
            DatagramPacket recPacket = new DatagramPacket(buffer, buffer.length);
            while(true){
                socket.receive(recPacket);
                handlePacketReceived(recPacket);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private void handlePacketReceived(DatagramPacket recPacket){

    }
}
