SET FOREIGN_KEY_CHECKS = 0;

create table IF NOT EXISTS sys_dict
(
    id          bigint auto_increment
        primary key,
    svc_name    varchar(64) default 'default' not null comment '服务名称',
    dict_key    varchar(100)                  not null comment '标识',
    description varchar(100)                  null comment '描述',
    remark      varchar(255)                  null comment '备注',
    system_flag smallint    default 0         not null comment '是否系统内置字典',
    enum_flag   smallint    default 0         not null comment '是否是枚举字典',
    constraint sys_dict_svc_key_uindex
        unique (svc_name, dict_key)
)
    comment '系统字典';

create table IF NOT EXISTS sys_dict_item
(
    id         bigint auto_increment primary key,
    svc_name   varchar(64)  default 'default' not null comment '服务名称',
    dict_key   varchar(100)                   not null comment '字典标识',
    value      varchar(100)                   null comment '值',
    label      varchar(100)                   null comment '标签',
    type       varchar(100)                   null comment '枚举值',
    color      varchar(32)                    null comment '颜色',
    ext       json                            null comment '扩展信息',
    sort_order int          default 0         not null comment '排序（升序）',
    system_flag smallint    default 0         not null comment '是否系统内置字典',
    remark     varchar(255) default ' '       null comment '备注',
    parent_item_value varchar(100)            null comment '上级字典值',
    constraint sys_dict_item_uindex
        unique (svc_name, dict_key, value)
)
    comment '系统字典项';

create table IF NOT EXISTS sys_menu
(
    id                    varchar(32)                 not null
        primary key,
    parent_id             varchar(32) default '0'     not null comment '上级菜单',
    name                  varchar(32)                 not null comment '名称',
    code                  varchar(64)                 null comment '菜单编码',
    permissions           varchar(1024)               null comment '权限',
    button_flag           smallint    default 0       not null comment '按钮权限',
    route_name            varchar(128)                null comment '路径名称',
    route_path            varchar(512)                null comment '路由路径',
    route_meta_icon       varchar(64)                 null comment '路由图标',
    route_meta_keep_alive smallint    default 1       null comment '路由保持',
    route_meta_frame_src  varchar(512)                null comment '路由内嵌页面',
    source_type           varchar(16) default 'admin' not null comment '来源类型(admin后台菜单miniapp小程序菜单)',
    hidden                smallint    default 0       not null comment '是否隐藏',
    sort_order            int         default 0       not null comment '排序',
    show_link             int         default 1       not null comment '是否显示菜单',
    active_path           varchar(256)                null comment '激活路径'
)
    comment '系统菜单';

create table IF NOT EXISTS sys_dept
(
    id        varchar(64)             not null
        primary key,
    dept_name  varchar(32)             not null comment '部门名称',
    remark    varchar(255)            null comment '备注',
    sort_order int                    not null default 0 comment '排序',
    parent_id varchar(64) default '0' not null comment '上级部门编号'
)
    comment '系统部门';

create table IF NOT EXISTS sys_role
(
    id        bigint auto_increment
        primary key,
    role_code varchar(32) not null comment '角色标识',
    role_name varchar(32) not null comment '角色名',
    remark    varchar(255) null comment '备注',
    constraint rolecode_uindex
        unique (role_code)
)
    comment '系统角色';

create table IF NOT EXISTS sys_role_menu
(
    id      bigint auto_increment
        primary key,
    role_id bigint      not null comment '角色编号',
    menu_id varchar(32) not null comment '菜单编号'
)
    comment '系统角色菜单关联';

create table IF NOT EXISTS sys_role_dept
(
    id      bigint auto_increment
        primary key,
    role_id bigint not null comment '角色编号',
    dept_id  varchar(64) not null comment '部门编号'
)
    comment '系统角色部门关联';

create TABLE IF NOT EXISTS sys_user
(
    id            bigint auto_increment
        primary key,
    username      varchar(32)              not null comment '用户名',
    nickname      varchar(32)              not null comment '昵称',
    mobile        varchar(32)              not null comment '手机号',
    password      varchar(32)              not null comment '密码',
    password_salt varchar(32)              not null comment '密码盐值',
    user_scope    smallint     default 1   not null comment '用户归属',
    user_status   smallint     default 1   not null comment '用户状态',
    dept_id       varchar(64)  default '0' not null comment '部门编号',
    avatar        varchar(255)             null comment '头像',
    constraint uindex_mobile
        unique (mobile),
    constraint username_uindex
        unique (username)
)
    comment '系统用户';

create table IF NOT EXISTS sys_user_role
(
    id      bigint auto_increment
        primary key,
    user_id bigint not null comment '用户编号',
    role_id bigint not null comment '角色编号'
)
    comment '系统用户角色关联';

CREATE TABLE IF NOT EXISTS `quartz_job` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务id',
    `bean_name` varchar(200) NOT NULL COMMENT 'SpringBean名称',
    `params` varchar(2000) DEFAULT NULL COMMENT '执行参数',
    `cron_expres` varchar(100) DEFAULT NULL COMMENT 'cron表达式',
    `quartz_job_status` varchar(1) NOT NULL DEFAULT '1' COMMENT '任务状态：1运行，2停止',
    `remark` varchar(100) DEFAULT NULL COMMENT '备注',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='定时任务';

CREATE TABLE IF NOT EXISTS `quartz_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志id',
  `job_id` bigint(20) NOT NULL COMMENT '任务id',
  `bean_name` varchar(200) DEFAULT NULL COMMENT 'SpringBean名称',
  `params` varchar(2000) DEFAULT NULL COMMENT '执行参数',
  `quartz_log_status` varchar(1) NOT NULL DEFAULT '1' COMMENT '任务状态：1成功，2失败',
  `error` varchar(2000) DEFAULT NULL COMMENT '失败信息',
  `times` int(11) NOT NULL COMMENT '耗时(单位：毫秒)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `job_id` (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='定时任务日志';

create table IF NOT EXISTS sys_notify
(
    id                 bigint auto_increment primary key COMMENT '主键',
    content            text                                  null COMMENT '通知内容',
    notify_type        varchar(255)                          null COMMENT '通知类型',
    target_id          varchar(64) default '0'               not null COMMENT '通知目标编号',
    notify_target_type varchar(255)                          null COMMENT '通知目标类型',
    notify_target_action             varchar(255)            null COMMENT '通知触发动作',
    sender_id          varchar(64) default '0'               not null COMMENT '通知发送者编号',
    notify_sender_type varchar(255)                          null COMMENT '通知发送者类型',
    is_read            smallint    default 0                 not null COMMENT '是否已读',
    user_id            bigint                                not null COMMENT '用户编号',
    create_time         datetime    default CURRENT_TIMESTAMP not null COMMENT '创建时间',
    notify_level       varchar(8)    default '0'                 null COMMENT '通知级别',
    key (notify_type),
    key (user_id)
)
comment '系统通知';

INSERT INTO sys_user (id, username, nickname, mobile, password, password_salt, user_scope, user_status, dept_id) VALUES (1, 'admin', '管理员', '15300000000', '3baabd68b1c33897f1afd011391aab61', '508768e2-309f-4370-bef7-b90a082e', 1, 1, "0");

SET FOREIGN_KEY_CHECKS = 1;
