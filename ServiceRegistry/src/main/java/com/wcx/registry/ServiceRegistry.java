package com.wcx.registry;

/**
 * @author: chuck
 * @Date: 2018/8/24
 * @Time: 10:22
 * @Description: 服务注册表
 * @modified:
 */
public interface ServiceRegistry {

    /**
     * 注册服务信息
     *
     * @param serviceName   服务名称
     * @param serviceAddress    服务地址
     */
    void register(String serviceName, String serviceAddress);
}
