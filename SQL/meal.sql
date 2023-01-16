CREATE DATABASE MEAL DEFAULT CHARACTER SET UTF8MB4 COLLATE UTF8MB4_UNICODE_CI;

DROP TABLE IF EXISTS `meal_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meal_user` (
                                 `id` int(11) NOT NULL AUTO_INCREMENT,
                                 `username` varchar(63) NOT NULL COMMENT '用户名称',
                                 `password` varchar(63) NOT NULL DEFAULT '' COMMENT '用户密码',
                                 `gender` tinyint(3) NOT NULL DEFAULT '0' COMMENT '性别：0 未知， 1男， 1 女',
                                 `birthday` date DEFAULT NULL COMMENT '生日',
                                 `last_login_time` datetime DEFAULT NULL COMMENT '最近一次登录时间',
                                 `last_login_ip` varchar(63) NOT NULL DEFAULT '' COMMENT '最近一次登录IP地址',
                                 `user_level` tinyint(3) DEFAULT '0' COMMENT '0 普通用户，1 VIP用户',
                                 `nickname` varchar(63) NOT NULL DEFAULT '' COMMENT '用户昵称或网络名称',
                                 `mobile` varchar(20) NOT NULL DEFAULT '' COMMENT '用户手机号码',
                                 `avatar` varchar(255) NOT NULL DEFAULT '' COMMENT '用户头像图片',
                                 `wx_openid` varchar(63) NOT NULL DEFAULT '' COMMENT '微信登录openid',
                                 `session_key` varchar(100) NOT NULL DEFAULT '' COMMENT '微信登录会话KEY',
                                 `status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '0 可用, 1 禁用, 2 注销',
                                 `add_time` datetime DEFAULT NULL COMMENT '创建时间',
                                 `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                 `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `user_name` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


DROP TABLE IF EXISTS `meal_shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meal_shop` (
                                  `id` int(11) NOT NULL AUTO_INCREMENT,
                                  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '商铺名称',
                                  `desc` varchar(255) NOT NULL DEFAULT '' COMMENT '商铺简介',
                                  `pic_url` varchar(255) NOT NULL DEFAULT '' COMMENT '商铺图片',
                                  `sort_order` int(3) DEFAULT '50' COMMENT '商铺排序',
                                  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
                                  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1046003 DEFAULT CHARSET=utf8mb4 COMMENT='商铺表';


DROP TABLE IF EXISTS `meal_search_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meal_search_history` (
                                           `id` int(11) NOT NULL AUTO_INCREMENT,
                                           `user_id` int(11) NOT NULL COMMENT '用户表的用户ID',
                                           `keyword` varchar(63) NOT NULL COMMENT '搜索关键字',
                                           `from` varchar(63) NOT NULL DEFAULT '' COMMENT '搜索来源',
                                           `add_time` datetime DEFAULT NULL COMMENT '创建时间',
                                           `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                           `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='搜索历史表';
