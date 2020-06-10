package study.dubbo.customer.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import study.dubbo.provider.code.MyMessageDecoder;
import study.dubbo.provider.code.MyMessageEncoder;
import study.dubbo.provider.famwork.Invocation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient {
    private static NettyClientHanlder client;
    private static ExecutorService executorService= Executors.newCachedThreadPool();
    //private static
    //初始化客户端
    public static void init(String ip, int port) {
        client=new NettyClientHanlder();
        //创建EventLoopGroup
        NioEventLoopGroup group = new NioEventLoopGroup();
        try{
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
               // .option(ChannelOption.TCP_NODELAY, true)
                .handler(
                        new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
//                                pipeline.addLast(new StringDecoder());
//                                pipeline.addLast(new StringEncoder());
                                pipeline.addLast(new MyMessageEncoder()); //加入编码器
                                pipeline.addLast(new MyMessageDecoder()); //加入解码器
                                pipeline.addLast(client);
                            }
                        }
                );
            ChannelFuture sync = bootstrap.connect(ip, port).sync();
            //给关闭通道进行监听
            //sync.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //客戶端不要关闭group，如果关闭了则接收不到服务端的信息
            //group.shutdownGracefully();
        }
    }
    public Object start(Invocation invocation){
        Object response=null;
        try {
            if(client==null){
                init("127.0.0.1", 8080);
            }
            //设置调用参数
            client.setInvocation(invocation);
            response=  executorService.submit(client).get();
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}