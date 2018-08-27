package com.wcx.config;

import com.wcx.registry.ServiceRegistry;
import com.wcx.registry.impl.ServiceRegistryImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: chuck
 * @Date: 2018/8/24
 * @Time: 13:36
 * @Description: 服务注册配置类
 * @modified:
 */
@Configuration
@ConfigurationProperties(prefix = "registry")
public class RegistryConfig {

    private String servers;

    @Bean
    public ServiceRegistry serviceRegistry() {
        return new ServiceRegistryImpl(servers);
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

}
