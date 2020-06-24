package study.dubbo.spi.exportservice;

public class StartNettyServer {
    public static void main(String[] args) {
        //new ExchangeHandlerAdapter()  爷爷
        //new HeaderExchangeHandler(handler)  爸爸包装爷爷
        // new DecodeHandler(handler)  孙子包装爸爸

        //new NettyHandler(getUrl(), new NettyClient())
        //NettyClient对象有上面的DecodeHandler对象

    }
}
