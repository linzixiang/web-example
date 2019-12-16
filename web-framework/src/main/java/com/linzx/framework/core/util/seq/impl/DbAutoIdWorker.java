package com.linzx.framework.core.util.seq.impl;

import com.linzx.framework.core.util.seq.SequenceIdWorker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库自增id
 */
public class DbAutoIdWorker implements SequenceIdWorker<Long> {

    /** 数据库连接池 **/
    private Connection conn;

    /** 序列号唯一key，用于生成相应的表结构 **/
    private String sequenceName;

    /** 开始值 **/
    private int start = 1;

    public DbAutoIdWorker(Connection connection, String sequenceName) {
        this(connection, sequenceName, 1);
    }

    public DbAutoIdWorker(Connection connection, String sequenceName, int start) {
        this.conn = connection;
        this.sequenceName = sequenceName;
        this.start = start;
    }

    @Override
    public synchronized Long getNextSeqValue() throws SQLException {
        Statement stat = null;
        Long seqId = null;
        try{
            stat = conn.createStatement();
            seqId = getLastId(stat, sequenceName);
        }catch(Exception e){
            //如果表不存在，创建表后重新获取序列
            if(e.getMessage() != null && e.getMessage().indexOf("doesn't exist") > 0){
                createSequenceTable(stat, sequenceName);
                seqId = getLastId(stat, sequenceName);
            }else{
                throw e;
            }
        }finally{
            if (stat != null)
                stat.close();
        }
        return seqId;
    }

    /**
     * 如果表不存在，自动创建表
     */
    private void createSequenceTable(Statement stat, String sequenceName) throws SQLException{
        String sql = "CREATE TABLE seq_" + sequenceName + "(id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,"
                + "stub CHAR(1) NOT NULL DEFAULT '',"
                + "PRIMARY KEY (id),UNIQUE KEY stub (stub))ENGINE=MYISAM DEFAULT CHARSET=gbk";
        stat.execute(sql);
        if (this.start > 1) {
            stat.execute("alter table seq_" + sequenceName + " auto_increment = " + this.start);
        }
    }

    /**
     * 获取生成的id
     */
    private long getLastId(Statement stat, String sequenceName) throws SQLException {
        stat.execute("REPLACE INTO seq_" + sequenceName + " (stub) VALUES ('a'); ");
        ResultSet set=stat.executeQuery("SELECT LAST_INSERT_ID();");
        if(set.next()){
            long seqid = set.getLong(1);
            return seqid;
        }else{
            throw new RuntimeException( sequenceName + " error!");
        }
    }

}
