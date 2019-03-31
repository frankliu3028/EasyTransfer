package network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import protocol.*;
import utils.Log;
import utils.LogLevel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainChildHandler extends ChannelInboundHandlerAdapter {

    private final String TAG = MainChildHandler.class.getSimpleName();

    private Executor executor = Executors.newCachedThreadPool();

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
                                BasicProtocol fileSendResponse = ProtocolFactory.createFileSendResponse(ErrorCode.SUCCESS, port);
                                ctx.writeAndFlush(fileSendResponse);
                            }

                            @Override
                            public void currentProgress(int progress) {

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
