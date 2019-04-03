package network;

import entity.DeviceInfo;
import entity.TaskIdPool;
import entity.TaskListItem;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import protocol.BasicProtocol;
import protocol.ErrorCode;
import protocol.MsgId;
import protocol.ProtocolFactory;
import utils.Log;
import utils.LogLevel;
import utils.Util;

import java.io.File;
import java.net.InetAddress;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final String TAG = ClientHandler.class.getSimpleName();

    private ClientCallback callback;
    private DeviceInfo deviceInfo;
    private File file;

    private TaskListItem item;

    public ClientHandler(DeviceInfo deviceInfo, File file, ClientCallback callback){
        this.deviceInfo = deviceInfo;
        this.file = file;
        this.callback = callback;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        BasicProtocol basicProtocol = ProtocolFactory.createFileSendRequest(file);
        ctx.writeAndFlush(basicProtocol);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        BasicProtocol basicProtocol = (BasicProtocol)msg;
        switch (basicProtocol.getMsgId()){
            case MsgId.FILE_SEND_RESPONSE:
                if(basicProtocol.getErrorCode() == ErrorCode.SUCCESS){
                    int port = Util.byteArrayToInt(basicProtocol.getDataArray());
                    int taskId = TaskIdPool.getInstance().allocate();
                    if(taskId == -1){
                        Log.log(TAG, LogLevel.ERROR, "no valid task id");
                        ctx.close();
                        return;
                    }
                    TaskListItem item = new TaskListItem();
                    item.setId(taskId);
                    item.setPath(file.getAbsolutePath());
                    item.setType(TaskListItem.TYPE_SEND);
                    item.setPeerIp(deviceInfo.getIp());
                    item.setProgress(0);
                    callback.startSendFile(item);
                    this.item = item;
                    FileSender fileSender = new FileSender(InetAddress.getByName(deviceInfo.getIp()), port, file, new FileSenderCallback(){
                        @Override
                        public void currentProgress(int progress) {
                            item.setProgress(progress);
                            callback.currentProgress(item);
                        }

                        @Override
                        public void finish() {
                            callback.sendFinish(ClientHandler.this.item);
                        }
                    });
                    new Thread(fileSender).start();
                }
                break;
                default:
                    break;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
