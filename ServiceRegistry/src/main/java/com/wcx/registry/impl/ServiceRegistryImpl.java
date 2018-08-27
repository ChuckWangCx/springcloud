package com.wcx.registry.impl;

import com.wcx.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * @author: chuck
 * @Date: 2018/8/24
 * @Time: 10:25
 * @Description: 服务注册表实现类
 * @modified:
 */
@Slf4j
@Component
public class ServiceRegistryImpl implements ServiceRegistry, Watcher {

    private static final String REGISTRY_PATH = "/registry";
//    private static final String CONNECTION_STRING = "127.0.0.1:2181";
    private static final int SESSION_TIMEOUT = 5000;

    private static CountDownLatch latch = new CountDownLatch(1);

    private ZooKeeper zk;

    public ServiceRegistryImpl() {}

    public ServiceRegistryImpl(String zkServers) {
        try {
            // 创建 ZooKeeper 客户端
            zk = new ZooKeeper(zkServers, SESSION_TIMEOUT, this);
            latch.await();
            log.debug("connected to zookeeper");
        } catch (Exception e) {
            log.error("create zookeeper client failure", e);
        }
    }

    @Override
    public void register(String serviceName, String serviceAddress) {
        try {
            // 创建根节点 （持久节点）
            String registryPath = REGISTRY_PATH;
            if (zk.exists(registryPath, false) == null) {
                zk.create(registryPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
                log.debug("create registry node: {}", registryPath);
            }
            // 创建服务节点 （持久节点）
            String servicePath = registryPath + "/" + serviceName;
            if (zk.exists(servicePath, false) == null) {
                zk.create(registryPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
                log.debug("create service node: {}", servicePath);
            }
            // 创建地址节点 （临时顺序节点）
            String addressPath = servicePath + "/address-";
            String addressNode = zk.create(addressPath, serviceAddress.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            log.debug("create address node: {} => {}", addressNode, serviceAddress);
        } catch (Exception e) {
            log.error("create node failure", e);
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            latch.countDown();
        }
    }
}
