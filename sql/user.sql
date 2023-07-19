-- auto-generated definition
create table user
(
    id           bigint auto_increment comment 'id'
        primary key,
    username     varchar(256)                       null comment '用户昵称',
    userAccount  varchar(256)                       null comment '登录账号',
    avatarUrl    varchar(1024)                      null comment '头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(256)                       not null comment '密码',
    phone        varchar(128)                       null comment '电话',
    email        varchar(512)                       null comment '邮箱',
    userStatus   int      default 0                 not null comment '用户状态：0 -正常',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete     tinyint  default 0                 not null comment '是否删除 已删除：1 未删除：0',
    userRole     int      default 0                 not null comment '普通用户 - 0  管理员 - 1 '
)
    comment '用户';