-- uw_code.code_template_group definition

CREATE TABLE `code_template_group` (
                                       `id` bigint NOT NULL COMMENT 'id',
                                       `group_type` int DEFAULT NULL COMMENT '数据源类型',
                                       `group_name` varchar(100) DEFAULT NULL COMMENT '模板组名',
                                       `group_desc` varchar(100) DEFAULT NULL COMMENT '模板组描述',
                                       `create_date` datetime(3) DEFAULT NULL COMMENT '创建日期',
                                       `modify_date` datetime(3) DEFAULT NULL COMMENT '修改日期',
                                       `state` int NOT NULL COMMENT '状态。1正常-1标记删除',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码模版组';


-- uw_code.code_template_info definition

CREATE TABLE `code_template_info` (
                                      `id` bigint NOT NULL COMMENT 'id',
                                      `group_id` bigint NOT NULL COMMENT '模板分组id',
                                      `template_type` int DEFAULT NULL COMMENT '数据类型',
                                      `template_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '模板名称',
                                      `template_desc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '模板描述',
                                      `template_filename` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '输出文件名模板',
                                      `template_body` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '模板内容',
                                      `create_date` datetime(3) DEFAULT NULL COMMENT '创建日期',
                                      `modify_date` datetime(3) DEFAULT NULL COMMENT '修改日期',
                                      `state` int NOT NULL COMMENT '状态。1正常-1标记删除',
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码模版';


-- uw_code.sys_crit_log definition

CREATE TABLE `sys_crit_log` (
                                `id` bigint NOT NULL COMMENT 'ID',
                                `saas_id` bigint NOT NULL COMMENT 'saasId',
                                `mch_id` bigint DEFAULT NULL COMMENT '商户ID',
                                `user_id` bigint NOT NULL COMMENT '用户id',
                                `user_type` int DEFAULT NULL COMMENT '用户类型',
                                `group_id` bigint DEFAULT NULL COMMENT '用户组ID',
                                `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户名',
                                `nick_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户昵称',
                                `real_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '真实名称',
                                `user_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户ip',
                                `api_uri` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '请求uri',
                                `api_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'API名称',
                                `biz_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务类型',
                                `biz_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务ID',
                                `biz_log` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '业务日志',
                                `request_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '请求时间',
                                `request_body` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '请求参数',
                                `response_state` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '响应状态',
                                `response_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '响应代码',
                                `response_msg` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '响应消息',
                                `response_body` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '响应日志',
                                `response_millis` bigint DEFAULT NULL COMMENT '请求毫秒数',
                                `status_code` int DEFAULT NULL COMMENT '响应状态码',
                                `app_info` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '应用信息',
                                `app_host` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '应用主机',
                                PRIMARY KEY (`id`),
                                KEY `sys_crit_log_saas_id_IDX` (`saas_id`,`user_id`) USING BTREE,
                                KEY `sys_crit_log_biz_type_IDX` (`biz_type`,`biz_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统关键日志';


-- uw_code.sys_data_history definition

CREATE TABLE `sys_data_history` (
                                    `id` bigint NOT NULL COMMENT 'ID',
                                    `saas_id` bigint NOT NULL COMMENT 'saasId',
                                    `mch_id` bigint DEFAULT NULL COMMENT '商户ID',
                                    `user_id` bigint NOT NULL COMMENT '用户ID',
                                    `user_type` int DEFAULT NULL COMMENT '用户类型',
                                    `group_id` bigint DEFAULT NULL COMMENT '用户的组ID',
                                    `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户名称',
                                    `nick_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户昵称',
                                    `real_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '真实名称',
                                    `entity_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '实体类',
                                    `entity_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '实体ID',
                                    `entity_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '实体名',
                                    `entity_data` json DEFAULT NULL COMMENT '实体数据',
                                    `entity_update_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '实体修改信息',
                                    `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '备注信息',
                                    `user_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户IP',
                                    `create_date` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建日期',
                                    PRIMARY KEY (`id`),
                                    KEY `sys_data_history_entity_class_IDX` (`entity_class`,`entity_id`) USING BTREE,
                                    KEY `sys_data_history_saas_id_IDX` (`saas_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统数据历史';


-- uw_code.sys_seq definition

CREATE TABLE `sys_seq` (
                           `seq_name` varchar(200) NOT NULL,
                           `seq_id` bigint DEFAULT NULL,
                           `seq_desc` varchar(200) DEFAULT NULL,
                           `increment_num` int DEFAULT NULL,
                           `create_date` datetime(3) DEFAULT NULL,
                           `last_update` datetime(3) DEFAULT NULL,
                           PRIMARY KEY (`seq_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='SYS序列';