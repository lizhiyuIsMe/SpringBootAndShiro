package study.dubbo.customer.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import study.dubbo.provider.code.MessageProtocol;
import study.dubbo.provider.famwork.Invocation;
import study.dubbo.provider.tools.ParseByteArray;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;

public class NettyClientHanlder extends SimpleChannelInboundHandler<MessageProtocol> implements Callable {
    private ChannelHandlerContext context;//上下文
    private String result; //返回的结果
    private Invocation invocation; //客户端调用方法时，传入的参数

    private ConcurrentHashMap<String, SynchronousQueue<Object>> queueMap = new ConcurrentHashMap<>();

    //调用方法前进行赋值
    public void setInvocation(Invocation invocation) {
        System.out.println(" 2.将要调用的 invocation 参数赋值 ");
        this.invocation=invocation;
    }

    //与服务器的连接创建后，就会被调用, 这个方法是第一个被调用(1)
    @Override
    public void channelActive(ChannelHandlerContext ctx)  {
        System.out.println(" 1.和服务端建立连接 初始化ChannelHandlerContext对象 ");
        context = ctx; //因为我们在其它方法会使用到 ctx
        //SocketAddress socketAddress = ctx.channel().remoteAddress();
        // context.writeAndFlush("323");
    }

    //(3)调用布标对象
    //注意这里有一把锁  目的是为了干什么呢
    @Override
    public synchronized Object call() throws Exception {
        //将对象转换成字节数组  这个对象要允许被序列化
        byte[] bytes = ParseByteArray.parseObjectToByteArray(invocation);
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(bytes.length);
        messageProtocol.setContent(bytes);

        if(this.context==null){
            System.out.println("call 时候context为空报错");
        }
        System.out.println(" 3 进行调用服务端,调用后阻塞 ");
        context.writeAndFlush(messageProtocol);
        //进行wait
        wait(); //等待channelRead 方法获取到服务器的结果后，
        System.out.println("接收到服务端的信息");
        return result; //服务方返回的结果
    }



    //收到服务器的数据后，调用方法 (4)
    ////注意这里有一把锁 目的是为了干什么呢
    @Override
    protected synchronized void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {
        System.out.println(" 4 服务端调用了客户端  ");
        if(messageProtocol.getLen() != messageProtocol.getContent().length){
            System.out.println("服务端发送信息错误");
        }
        String str =new String(messageProtocol.getContent());
        //String str = (String)ParseByteArray.parseByteArrayToObject(messageProtocol.getContent());
        //如果返回的是字符串使用下面方式进行转换会报错
        //java.io.StreamCorruptedException: invalid stream header: E69C8DE5
        result = str;
        //接收到的数据
        System.out.println("服务端返回的参数是:"+result);
        //唤醒一个随机线程,这里要是想要唤醒相对应的线程要可以用队列
        notify(); //唤醒等待的线程
    }



}
