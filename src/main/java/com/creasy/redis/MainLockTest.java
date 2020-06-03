package com.creasy.redis;

import com.creasy.dao.ILoanDao;
import com.creasy.pojo.Loan;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author laicreasy
 */
public class MainLockTest {

    private static final Logger log = LoggerFactory.getLogger(MainLockTest.class);

    private static final long LEASE_TIME = 10;

    public static void main(String[] args) throws InterruptedException, IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);
        RLock lock = redisson.getLock("myLock");

        while (true){
            boolean res = lock.tryLock(new Random().nextInt(10), LEASE_TIME, TimeUnit.SECONDS);
            if (res) {
                //模拟获得锁之后处理业务数据
                try {
                    log.info("Get lock");
                    SqlSession session = sqlSessionFactory.openSession();
                    ILoanDao mapper = session.getMapper(ILoanDao.class);
                    Loan loan = mapper.queryLoanWithoutDeal();
                    if( loan == null ) {
                        break;
                    }
                    loan.setDeal_instance("PID:" + ManagementFactory.getRuntimeMXBean().getPid());
                    mapper.updateLoan(loan);
                    session.commit();
                    session.close();
                } finally {
                    log.info("Release lock");
                    try{
                        lock.unlock();
                    } catch (Exception e){
                        log.error(e.getMessage(), e);
                    }
                }
            }
//            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(10));
        }
        redisson.shutdown();
    }

}
