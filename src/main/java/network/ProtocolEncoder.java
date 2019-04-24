package network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import protocol.BasicProtocol;
import utils.Log;
import utils.LogLevel;

public class ProtocolEncoder extends MessageToByteEncoder<BasicProtocol> {

    private final String TAG = ProtocolEncoder.class.getSimpleName();

    @Override
    protected void encode(ChannelHandlerContext ctx, BasicProtocol msg, ByteBuf out) throws Exception {
        Log.log(TAG, LogLevel.INFO, "send message:" + msg.toString());
        out.writeBytes(msg.getBytes());
    }
}
