package network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import protocol.BasicProtocol;

import java.io.IOException;
import java.net.InetAddress;

public class Client {

    private final int port;
    private final InetAddress inetAddress;
    private Channel channel;

    public Client(InetAddress inetAddress, int port){
        this.inetAddress = inetAddress;
        this.port = port;
    }

    public void start(){
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(worker)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ProtocolEncoder(), new ClientHandler());
                            ch.pipeline().addLast(new ProtocolDecoder());
                        }
                    });
            ChannelFuture f = b.connect().sync();
            channel = f.channel();
            f.channel().closeFuture().sync();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
        }
    }

    public void sendMessage(BasicProtocol protocol){
        channel.writeAndFlush(protocol);
    }

    public void close(){
        channel.close();
    }
}
