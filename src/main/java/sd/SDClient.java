package sd;

import entity.DeviceInfo;
import protocol.BasicProtocol;
import protocol.ProtocolFactory;
import protocol.UtilProtocol;
import utils.Config;
import utils.Log;
import utils.LogLevel;
import utils.Util;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class SDClient extends Thread{
    private final String TAG = SDClient.class.getSimpleName();
    private SDClientCallback callback;
    private MulticastSocket socket;

    private boolean isRunning = true;
    private boolean receiving = true;
    private int receivingTime = 2*1000;

    public SDClient(SDClientCallback callback){
        this.callback = callback;
    }

    @Override
    public void run() {
        try{
            socket = new MulticastSocket();
            InetAddress groupAddr = InetAddress.getByName(Config.multicastAddress);
            socket.joinGroup(groupAddr);
            socket.setLoopbackMode(true);
            BasicProtocol basicProtocol = ProtocolFactory.createServiceDiscoverRequest();
            DatagramPacket packetReq = new DatagramPacket(basicProtocol.getBytes(), basicProtocol.getBytes().length, groupAddr, Config.SERVICE_DISCOVER_LISTEN_PORT);
            socket.send(packetReq);

            startTimer();

            byte[] buffer=new byte[1024];
            DatagramPacket recPacket=new DatagramPacket(buffer,buffer.length);
            socket.setSoTimeout(1000);
            List<DeviceInfo> resList = new ArrayList<>();
            while(isRunning) {
                if(!receiving)break;
                try {
                    socket.receive(recPacket);
                }catch(SocketTimeoutException e) {
                    Log.log(TAG, LogLevel.INFO, "未找到");
                    continue;
                }

                BasicProtocol recProtocol = UtilProtocol.readFromBytes(recPacket.getData());
                Log.log(TAG, LogLevel.BRIEF, "rec:" + recProtocol);
                byte[] recData = recProtocol.getDataArray();
                int port= Util.bytes2Int(recData, 0);
                byte[] hostnameBytes = Arrays.copyOfRange(recData, 4, recProtocol.getLength() - 7);
                String hostname = new String(hostnameBytes, 0, hostnameBytes.length);
                resList.add(new DeviceInfo(recPacket.getAddress().getHostAddress(), port, hostname));
            }

            if(callback != null) {
                callback.discoverDevices(resList);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            socket.close();
        }


    }

    private void startTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                //Log.log(TAG, LogLevel.INFO, "set receiving false");
                receiving = false;
                //need stop timer manual
                timer.cancel();
            }

        }, receivingTime);
    }
}
