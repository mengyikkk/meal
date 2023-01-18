CREATE DATABASE meal DEFAULT CHARACTER SET UTF8MB4 COLLATE UTF8MB4_UNICODE_CI;

DROP TABLE IF EXISTS `meal_user`;

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
                                 `add_time` datetime  DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                                 `update_time` datetime DEFAULT NULL COMMENT '更新时间' ON UPDATE CURRENT_TIMESTAMP,
                                 `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `user_name` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


DROP TABLE IF EXISTS `meal_shop`;

CREATE TABLE `meal_shop` (
                                  `id` int(11) NOT NULL AUTO_INCREMENT,
                                  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '商铺名称',
                                  `desc` varchar(255) NOT NULL DEFAULT '' COMMENT '商铺简介',
                                  `pic_url` varchar(255) NOT NULL DEFAULT '' COMMENT '商铺图片',
                                  `sort_order` int(3) DEFAULT '50' COMMENT '商铺排序',
                                  `add_time` datetime  DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                                  `update_time` datetime DEFAULT NULL COMMENT '更新时间' ON UPDATE CURRENT_TIMESTAMP,
                                  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1046003 DEFAULT CHARSET=utf8mb4 COMMENT='商铺表';


DROP TABLE IF EXISTS `meal_search_history`;

CREATE TABLE `meal_search_history` (
                                           `id` int(11) NOT NULL AUTO_INCREMENT,
                                           `user_id` int(11) NOT NULL COMMENT '用户表的用户ID',
                                           `keyword` varchar(63) NOT NULL COMMENT '搜索关键字',
                                           `from` varchar(63) NOT NULL DEFAULT '' COMMENT '搜索来源',
                                           `add_time` datetime  DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                                           `update_time` datetime DEFAULT NULL COMMENT '更新时间' ON UPDATE CURRENT_TIMESTAMP,
                                           `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='搜索历史表';

DROP TABLE IF EXISTS `meal_goods`;

CREATE TABLE `meal_goods` (
                                  `id` int(11) NOT NULL AUTO_INCREMENT,
                                  `goods_sn` varchar(63) NOT NULL DEFAULT '' COMMENT '商品编号',
                                  `name` varchar(127) NOT NULL DEFAULT '' COMMENT '商品名称',
                                  `category_id` int(11) DEFAULT '0' COMMENT '商品所属类目ID',
                                  `shop_id` int(11) DEFAULT '0' COMMENT '商铺id',
                                  `keywords` varchar(255) DEFAULT '' COMMENT '商品关键字，采用逗号间隔',
                                  `brief` varchar(255) DEFAULT '' COMMENT '商品简介',
                                  `is_on_sale` tinyint(1) DEFAULT '1' COMMENT '是否上架',
                                  `is_time_on_sale` varchar(255) DEFAULT '1' COMMENT '1,早餐2,午餐 3,晚餐' '0,全时段供应',
                                  `sort_order` smallint(4) DEFAULT '100',
                                  `pic_url` varchar(255) DEFAULT NULL COMMENT '商品页面商品图片',
                                  `unit` varchar(31) DEFAULT '’份‘' COMMENT '商品单位，例如件、盒',
                                  `retail_price` decimal(10,2) DEFAULT '0.00' COMMENT '零售价格',
                                  `detail` text COMMENT '商品详细介绍，是富文本格式',
                                  `add_time` datetime  DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                                  `update_time` datetime DEFAULT NULL COMMENT '更新时间'  ON UPDATE CURRENT_TIMESTAMP,
                                  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                                  PRIMARY KEY (`id`),
                                  KEY `goods_sn` (`goods_sn`),
                                  KEY `cat_id` (`category_id`),
                                  KEY `shop_id` (`shop_id`),
                                  KEY `sort_order` (`sort_order`)
) ENGINE=InnoDB AUTO_INCREMENT=1181004 DEFAULT CHARSET=utf8mb4 COMMENT='商品基本信息表';


DROP TABLE IF EXISTS `meal_goods_product`;

CREATE TABLE `meal_goods_product` (
                                          `id` int(11) NOT NULL AUTO_INCREMENT,
                                          `goods_id` int(11) NOT NULL DEFAULT '0' COMMENT '商品表的商品ID',
                                          `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品货品价格(原价)',
                                          `number` int(11) NOT NULL DEFAULT '0' COMMENT '商品货品数量',
                                          `url` varchar(125) DEFAULT NULL COMMENT '商品货品图片',
                                          `add_time` datetime  DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                                          `update_time` datetime DEFAULT NULL COMMENT '更新时间'  ON UPDATE CURRENT_TIMESTAMP,
                                          `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                                          PRIMARY KEY (`id`),
                                          KEY `goods_id` (`goods_id`)
) ENGINE=InnoDB AUTO_INCREMENT=251 DEFAULT CHARSET=utf8mb4 COMMENT='商品货品表';


DROP TABLE IF EXISTS `meal_cart`;

CREATE TABLE `meal_cart` (
                                 `id` int(11) NOT NULL AUTO_INCREMENT,
                                 `user_id` int(11) DEFAULT NULL COMMENT '用户表的用户ID',
                                 `goods_id` int(11) DEFAULT NULL COMMENT '商品表的商品ID',
                                 `goods_sn` varchar(63) DEFAULT NULL COMMENT '商品编号',
                                 `goods_is_time` int(11) DEFAULT NULL COMMENT '商品时间到期提示',
                                 `goods_name` varchar(127) DEFAULT NULL COMMENT '商品名称',
                                 `number` int(11) DEFAULT 1 COMMENT '商品货品的数量',
                                 `checked` tinyint(1) DEFAULT '1' COMMENT '购物车中商品是否选择状态',
                                 `add_time` datetime  DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                                 `update_time` datetime DEFAULT NULL COMMENT '更新时间'  ON UPDATE CURRENT_TIMESTAMP,
                                 `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='购物车商品表';
