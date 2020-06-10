package study.dubbo.provider.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import study.dubbo.provider.code.MyMessageDecoder;
import study.dubbo.provider.code.MyMessageEncoder;

//1、一个EventLoopGroup包含一个或者多个EventLoop;
//2、一个EventLoop在它的生命周期内只和一个Thread绑定；
//3、所有有EventLoop处理的I/O事件都将在它专有的Thread上被处理；
//4、一个Channel在它的生命周期内只注册于一个EventLoop;
//5、一个EventLoop可能会被分配给一个货多个Channel；
public class NettyServer {
    public String ip;
    public int port;
    public NettyServer(String ip, int port) {
        this.ip = ip;
        this.port = port;
        start(ip,port);
    }

    private static void start(String ip,int port)  {
        //这个用来处理连接请求
        //创建1个 EventExecutor
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //这个从来执行业务
        //创建系统核数*2个 EventExecutor
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
        //创建服务端启动对象  配置参数
        ServerBootstrap b = new ServerBootstrap();
        //使用链式编程来进行设置
        //如果只配置一个Group,那么则使用一个group处理
        b.group(bossGroup,workGroup)
                //创建ReflectiveChannelFactory对象,对NioServerSocketChannel.class进行包装
                //将ReflectiveChannelFactory对象赋值给channelFactory变量
                //用于指定io类型
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到连接个数
                .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动连接状态
                // 设置线程队列得到连接个数
//                .option(ChannelOption.SO_BACKLOG,1024)
//                .childOption(ChannelOption.SO_KEEPALIVE,true)
//                .childOption(ChannelOption.TCP_NODELAY,true)
                //设置保持活动连接状态
                //.childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                // handler方法 对应 bossGroup处理的Hanlder , childHandler方法 对应workerGroup处理的Hanlder
                //给workGroup设置对应Hanlder
                .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            System.out.println("有一个客户端进行了连接"+pipeline.channel().id());
//                            pipeline.addLast(new StringDecoder());
//                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new MyMessageEncoder()); //加入编码器
                            pipeline.addLast(new MyMessageDecoder()); //加入解码器
                            pipeline.addLast(new NettyServerHandler()); //业务处理器
                        }
                });
            //通过端口和"0.0.0.0"的ip创建InetSocketAddress对象
            //反射创建NioServerSocketChannel.class
               //1.通过启动参数查看SelectorProvider.class 的实现类如果有则创建
               //2.如果启动参数没配置,则通过spi从文件中获得SelectorProvider.class实现类创建
               //3.如果spi没有配置,则自己创建一个WindowsSelectorProvider
               //使用WindowsSelectorProvider创建一个serverSocketChannel
               //将serverSocketChannel对象赋值给 NioServerSocketChannel父类的ch属性中
               //NioServerSocketChannel父类创建一个DefaultChannelPipeline对象,是一个链表 和 Unsafe对象
               //往 pipeline 中添加 handler
               //EvenloopGroup对象中有多个EventExecutor,将NioServerSocketChannel设置给每个EventExecutor
               //channel.eventLoop().execute(new Runnable() {} //将事件放到taskqueue中运行
            ChannelFuture channelFuture = b.bind(ip,port).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
