package org.example.common.config.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @Value("${redis.address}")
    private String redisAddress;

    @Value("${redis.master}")
    private String redisMaster;

    @Value("${redis.password}")
    private String password;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSentinelServers()
                .setMasterName(redisMaster)
                .addSentinelAddress(redisAddress.split(","))
                .setPassword(password);

        return Redisson.create(config);
    }
}
