package com.lin;

import com.linzx.MainApplication;
import com.linzx.framework.core.util.seq.SequenceIdFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class SequenceIdTest {

    @Autowired
    private SequenceIdFactory sequenceIdFactory;

    @Test
    public void redisIdWorker() {
        try {
            Long idRedis = sequenceIdFactory.getRedisNextSeqValue("test");
            System.out.println("redisId:" + idRedis);
            Long idDb1 = sequenceIdFactory.getDbNextSeqValue("test1");
            System.out.println("dbId1:" + idDb1);
            Long idDb2 = sequenceIdFactory.getDbNextSeqValue("test1");
            System.out.println("dbId2:" + idDb2);
            Long idDb3 = sequenceIdFactory.getDbNextSeqValue("test1");
            System.out.println("dbId3:" + idDb3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
