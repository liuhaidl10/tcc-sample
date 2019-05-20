package com.cn.sample.account.service.config;

import org.apache.dubbo.remoting.TimeoutException;
import org.mengyun.tcctransaction.repository.ZooKeeperTransactionRepository;
import org.mengyun.tcctransaction.spring.recover.DefaultRecoverConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>Title:</p>
 * <p>Description:</p>
 *
 * @author Chen Nan
 * @date 2019/5/19.
 */
@Configuration
public class TccConfig {

    /**
     * 设置TransactionRepository
     * <p>
     * 需要为参与事务的应用项目配置一个TransactionRepository，
     * tcc-transaction框架使用transactionRepository持久化事务日志。
     * 可以选择:
     * FileSystemTransactionRepository、
     * SpringJdbcTransactionRepository、
     * RedisTransactionRepository、
     * ZooKeeperTransactionRepository。
     */
    @Bean
    public ZooKeeperTransactionRepository transactionRepository() {
        ZooKeeperTransactionRepository repository = new ZooKeeperTransactionRepository();
        repository.setZkServers("localhost:2181");
        repository.setZkTimeout(10000);
        repository.setZkRootPath("/tcc_account");
        return repository;
    }

    /**
     * 设置恢复策略(可选）
     * 当Tcc事务异常后，恢复Job将会定期恢复事务。
     */
    @Bean
    public DefaultRecoverConfig defaultRecoverConfig() {
        Set<Class<? extends Exception>> exceptionSet = new HashSet<>();
        exceptionSet.add(TimeoutException.class);

        DefaultRecoverConfig recoverConfig = new DefaultRecoverConfig();
        recoverConfig.setMaxRetryCount(30);
        recoverConfig.setRecoverDuration(120);
        recoverConfig.setCronExpression("0 */1 * * * ?");
        recoverConfig.setDelayCancelExceptions(exceptionSet);
        return recoverConfig;
    }
}