package org.example.potm.svc.storage.db;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StorageDbConfigure {

    private final JdbcTemplate jdbcTemplate;
    public StorageDbConfigure(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.initDatabase();
    }

    /**
     * 初始化记录表
     */
    @PostConstruct
    private void initDatabase() {
        jdbcTemplate.batchUpdate(SQL_CREATE_TABLE_STORAGE_FILE, SQL_CREATE_TABLE_STORAGE_REF);
    }

    private final static String TABLE_STORAGE_FILE = "storage_file";
    private final static String SQL_CREATE_TABLE_STORAGE_FILE = "create table if not exists " + TABLE_STORAGE_FILE +
            "(" +
            "    id                        bigint               not null auto_increment primary key comment '编号'," +
            "    original_file_name        varchar(512)         not null comment '原始文件名'," +
            "    storage_file_name         varchar(512)         not null comment '存储文件名'," +
            "    mime_type                 varchar(128)         null comment 'mime类型'," +
            "    suffix                    varchar(64)          null comment '文件后缀名'," +
            "    hash                      varchar(256)         null comment '文件hash'," +
            "    size                      bigint               null comment '文件大小'," +
            "    path                      varchar(1024)        null comment '存储对象key或者路径'," +
            "    module                    varchar(128)         null comment  '模块标识'," +
            "    store_type                varchar(128)         null null comment '存储平台类型'," +
            "    create_user               bigint               null comment '创建者'," +
            "    create_time               datetime             null comment '创建时间'" +
            ") comment '对象文件存储'";

    private final static String TABLE_STORAGE_REF = "storage_ref";
    private final static String SQL_CREATE_TABLE_STORAGE_REF = "create table if not exists " + TABLE_STORAGE_REF +
            "(" +
            "    id                        bigint               not null auto_increment primary key comment '编号'," +
            "    file_id                   bigint               not null comment '文件编号'," +
            "    ref_id                    varchar(64)          not null comment '关联编号'," +
            "    ref_module                varchar(512)         not null comment '关联文件库'," +
            "    aq                        int                  null comment '附加条件'," +
            "    create_user               bigint               null comment '创建者'," +
            "    create_time               datetime             null comment '创建时间'" +
            ") comment '对象文件存储关联表'";
}
