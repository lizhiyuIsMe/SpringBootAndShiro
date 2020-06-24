package study.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class TestCurator {
    private static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("192.168.8.110:2181")
            .sessionTimeoutMs(50000)
            .connectionTimeoutMs(30000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
    public static void main(String[] args) throws Exception {
//        zookeeper中有四种节点
//        1、持久化节点(客户端断开连接后该节点依然存在)
//        2、持久化有序节点(客户端断开连接后该节点依然存在，只是zookeeper会对该节点名称进行有序编号)
//        3、临时节点(客户端断开连接后该节点不存在)
//        4、临时有序节点（客户端断开连接后该节点不存在，只是zookeeper会对该节点名称进行有序编号）
        TestCurator testCurator = new TestCurator();
        testCurator.createNode();
        Thread.sleep(5000);
    }
    //
    public void createNode() throws Exception{
        // 创建回话
        client.start();

        //创建临时节点 方法
        //client.create().forPath("/China");
        //client.create().forPath("/America", "zhangsan".getBytes());

        //创建持久节点
        client.create().withMode(CreateMode.EPHEMERAL).forPath("/temporaryNode");

        //创建一个初始内容为空的持久节点
        client.create().withMode(CreateMode.EPHEMERAL).forPath("/persistentNode");

        //递归创建,/Russia是持久节点
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/Russia/car", "haha".getBytes());
    }
    //获取节点内容
    public void getNodeValue() throws Exception{
        client.start();
        byte[] data = client.getData().forPath("/America");
        System.out.println(new String(data));
        //传入一个旧的stat变量,来存储服务端返回的最新的节点状态信息
        byte[] data2 = client.getData().storingStatIn(new Stat()).forPath("/America");
        System.out.println(new String(data2));
    }

    //更新节点
    public void updateNode() throws Exception{
        client.start();
        client.setData().forPath("/America");
        client.setData().withVersion(4).forPath("/America", "lisi".getBytes());
    }

    //删除节点
    public void deleteNode() throws Exception {
        client.start();
        //只能删除叶子节点
        client.delete().forPath("/China");
        //删除一个节点,并递归删除其所有子节点
        client.delete().deletingChildrenIfNeeded().forPath("/Russia");
        //强制指定版本进行删除
        client.delete().withVersion(5).forPath("/America");
        //注意:由于一些网络原因,上述的删除操作有可能失败,使用guaranteed(),如果删除失败,会记录下来,只要会话有效,就会不断的重试,直到删除成功为止
        client.delete().guaranteed().forPath("/America");
    }

    //curator事件监听 监听本节点本节点变化
    public void watch() throws Exception {
        client.start();
        client.create().creatingParentsIfNeeded().forPath("/book/computer", "java".getBytes());

        // 添加 NodeCache
        NodeCache nodeCache = new NodeCache(client, "/book/computer");
        // 添加 NodeCache listener
        nodeCache.getListenable().addListener(() -> System.out.println("新的节点数据:" + new String(nodeCache.getCurrentData().getData())));
        nodeCache.start(true);

        client.setData().forPath("/book/computer", "c++".getBytes());
    }

    //监听子节点变化
    public void watchChildren() throws Exception{
        client.start();

        // 添加 pathChildrenCache
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/book13", true);
        // 添加 pathChildrenCache listener
        pathChildrenCache.getListenable().addListener((client, event) ->  {
            switch (event.getType()) {
                case CHILD_ADDED:
                    System.out.println("新增子节点:" + event.getData().getPath());
                    break;
                case CHILD_UPDATED:
                    System.out.println("子节点数据变化:" + event.getData().getPath());
                    break;
                case CHILD_REMOVED:
                    System.out.println("删除子节点:" + event.getData().getPath());
                    break;
                default:
                    break;
            }
        });
        pathChildrenCache.start();

        client.create().forPath("/book13");
        client.create().forPath("/book13/car", "bmw".getBytes());
    }
}
