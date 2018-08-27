package com.wcx;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author: chuck
 * @Date: 2018/8/23
 * @Time: 15:11
 * @Description: Zookeeper 客户端常用操作
 * @modified:
 */
public class ZooKeeperDemo {

    private static final String CONNECTION_STRING = "127.0.0.1:2181";
    private static final int SESSION_TIMEOUT = 5000;

    private static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        // 连接 ZooKeeper
        ZooKeeper zk = new ZooKeeper(CONNECTION_STRING, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }
            }
        });
        latch.await();

        // 以同步的方式获取节点信息
        List<String> children = zk.getChildren("/", null);
        for (String node : children) {
            System.out.println(node);
        }

        // 异步方式获取节点信息，回调的方式
        zk.getChildren("/", null, new AsyncCallback.ChildrenCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, List<String> children) {
                for (String node : children) {
                    System.out.println(node);
                }
            }
        }, null);

        // 同步方式 判断节点是否存在
        Stat stat = zk.exists("/", null);
        if (stat != null) {
            System.out.println("node exists");
        } else {
            System.out.println("node does not exist");
        }

        // 异步方式 判断节点是否存在
        zk.exists("/", null, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int rc, String s, Object cxt, Stat stat) {
                if (stat != null) {
                    System.out.println("node exists");
                } else {
                    System.out.println("node does not exists");
                }
            }
        }, null);

        // 获取 Zookeeper 客户端对象
        System.out.println(zk);
    }

}
