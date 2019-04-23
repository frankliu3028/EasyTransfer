package network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import protocol.BasicProtocol;
import protocol.UtilProtocol;
import utils.Log;
import utils.LogLevel;

import java.util.List;

public class ProtocolDecoder extends ByteToMessageDecoder {

    private final String TAG = ProtocolDecoder.class.getSimpleName();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //Log.log(TAG, LogLevel.INFO, "readableBytes: " + in.readableBytes());
        if(in.readableBytes() > 4){
            int length = in.readInt();
            //Log.log(TAG, LogLevel.INFO, "length: " + length);
            //Log.log(TAG, LogLevel.INFO, "readableBytes2: " + in.readableBytes());
            if(in.readableBytes() < length - 4){
                in.resetReaderIndex();
                return;
            }
            //Log.log(TAG, LogLevel.INFO, "length2: " + length);
            byte[] msgBytes = new byte[length - 4];
            in.readBytes(msgBytes);
            BasicProtocol basicProtocol = UtilProtocol.readFromBytesWithoutLength(msgBytes);
            out.add(basicProtocol);
        }
    }
}
