package network;

import entity.TaskIdPool;
import entity.TaskListItem;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import protocol.*;
import utils.Config;
import utils.Log;
import utils.LogLevel;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainChildHandler extends ChannelInboundHandlerAdapter {

    private final String TAG = MainChildHandler.class.getSimpleName();

    private ServerCallback callback;
    private Executor executor = Executors.newCachedThreadPool();

    private TaskListItem item;

    public MainChildHandler(ServerCallback callback){
        this.callback = callback;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        BasicProtocol basicProtocol = (BasicProtocol)msg;
        switch (basicProtocol.getMsgId()){
            case MsgId.FILE_SEND_REQUEST:
                FileSendRequest fileSendRequest = Parser.parseFileSendRequest(basicProtocol.getDataArray());
                FileReceiver fileReceiver = new FileReceiver(fileSendRequest.getFileName(), fileSendRequest.getFileLength(),
                        new FileReceiverCallback() {
                            @Override
                            public void ready(int port) {
                                int taskId = TaskIdPool.getInstance().allocate();
                                if(taskId == -1){
                                    BasicProtocol fileSendResponse = ProtocolFactory.createFileSendResponse(ErrorCode.FAILURE, port);
                                    ctx.writeAndFlush(fileSendResponse);
                                    Log.log(TAG, LogLevel.ERROR, "no valid task id");
                                    return;
                                }
                                BasicProtocol fileSendResponse = ProtocolFactory.createFileSendResponse(ErrorCode.SUCCESS, port);
                                ctx.writeAndFlush(fileSendResponse);

                                TaskListItem item = new TaskListItem();
                                item.setId(taskId);
                                item.setType(TaskListItem.TYPE_RECEIVE);
                                String peerIp = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
                                item.setPeerIp(peerIp);
                                item.setPath(Config.fileSaveDir + "/" + fileSendRequest.getFileName());
                                item.setProgress(0);
                                callback.receiveFile(item);
                                MainChildHandler.this.item = item;
                            }

                            @Override
                            public void currentProgress(int progress) {
                                MainChildHandler.this.item.setProgress(progress);
                                callback.updateProgress(MainChildHandler.this.item);
                            }

                            @Override
                            public void finish() {
                                callback.receiveFinish(item);
                            }
                        });
                executor.execute(fileReceiver);

                break;
                default:
                    Log.log(TAG, LogLevel.INFO, "unknow msg");
                    break;
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
