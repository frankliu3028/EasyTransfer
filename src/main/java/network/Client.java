package network;

import entity.DeviceInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import protocol.BasicProtocol;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

public class Client {

    private DeviceInfo deviceInfo;
    private File file;
    private Channel channel;
    private ClientCallback callback;

    public Client(DeviceInfo deviceInfo, File file, ClientCallback callback){
        this.deviceInfo = deviceInfo;
        this.file = file;
        this.callback = callback;
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
                            ch.pipeline().addLast(new ProtocolEncoder(), new ClientHandler(deviceInfo, file, callback));
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

    public void close(){
        channel.close();
    }
}