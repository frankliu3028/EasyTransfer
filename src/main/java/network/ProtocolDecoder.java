package network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import protocol.BasicProtocol;
import protocol.UtilProtocol;

import java.util.List;

public class ProtocolDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() > 4){
            int length = in.readInt();
            if(in.readableBytes() < length){
                in.resetReaderIndex();
                return;
            }
            byte[] msgBytes = new byte[length];
            BasicProtocol basicProtocol = UtilProtocol.readFromBytesWithoutLength(msgBytes);
            out.add(basicProtocol);
        }
    }
}