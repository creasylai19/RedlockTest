package com.creasy.redis;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author laicreasy
 */
public class MainLockTest {

    private static final Logger log = LoggerFactory.getLogger(MainLockTest.class);

    private static final long TASK_CONSUME_TIME = 2;
    private static final long LEASE_TIME = 10;

    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
//        config.useClusterServers()
//                // use "rediss://" for SSL connection
//                .addNodeAddress("redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);
        RLock lock = redisson.getLock("myLock");
        // traditional lock method
//        lock.lock();

        // or acquire lock and automatically unlock it after 10 seconds
//        lock.lock(10, TimeUnit.SECONDS);

        // or wait for lock aquisition up to 10 seconds
        // and automatically unlock it after 10 seconds
        while (true){
            long startTime = System.currentTimeMillis();
            boolean res = lock.tryLock(new Random().nextInt(10), LEASE_TIME, TimeUnit.SECONDS);
            if (res) {
                try {
                    log.info("Get lock");
                    long now = System.currentTimeMillis();
                    while (LEASE_TIME - (now - startTime)/1000 > TASK_CONSUME_TIME) {
                        log.info("PID:{}, Thread info:{}", ManagementFactory.getRuntimeMXBean().getPid(), Thread.currentThread().toString());
                        TimeUnit.SECONDS.sleep(1);
                        now = System.currentTimeMillis();
                    }
                } finally {
                    log.info("Release lock");
                    lock.unlock();
                }
            }
            TimeUnit.SECONDS.sleep(new Random().nextInt(10));
        }
    }

}
