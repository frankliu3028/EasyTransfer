package sd;

import protocol.*;
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
    public static boolean isRunning = true;

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
            while(isRunning){
                socket.receive(recPacket);
                handlePacketReceived(recPacket);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private void handlePacketReceived(DatagramPacket recPacket){
        BasicProtocol basicProtocol = UtilProtocol.readFromBytes(recPacket.getData());
        if(basicProtocol == null){
            Log.log(TAG, LogLevel.INFO, "parse failure, discard packet");
            return;
        }
        if(basicProtocol.getMsgId() != MsgId.SERVICE_DISCOVER_REQUEST){
            Log.log(TAG, LogLevel.WARNING, "unknown msgid");
            return;
        }

        BasicProtocol retProtocol = new BasicProtocol();
        retProtocol.setErrorCode(ErrorCode.SUCCESS);
        retProtocol.setMsgId(MsgId.SERVICE_DISCOVER_RESPONSE);
        retProtocol.setDataFormat(DataFormat.CUSTOM);
        retProtocol.setDataArray(Util.int2ByteArrays(Config.FILE_TRANSFER_SERVICE_LISTEN_PORT));
        byte[] retBuffer = retProtocol.getBytes();
        DatagramPacket retPacket = new DatagramPacket(retBuffer, retBuffer.length, recPacket.getSocketAddress());
        try{
            socket.send(retPacket);
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
