-- code.code_crit_log definition

CREATE TABLE `code_crit_log` (
                                 `id` bigint NOT NULL COMMENT '主键ID',
                                 `saas_id` bigint NOT NULL COMMENT '运营商编号',
                                 `mch_id` bigint DEFAULT NULL COMMENT '商户id',
                                 `user_id` bigint NOT NULL COMMENT '操作人id',
                                 `user_type` int DEFAULT NULL COMMENT '用户类型',
                                 `group_id` bigint DEFAULT NULL COMMENT '用户的组ID',
                                 `user_name` varchar(100) DEFAULT NULL COMMENT '操作人名称',
                                 `nick_name` varchar(100) DEFAULT NULL COMMENT '操作人用户昵称',
                                 `real_name` varchar(100) DEFAULT NULL COMMENT '操作人真实名称',
                                 `object_id` varchar(100) DEFAULT NULL COMMENT '操作对象id',
                                 `object_type` varchar(100) DEFAULT NULL COMMENT '操作对象类型',
                                 `uri` varchar(500) DEFAULT NULL COMMENT '请求uri',
                                 `info` varchar(1000) DEFAULT NULL COMMENT '方法操作描述',
                                 `log` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '日志内容',
                                 `request_body` text COMMENT '请求参数',
                                 `response_body` text COMMENT '响应日志',
                                 `response_millis` bigint DEFAULT NULL COMMENT '请求毫秒数',
                                 `exception` text COMMENT '异常信息',
                                 `status_code` int DEFAULT NULL COMMENT '响应状态码',
                                 `app_info` varchar(100) DEFAULT NULL COMMENT '应用信息',
                                 `app_host` varchar(100) DEFAULT NULL COMMENT '应用主机',
                                 `user_ip` varchar(50) DEFAULT NULL COMMENT '操作人ip',
                                 `request_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='code关键日志';


-- code.code_data_history definition

CREATE TABLE `code_data_history` (
                                     `id` bigint NOT NULL COMMENT 'ID#主键',
                                     `entity_id` bigint DEFAULT NULL COMMENT '修改的表记录Id',
                                     `entity_class` varchar(255) DEFAULT NULL COMMENT '对应的实例类',
                                     `entity_data` json DEFAULT NULL COMMENT '修改时被替换的旧值',
                                     `entity_update_info` text COMMENT '修改的字段信息',
                                     `saas_id` bigint DEFAULT NULL COMMENT 'saasId',
                                     `mch_id` bigint DEFAULT NULL COMMENT '商户id',
                                     `user_id` bigint DEFAULT NULL COMMENT '用户Id',
                                     `user_type` int DEFAULT NULL COMMENT '用户类型',
                                     `group_id` bigint DEFAULT NULL COMMENT '用户的组ID',
                                     `user_name` varchar(100) DEFAULT NULL COMMENT '用户名称',
                                     `nick_name` varchar(100) DEFAULT NULL COMMENT '操作人用户昵称',
                                     `real_name` varchar(100) DEFAULT NULL COMMENT '操作人真实名称',
                                     `user_ip` varchar(100) DEFAULT NULL COMMENT '操作人的ip',
                                     `create_date` datetime(3) DEFAULT NULL COMMENT '创建日期',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='code数据历史';


-- code.code_template definition

CREATE TABLE `code_template` (
                                 `id` bigint NOT NULL COMMENT 'id',
                                 `group_id` bigint NOT NULL COMMENT '模板分组id',
                                 `template_type` int DEFAULT NULL COMMENT '数据类型',
                                 `template_name` varchar(100) DEFAULT NULL COMMENT '模板名称',
                                 `template_desc` varchar(100) DEFAULT NULL COMMENT '模板描述',
                                 `template_filename` varchar(100) DEFAULT NULL COMMENT '输出文件名模板',
                                 `template_body` longtext COMMENT '模板内容',
                                 `create_date` datetime(3) DEFAULT NULL COMMENT '创建日期',
                                 `modify_date` datetime(3) DEFAULT NULL COMMENT '修改日期',
                                 `state` int NOT NULL COMMENT '状态。1正常-1标记删除',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码模版';


-- code.code_template_group definition

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


-- code.sys_seq definition

CREATE TABLE `sys_seq` (
                           `seq_name` varchar(200) NOT NULL,
                           `seq_id` bigint DEFAULT NULL,
                           `seq_desc` varchar(200) DEFAULT NULL,
                           `increment_num` int DEFAULT NULL,
                           `create_date` datetime(3) DEFAULT NULL,
                           `last_update` datetime(3) DEFAULT NULL,
                           PRIMARY KEY (`seq_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='SYS序列';