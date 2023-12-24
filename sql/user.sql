DROP TABLE IF EXISTS `user_account`;
CREATE TABLE `user_account`
(
    `id`          bigint not null comment 'id',
    `mobile`      varchar(20) comment '手机号',
    `email`       varchar(50)  comment '邮箱',
    `username`    varchar(20)  comment '用户名',
    `password`    varchar(50) DEFAULT NULL comment '密码',
    `salt`        varchar(50) DEFAULT NULL comment '盐值',
    `create_time` datetime(3)  comment '新增时间',
    `update_time` datetime(3)  comment '修改时间',
    primary key (`id`),
    unique key `mobile_unique` (`mobile`),
    unique key email_unique (`email`),
    unique key username_unique (`username`),
    index         create_time_index (`create_time`),
    index         update_time_index (`update_time`)
) engine=innodb default charset=utf8mb4 comment='用户';


DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`
(
    `id`          bigint not null comment 'id',
    `user_id`     bigint not null comment '用户id',
    `nick_name`        varchar(100) DEFAULT NULL comment '昵称',
    `avatar`      varchar(255) DEFAULT NULL comment '头像',
    `sign`        varchar(255)  COMMENT '签名',
    `gender`      char(1)   DEFAULT NULL comment '性别|枚举[GenderEnum]',
    `birth`       varchar(20)  DEFAULT NULL comment '生日',
    `create_time` datetime(3)  comment '新增时间',
    `update_time` datetime(3)  comment '修改时间',
    primary key (`id`),
    unique key nick_unique (`nick_name`),
    index         create_time_index (`create_time`),
    index         update_time_index (`update_time`)
) engine=innodb default charset=utf8mb4 COMMENT='用户基本信息';



-- SEATA 使用到的库都得加
-- 注意此处0.3.0+ 增加唯一索引 ux_undo_log
CREATE TABLE `undo_log`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `branch_id`     bigint(20) NOT NULL,
    `xid`           varchar(100) NOT NULL,
    `context`       varchar(128) NOT NULL,
    `rollback_info` longblob     NOT NULL,
    `log_status`    int(11) NOT NULL,
    `log_created`   datetime     NOT NULL,
    `log_modified`  datetime     NOT NULL,
    `ext`           varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
