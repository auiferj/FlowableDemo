package org.flowable;

import org.flowable.engine.impl.db.DbSchemaCreate;

public class CreateTableMain {
    public static void main(String[] args) {
        //创建数据库表，这个版本的flowable可建34张表
        DbSchemaCreate.main(args);
    }
}
