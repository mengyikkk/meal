CREATE DATABASE meal DEFAULT CHARACTER SET UTF8MB4 COLLATE UTF8MB4_UNICODE_CI;

DROP TABLE IF EXISTS `meal_user`;

CREATE TABLE `meal_user` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `username` varchar(63) NOT NULL COMMENT '用户名称',
                                 `password` varchar(63) NOT NULL DEFAULT '' COMMENT '用户密码',
                                 `gender` tinyint(3) NOT NULL DEFAULT '0' COMMENT '性别：0 未知， 1男， 2女',
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
                                 `admin` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否是管理员',
                                 `add_time` datetime  DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                                 `update_time` datetime DEFAULT NULL COMMENT '更新时间' ON UPDATE CURRENT_TIMESTAMP,
                                 `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `user_name` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

DROP TABLE IF EXISTS `meal_menu`;
CREATE TABLE `meal_menu`  (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                             `path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单路径',
                             `icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
                             `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
                             `component` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单组件',
                             `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父级菜单',
                             `status` tinyint(1) NULL DEFAULT 0 COMMENT '显示状态(0不显示、1显示)',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `meal_permission`;
CREATE TABLE `meal_permission`  (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                   `label` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限标签',
                                   `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据权限标签值',
                                   `status` tinyint(1) NULL DEFAULT 0 COMMENT '显示状态(0不显示、1显示)',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `meal_role`;
CREATE TABLE `meal_role`  (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                             `label` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
                             `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色对应的标签值',
                             `status` tinyint(1) NULL DEFAULT 0 COMMENT '显示状态(0不显示、1显示)',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `roles_menus`;
CREATE TABLE `roles_menus`  (
                                `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
                                `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单权限ID'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `roles_permissions`;
CREATE TABLE `roles_permissions`  (
                                      `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
                                      `permission_id` bigint(20) NULL DEFAULT NULL COMMENT '数据权限ID'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles` (
                              `user_id` bigint DEFAULT NULL COMMENT '用户ID',
                              `role_id` bigint DEFAULT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;


DROP TABLE IF EXISTS `meal_shop`;

CREATE TABLE `meal_shop` (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '商铺名称',
                                  `desc` varchar(255) NOT NULL DEFAULT '' COMMENT '商铺简介',
                                  `pic_url` varchar(255) NOT NULL DEFAULT '' COMMENT '商铺图片',
                                  `sort_order` int(3) DEFAULT '50' COMMENT '商铺排序',
                                  `phone` varchar(30) NOT NULL COMMENT '店铺电话',
                                  `add_time` datetime  DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                                  `update_time` datetime DEFAULT NULL COMMENT '更新时间' ON UPDATE CURRENT_TIMESTAMP,
                                  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1046003 DEFAULT CHARSET=utf8mb4 COMMENT='商铺表';


DROP TABLE IF EXISTS `meal_search_history`;

CREATE TABLE `meal_search_history` (
                                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                           `user_id` bigint(20) NOT NULL COMMENT '用户表的用户ID',
                                           `keyword` varchar(63) NOT NULL COMMENT '搜索关键字',
                                           `from` varchar(63) NOT NULL DEFAULT '' COMMENT '搜索来源',
                                           `add_time` datetime  DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                                           `update_time` datetime DEFAULT NULL COMMENT '更新时间' ON UPDATE CURRENT_TIMESTAMP,
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='搜索历史表';

DROP TABLE IF EXISTS `meal_goods`;

CREATE TABLE `meal_goods` (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                  `goods_sn` varchar(63) NOT NULL DEFAULT '' COMMENT '商品编号',
                                  `name` varchar(127) NOT NULL DEFAULT '' COMMENT '商品名称',
                                  `category_id` bigint(20) DEFAULT '0' COMMENT '商品所属类目ID',
                                  `shop_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '商铺id',
                                  `keywords` varchar(255) DEFAULT '' COMMENT '商品关键字，采用逗号间隔',
                                  `brief` varchar(255) DEFAULT '' COMMENT '商品简介',
                                  `is_on_sale` tinyint(1) DEFAULT '1' COMMENT '是否上架',
                                  `is_time_on_sale` int(11) DEFAULT 0 COMMENT '1,早餐2,午餐 3,晚餐 0,全时段供应',
                                  `sort_order` bigint(20) DEFAULT '100',
                                  `pic_url` varchar(255) DEFAULT NULL COMMENT '商品页面商品图片',
                                  `unit` varchar(31) DEFAULT '’份‘' COMMENT '商品单位，例如件、盒',
                                  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品货品价格(原价)',
                                  `number` bigint(20) NOT NULL DEFAULT '0' COMMENT '商品货品数量',
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


DROP TABLE IF EXISTS `meal_little_calamity`;

CREATE TABLE `meal_little_calamity` (
                                          `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                          `goods_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '0 对应 普通小料,1对应限定商品小料',
                                          `pic_url` varchar(255) DEFAULT NULL COMMENT '商品页面商品图片',
                                          `name`   varchar(31) DEFAULT '' COMMENT '小料名称',
                                          `shop_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '0全店铺小料 商铺限定id',
                                          `number` bigint(20) NOT NULL DEFAULT '0' COMMENT '商品货品数量',
                                          `brief` varchar(255) DEFAULT '' COMMENT '商品简介',
                                          `sort_order` bigint(20) DEFAULT '100',
                                          `retail_price` decimal(10,2) DEFAULT '0.00' COMMENT '零售价格',
                                          `unit` varchar(31) DEFAULT '’份‘' COMMENT '商品单位，例如件、盒',
                                          `is_on_sale` tinyint(1) DEFAULT '1' COMMENT '是否上架',
                                          `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品货品价格(原价)',
                                          `is_time_on_sale` int(11) DEFAULT 0 COMMENT '1,早餐2,午餐 3,晚餐 0,全时段供应',
                                          `detail` text COMMENT '商品详细介绍，是富文本格式',
                                          `add_time` datetime  DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                                          `update_time` datetime DEFAULT NULL COMMENT '更新时间'  ON UPDATE CURRENT_TIMESTAMP,
                                          `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                                          PRIMARY KEY (`id`),
                                          KEY `goods_id` (`goods_id`),
                                          KEY `shop_id` (`shop_id`)
) ENGINE=InnoDB AUTO_INCREMENT=251 DEFAULT CHARSET=utf8mb4 COMMENT='商品小料表';


DROP TABLE IF EXISTS `meal_cart`;

CREATE TABLE `meal_cart` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `user_id` bigint(20) DEFAULT NULL COMMENT '用户表的用户ID',
                                 `shop_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '商铺id',
                                 `goods_id` bigint(20) DEFAULT NULL COMMENT '商品表的商品ID',
                                 `goods_sn` varchar(63) DEFAULT NULL COMMENT '商品编号',
                                 `goods_is_time` tinyint(1) DEFAULT '1' COMMENT '商品时间到期提示',
                                 `goods_name` varchar(127) DEFAULT NULL COMMENT '商品名称',
                                 `number` bigint(20) DEFAULT 1 COMMENT '商品货品的数量',
                                 `checked` tinyint(1) DEFAULT '1' COMMENT '购物车中商品是否选择状态',
                                 `add_time` datetime  DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                                 `update_time` datetime DEFAULT NULL COMMENT '更新时间'  ON UPDATE CURRENT_TIMESTAMP,
                                 `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='购物车商品表';

DROP TABLE IF EXISTS `meal_cart_calamity`;
CREATE TABLE `meal_cart_calamity` (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT,
                             `cart_id` bigint(20) DEFAULT NULL COMMENT '购物车表ID',
                             `calamity_id` bigint(20) DEFAULT NULL COMMENT '小料表的商品ID',
                             `calamity_name` varchar(127) DEFAULT NULL COMMENT '小料名称',
                             `calamity_number` bigint(20) DEFAULT 1 COMMENT '小料数量',
                             `add_time` datetime  DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间'  ON UPDATE CURRENT_TIMESTAMP,
                             `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='购物车商品小料子表';

DROP TABLE IF EXISTS `meal_category`;

CREATE TABLE `meal_category` (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                     `name` varchar(63) NOT NULL DEFAULT '' COMMENT '类目名称',
                                     `shop_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '商铺id',
                                     `keywords` varchar(1023) NOT NULL DEFAULT '' COMMENT '类目关键字，以JSON数组格式',
                                     `desc` varchar(255) DEFAULT '' COMMENT '类目广告语介绍',
                                     `pid` int(11) NOT NULL DEFAULT '0' COMMENT '父类目ID',
                                     `icon_url` varchar(255) DEFAULT '' COMMENT '类目图标',
                                     `pic_url` varchar(255) DEFAULT '' COMMENT '类目图片',
                                     `sort_order` tinyint(3) DEFAULT '50' COMMENT '排序',
                                     `is_time_on_sale` int(11) DEFAULT 0 COMMENT '1,早餐2,午餐 3,晚餐 0,全时段供应',
                                     `add_time` datetime  DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                                     `update_time` datetime DEFAULT NULL COMMENT '更新时间'  ON UPDATE CURRENT_TIMESTAMP,
                                     `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                                     PRIMARY KEY (`id`),
                                     KEY `parent_id` (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=1036007 DEFAULT CHARSET=utf8mb4 COMMENT='类目表';

DROP TABLE IF EXISTS `meal_banner`;

CREATE TABLE `meal_banner` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `shop_id` bigint(20)  COMMENT '商铺id',
                                 `banner_url` varchar(255) DEFAULT '' COMMENT 'banner图标',
                                 `name` varchar(63) NOT NULL DEFAULT '' COMMENT '名称',
                                 `order_num` tinyint(5) DEFAULT '50' COMMENT '排序',
                                 `desc` varchar(255) DEFAULT '' COMMENT 'banner语介绍',
                                 `add_time` datetime  DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                                 `update_time` datetime DEFAULT NULL COMMENT '更新时间'  ON UPDATE CURRENT_TIMESTAMP,
                                 `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1036007 DEFAULT CHARSET=utf8mb4 COMMENT='banner';

DROP TABLE IF EXISTS `meal_user_shop`;
CREATE TABLE `meal_user_shop` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT,
                               `user_id` bigint(20)  NOT NULL COMMENT '商铺id',
                               `shop_id` bigint(20) NOT NULL COMMENT '店铺id',
                               `add_time` datetime  DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                               `update_time` datetime DEFAULT NULL COMMENT '更新时间'  ON UPDATE CURRENT_TIMESTAMP,
                               `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                                PRIMARY KEY (`id`),
                                UNIQUE KEY unique_user (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='绑定店铺';

DROP TABLE IF EXISTS `meal_order`;
CREATE TABLE `meal_order` (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                  `user_id` bigint(20) NOT NULL COMMENT '用户表的用户ID',
                                  `shop_id` bigint(20) NOT NULL COMMENT '店铺id',
                                  `order_sn` varchar(255) NOT NULL  COMMENT '订单编号',
                                  `order_status` smallint(6) NOT NULL DEFAULT 0  COMMENT '订单状态 0是未支付 1是已支付 3 订单完成 4订单退款',
                                  `ship_status` smallint(6) NOT NULL DEFAULT 0 COMMENT '取货状态 0是未取货 1是已取货 ',
                                  `refund_status` smallint(6) NOT NULL DEFAULT 0 COMMENT '退款状态，0是可申请，1是用户已申请，2是管理员审核通过，3是管理员退款成功，4是管理员审核拒绝，5是用户已取消',
                                  `consignee` varchar(63) NOT NULL COMMENT '收货人名称',
                                  `mobile` varchar(63) NOT NULL COMMENT '收货人手机号',
                                  `address` varchar(127) DEFAULT '' COMMENT '收货具体地址',
                                  `message` varchar(512)  DEFAULT '' COMMENT '用户订单留言',
                                  `goods_price` decimal(10,2) DEFAULT 0.00 COMMENT '商品总费用',
                                  `freight_price` decimal(10,2) DEFAULT 0.00 COMMENT '配送费用',
                                  `coupon_price` decimal(10,2) DEFAULT 0.00 COMMENT '优惠券减免',
                                  `order_price` decimal(10,2) NOT NULL COMMENT '订单费用， = goods_price + freight_price - coupon_price',
                                  `actual_price` decimal(10,2) NOT NULL COMMENT '实付费用， = order_price',
                                  `pay_id` varchar(63) DEFAULT NULL COMMENT '微信付款编号',
                                  `pay_time` datetime DEFAULT NULL COMMENT '微信付款时间',
                                  `ship_sn` varchar(63) DEFAULT NULL COMMENT '取货编号',
                                  `ship_time` datetime DEFAULT NULL COMMENT '取货时间',
                                  `is_time_on_sale` int(11) DEFAULT 0 COMMENT '1,早餐2,午餐 3,晚餐 0,全时段供应',
                                  `refund_amount` decimal(10,2) DEFAULT NULL COMMENT '实际退款金额，（有可能退款金额小于实际支付金额）',
                                  `refund_content` varchar(127) DEFAULT NULL COMMENT '退款备注',
                                  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
                                  `confirm_time` datetime DEFAULT NULL COMMENT '用户确认收货时间',
                                  `end_time` datetime DEFAULT NULL COMMENT '订单关闭时间=用户确认时间',
                                  `add_time` datetime  DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                                  `update_time` datetime DEFAULT NULL COMMENT '更新时间'  ON UPDATE CURRENT_TIMESTAMP,
                                  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                                  `version` INT NOT NULL DEFAULT 0,
                                  PRIMARY KEY (`id`),
                                  KEY `user_id` (`user_id`),
                                  KEY `shop_id` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
DROP TABLE IF EXISTS `meal_order_goods`;

CREATE TABLE `meal_order_goods` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                        `order_id` bigint(20) NOT NULL  COMMENT '订单表的订单ID',
                                        `goods_id` bigint(20) NOT NULL  COMMENT '订单商品表的ID',
                                        `goods_name` varchar(127) NOT NULL DEFAULT '' COMMENT '商品名称',
                                        `goods_sn` varchar(63) NOT NULL DEFAULT '' COMMENT '商品编号',
                                        `unit` varchar(31) DEFAULT '’份‘' COMMENT '商品单位，例如件、盒',
                                        `number` bigint(20) NOT NULL DEFAULT '0' COMMENT '商品货品的购买数量',
                                        `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品货品的售价',
                                        `pic_url` varchar(255)  NULL DEFAULT '' COMMENT '商品货品图片或者商品图片',
                                        `add_time` datetime  DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                                        `update_time` datetime DEFAULT NULL COMMENT '更新时间'  ON UPDATE CURRENT_TIMESTAMP,
                                        `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                                        `version` INT NOT NULL DEFAULT 0,
                                        PRIMARY KEY (`id`),
                                        KEY `order_id` (`order_id`),
                                        KEY `goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单商品表';
DROP TABLE IF EXISTS `meal_order_goods_calamity`;
CREATE TABLE `meal_order_goods_calamity` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                    `order_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '订单表的订单ID',
                                    `order_goods_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '商品表的商品ID',
                                    `calamity_id` bigint(11) NOT NULL DEFAULT '0' COMMENT '商品表的商品ID',
                                    `calamity_name` varchar(127) NOT NULL DEFAULT '' COMMENT '商品名称',
                                    `calamity_sn` varchar(63) NOT NULL DEFAULT '' COMMENT '商品编号',
                                    `number` bigint(20) NOT NULL DEFAULT '0' COMMENT '商品货品的购买数量',
                                    `unit` varchar(31) DEFAULT '’份‘' COMMENT '商品单位，例如件、盒',
                                    `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品货品的售价',
                                    `pic_url` varchar(255)  NULL DEFAULT '' COMMENT '商品货品图片或者商品图片',
                                    `add_time` datetime  DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                                    `update_time` datetime DEFAULT NULL COMMENT '更新时间'  ON UPDATE CURRENT_TIMESTAMP,
                                    `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                                    `version` INT NOT NULL DEFAULT 0,
                                    PRIMARY KEY (`id`),
                                    KEY `order_id` (`order_id`),
                                    KEY `goods_id` (`order_goods_id`),
                                    KEY `calamity_id` (`calamity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单小料表';
DROP TABLE IF EXISTS `meal_order_wx`;
CREATE TABLE `meal_order_wx` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                    `order_id` bigint(20) NOT NULL  COMMENT '订单表的订单ID',
                                    `order_type` varchar(25) NOT NULL  COMMENT '订单种类',
                                    `request_param` TEXT NOT NULL  COMMENT '请求wx_json',
                                    `response_param` TEXT NOT NULL  COMMENT 'wx返回json',
                                    `add_time` datetime  DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                                    `update_time` datetime DEFAULT NULL COMMENT '更新时间'  ON UPDATE CURRENT_TIMESTAMP,
                                    `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
                                    `version` INT NOT NULL DEFAULT 0,
                                    PRIMARY KEY (`id`),
                                    KEY `order_id` (`order_id`),
                                    KEY `order_type` (`order_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信日志表';
