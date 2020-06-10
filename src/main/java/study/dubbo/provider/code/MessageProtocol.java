package study.dubbo.provider.code;


//协议包
public class MessageProtocol {
    private int len; //关键
    //任何对象都可以转换成字节数组
    private byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
