# uw-code-center · 代码生成中心

> 基于数据库元数据、Swagger/OpenAPI 规范及 JSON/XML 结构，驱动 FreeMarker 模板批量生成可运行代码的后端微服务。

---

## 目录

1. [项目概述](#1-项目概述)
2. [核心功能](#2-核心功能)
3. [技术栈](#3-技术栈)
4. [项目结构](#4-项目结构)
5. [快速开始](#5-快速开始)
6. [配置说明](#6-配置说明)
7. [API 接口文档](#7-api-接口文档)
8. [数据库设计](#8-数据库设计)
9. [模板系统](#9-模板系统)
10. [构建与部署](#10-构建与部署)
11. [常见问题解答](#11-常见问题解答)

---

## 1. 项目概述

**uw-code-center** 是 UW 平台的代码生成微服务，提供三类代码生成能力：

| 生成类型 | 数据源 | 典型输出 |
|---|---|---|
| 数据库驱动 | MySQL / Oracle 元数据 | Entity、Controller、DTO、Mapper 等 |
| JSON/XML 驱动 | 原始 JSON 或 XML 文本 | Java VO（值对象）类文件 |
| Swagger/OpenAPI 驱动 | OpenAPI v3 接口文档 URL | Vue3 前端代码、JMeter 测试脚本 |

所有生成逻辑均由 **FreeMarker 模板**驱动，模板数据持久化在数据库中，可在线管理和热更新，无需重启服务。

- **Maven 坐标**：`com.umtone:uw-code-center:1.1.6`
- **应用名称**：`uw-code-center`
- **默认服务端口**：`10050`（CI 部署）/ `10030`（日志默认）

---

## 2. 核心功能

### 2.1 数据库驱动代码生成

- 通过已注册的 JDBC 连接池名称（`connName`）接入 MySQL 或 Oracle 数据库
- 使用 `DatabaseMetaData` API（MySQL）或系统字典表（Oracle）自动读取：
  - 表/视图清单、字段信息、主键信息、注释
  - SQL 类型 → Java 类型的映射（见下表）
- 根据选择的**模板分组**批量渲染 FreeMarker 模板，生成对应源文件
- 结果打包为 ZIP 文件下载

**SQL → Java 类型映射规则：**

| SQL 类型 | Java 类型 |
|---|---|
| BIGINT | `long` |
| INTEGER / INT | `int` |
| TINYINT | `int` |
| FLOAT / REAL | `float` |
| DOUBLE | `double` |
| NUMERIC / DECIMAL（Oracle：位数<10） | `int` |
| NUMERIC / DECIMAL（Oracle：位数<18） | `long` |
| NUMERIC / DECIMAL（其余） | `java.math.BigDecimal` |
| VARCHAR / CHAR / TEXT / CLOB | `String` |
| DATE / TIMESTAMP / DATETIME | `java.util.Date` |
| BLOB / BINARY | `byte[]` |

### 2.2 JSON/XML → Java VO 生成

- 接受原始 JSON 文本或 XML 文本（XML 内部先转为 JSON 再处理）
- 递归解析嵌套对象/数组，自动提取内层类为 **static inner class**
- 字段命名支持 camelCase 及 `underscore_to_camel` 自动转换
- 支持多种**注解风格**（通过 `AnnotationStyle` 枚举控制）：

| 风格 | 说明 |
|---|---|
| `JACKSON2` | 生成 `@JsonProperty`、`@JsonIgnoreProperties` 等 |
| `JAXB` | 生成 `@XmlRootElement`、`@XmlElement` 等 |
| `XSTREAM` | 生成 XStream 注解 |
| `NONE` | 无序列化注解 |

- 可选附加 SpringDoc `@Schema` 注解（`takeSwagger=true`）
- 批量模式：上传包含多个 JSON/XML 文件的 ZIP，返回对应 Java 类 ZIP

### 2.3 Swagger/OpenAPI 驱动代码生成

- 通过 `swagger-parser-v3` 库远程拉取并解析 OpenAPI v3 文档（支持逗号分隔多个 URL）
- 解析产物：
  - `schemaInfoList`：DTO/模型定义列表，含字段类型映射（OpenAPI → TypeScript）
  - `apiInfoList`：全量接口列表，含请求参数、请求体、响应体
  - `apiGroupInfoList`：按父路径分组的接口组
  - `apiCatalogInfoList`：顶层菜单目录（由 `$-package-info-$` 标记控制）
- 自动剥离响应包装类型（`ResponseData`、`DataList`、`ESDataList`）
- 路径到函数名转换规则：`/ops/template/group/list` → `opsTemplateGroupList`
- 生成 Vue3 前端工程代码（API 客户端、Router、页面组件、i18n）
- 生成 JMeter 压测脚本
- 结果打包为 ZIP 文件下载

### 2.4 模板管理

- 模板数据存储在 `code_template_group`（分组）和 `code_template_info`（模板内容）两张表中
- FreeMarker `StringTemplateLoader` 在启动时一次性加载所有启用模板到内存
- 每条模板记录注册两个 FreeMarker 模板 key：`{id}#filename`（输出文件名模板）和 `{id}#body`（代码体模板）
- **热更新**：调用模板更新接口后，服务端自动重新加载 FreeMarker 缓存，无需重启
- 支持启用/禁用/软删除操作，以及完整的变更历史记录

---

## 3. 技术栈

### 运行时环境

| 组件 | 版本 / 说明 |
|---|---|
| JDK | Java 25（BellSoft Liberica JRE 25 CDS） |
| 构建工具 | Maven |
| 父 BOM | `com.umtone:uw-base:2026.0301.0002` |
| Spring Boot | 由父 BOM 管理 |
| Spring Cloud | Bootstrap、LoadBalancer |
| Spring Cloud Alibaba | Nacos Discovery + Nacos Config |

### 核心依赖

| 依赖 | 版本 | 用途 |
|---|---|---|
| `org.freemarker:freemarker` | 由父 BOM 管理 | 代码模板渲染引擎 |
| `org.springdoc:springdoc-openapi-starter-webmvc-ui` | 由父 BOM 管理 | OpenAPI/Swagger UI |
| `io.swagger.parser.v3:swagger-parser-v3` | 2.1.29 | 解析远程 OpenAPI v3 文档 |
| `org.json:json` | 20240303 | JSON 解析（VO 生成） |
| `javax.xml.bind:jaxb-api` | 2.4.0-b180830.0359 | XML VO 生成 JAXB 支持 |
| `com.google.guava` | 由父 BOM 管理 | `CaseFormat`、`ImmutableMap` 等工具 |
| `org.apache.commons:commons-lang3` | 由父 BOM 管理 | `StringUtils`、`FastDateFormat` |

### 内部平台库

| 库 | 用途 |
|---|---|
| `com.umtone:uw-common-app` | 公共实体（`SysCritLog`、`SysDataHistory`）、Helper 类 |
| `com.umtone:uw-dao` | 自研 ORM/DAO 框架（`DaoManager`、`DataEntity`、序列 ID 生成） |
| `com.umtone:uw-log-es` | Elasticsearch 日志集成 |
| `com.umtone:uw-logback-es` | Logback → ES Appender |

### 基础设施

| 组件 | 用途 |
|---|---|
| Nacos | 服务注册与配置中心 |
| MySQL / Oracle | 持久化存储（模板数据 + 元数据来源） |
| Elasticsearch（可选） | 结构化日志存储 |
| Docker | 容器化部署 |
| Gitea Actions | CI/CD 流水线 |

---

## 4. 项目结构

```
uw-code-center/
├── .gitea/
│   └── workflows/
│       └── build.yml               # CI/CD 流水线（push main 触发构建+部署）
├── database/
│   ├── ddl.sql                     # 数据库建表 DDL
│   └── backup-uw_code.sql          # 数据库备份
├── Dockerfile                      # 多阶段 Docker 构建
├── pom.xml                         # Maven 项目描述符
└── src/
    ├── main/
    │   ├── java/uw/code/center/
    │   │   ├── UwCodeCenterApplication.java        # Spring Boot 入口
    │   │   ├── conf/
    │   │   │   ├── CodeCenterAutoConfiguration.java # 启动初始化（加载模板缓存）
    │   │   │   └── SwaggerConfig.java               # Springdoc 配置
    │   │   ├── constant/
    │   │   │   ├── TemplateGroupType.java           # 枚举：DATABASE(1), SWAGGER(2)
    │   │   │   └── TemplateInfoType.java            # 枚举：10-13 DB类, 20-24 Vue类, 31 JMeter
    │   │   ├── controller/
    │   │   │   ├── open/                            # 调试用接口（仅 debug/dev profile）
    │   │   │   │   ├── DaoController.java
    │   │   │   │   ├── EnumController.java
    │   │   │   │   └── TestController.java
    │   │   │   └── ops/                             # 生产业务接口
    │   │   │       ├── codegen/
    │   │   │       │   ├── DatabaseGenCodeController.java
    │   │   │       │   ├── JsonXmlGenCodeController.java
    │   │   │       │   └── SwaggerGenCodeController.java
    │   │   │       ├── log/
    │   │   │       │   ├── SysCritLogController.java
    │   │   │       │   └── SysDataHistoryController.java
    │   │   │       └── template/
    │   │   │           ├── CodeTemplateGroupController.java
    │   │   │           └── CodeTemplateInfoController.java
    │   │   ├── dto/
    │   │   │   ├── CodeTemplateGroupQueryParam.java
    │   │   │   ├── CodeTemplateInfoQueryParam.java
    │   │   │   └── JsonXmlRequestParam.java
    │   │   ├── entity/
    │   │   │   ├── CodeTemplateGroup.java           # 模板分组实体
    │   │   │   └── CodeTemplateInfo.java            # 模板内容实体
    │   │   ├── service/
    │   │   │   ├── dao/                             # 数据库元数据解析
    │   │   │   │   ├── DataMetaInterface.java
    │   │   │   │   ├── DatabaseMetaParser.java      # 工厂类（MySQL/Oracle 分发）
    │   │   │   │   ├── MetaColumnInfo.java
    │   │   │   │   ├── MetaPrimaryKeyInfo.java
    │   │   │   │   ├── MetaTableInfo.java
    │   │   │   │   ├── MySQLDataMetaImpl.java
    │   │   │   │   └── OracleDataMetaImpl.java
    │   │   │   ├── jsonxml/                         # JSON/XML → Java VO 生成
    │   │   │   │   ├── AnnotationStyle.java
    │   │   │   │   ├── AnnotationType.java
    │   │   │   │   ├── GenerationConfig.java
    │   │   │   │   ├── GenerationType.java
    │   │   │   │   ├── JavaType.java
    │   │   │   │   ├── Json2JavaVo.java
    │   │   │   │   └── VoCodeGenTools.java          # 核心生成引擎
    │   │   │   └── swagger/                         # OpenAPI 解析
    │   │   │       ├── ApiCatalogInfo.java
    │   │   │       ├── ApiGroupInfo.java
    │   │   │       ├── ApiInfo.java
    │   │   │       ├── SchemaInfo.java
    │   │   │       └── SwaggerParser.java           # OpenAPI v3 解析器
    │   │   ├── template/
    │   │   │   └── TemplateHelper.java              # FreeMarker 模板引擎封装
    │   │   └── util/
    │   │       └── DaoStringUtils.java              # 命名转换工具（camelCase、路径等）
    │   └── resources/
    │       ├── bootstrap.yml                        # 生产配置（Nacos 变量驱动）
    │       ├── bootstrap-debug.yml                  # 本地开发配置（硬编码连接信息）
    │       └── logback-spring.xml                   # 日志配置（Console + ES）
    └── test/
        └── java/uw/code/center/
            └── UwCodegenApplicationTests.java
```

---

## 5. 快速开始

### 前置条件

- JDK 25+
- Maven 3.8+
- MySQL 8.0+ 或 Oracle 19c+（用于持久化模板数据）
- Nacos 2.x 服务（用于配置中心和服务注册）

### 本地开发启动

**第一步：初始化数据库**

```sql
-- 执行建表 DDL
source database/ddl.sql;
```

**第二步：修改本地开发配置**

编辑 `src/main/resources/bootstrap-debug.yml`，确认 Nacos 地址和命名空间匹配本地环境：

```yaml
spring:
  cloud:
    nacos:
      server-addr: 192.168.88.21:8848   # 修改为实际地址
      username: nacos
      password: nacos888
      config:
        namespace: dev
      discovery:
        register-enabled: false         # 本地开发关闭服务注册
```

**第三步：确认 Nacos 配置**

在 Nacos 的 `dev` 命名空间下，为 `uw-code-center` 创建如下 YAML 配置（示例）：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/uw_code?serverTimezone=Asia/Shanghai
    username: root
    password: yourpassword
    driver-class-name: com.mysql.cj.jdbc.Driver
```

**第四步：启动应用**

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=debug
```

访问 Swagger UI：`http://localhost:8080/swagger-ui/index.html`

### 构建 JAR

```bash
mvn clean package -Dmaven.test.skip=true
```

JAR 文件输出至 `target/uw-code-center-*.jar`。

---

## 6. 配置说明

### 6.1 环境变量（生产部署必填）

| 变量名 | 是否必填 | 说明 |
|---|---|---|
| `NACOS_SERVER` | 必填 | Nacos 服务地址，如 `192.168.1.10:8848` |
| `NACOS_USERNAME` | 必填 | Nacos 认证用户名 |
| `NACOS_PASSWORD` | 必填 | Nacos 认证密码 |
| `NACOS_NAMESPACE` | 必填 | Nacos 配置命名空间 ID |
| `APP_HOST` | 可选 | 服务注册时对外暴露的 IP 地址 |
| `APP_PORT` | 可选 | 日志标识用端口，默认 `10030` |
| `LOG_ES_SERVER` | 可选 | Elasticsearch 日志服务地址，如 `http://host:9200` |
| `LOG_ES_USERNAME` | 可选 | ES 认证用户名 |
| `LOG_ES_PASSWORD` | 可选 | ES 认证密码 |
| `JAVA_OPTS` | 可选 | JVM 启动参数，如 `-Xmx512m` |
| `SPRING_OPTS` | 可选 | Spring Boot 启动参数，如 `--server.port=8080` |

### 6.2 bootstrap.yml 关键配置项

```yaml
project:
  name: uw-code-center          # 应用名称（Maven filtering 自动填充）
  version: 1.1.6                # 版本号（Maven filtering 自动填充）

spring:
  application:
    name: ${project.name}
  cloud:
    nacos:
      server-addr: ${NACOS_SERVER}
      username: ${NACOS_USERNAME}
      password: ${NACOS_PASSWORD}
      config:
        namespace: ${NACOS_NAMESPACE}
        file-extension: yaml
      discovery:
        namespace: ${spring.cloud.nacos.config.namespace}
        service: ${spring.application.name}
        ip: ${APP_HOST:}        # 为空时 Nacos 客户端自动探测本机 IP
```

### 6.3 日志配置（logback-spring.xml）

| Profile | 输出目标 |
|---|---|
| `debug` | 仅输出到控制台（Console） |
| 其他（生产） | 控制台 + Elasticsearch |

ES 日志推送参数（可通过环境变量覆盖）：

| 参数 | 默认值 | 说明 |
|---|---|---|
| ES 地址 | `http://192.168.88.21:9200` | 通过 `LOG_ES_SERVER` 覆盖 |
| 最大刷新间隔 | 10 秒 | |
| 最大批量大小 | 8 MB | |
| 批处理线程数 | 5 | |
| 队列大小 | 20 | |
| 堆栈跟踪深度 | 20 | |
| 索引名 | `${spring.application.name}` | |

---

## 7. API 接口文档

> 所有 `/ops/**` 接口均需要 `UserType.OPS` 级别的身份认证（由上游网关/认证服务控制）。
> 调试接口 `/open/**` 仅在 `debug` 或 `dev` Spring Profile 下激活。

### 7.1 代码生成 — 数据库 `(/ops/codegen/databaseGenCode)`

#### `GET /ops/codegen/databaseGenCode/list`

获取已注册的数据库连接名称列表。

**响应**：`ResponseData<List<String>>`

---

#### `GET /ops/codegen/databaseGenCode/tableInfoList`

获取指定数据库连接的表/视图列表。

| 参数 | 类型 | 必填 | 说明 |
|---|---|---|---|
| `connName` | String | 是 | 连接池名称 |
| `schemaName` | String | 否 | Schema 名称（Oracle 用户名/Schema） |
| `filter` | String | 否 | 表名过滤关键字 |

**响应**：`ResponseData<List<MetaTableInfo>>`

`MetaTableInfo` 字段：

| 字段 | 类型 | 说明 |
|---|---|---|
| `tableName` | String | 原始表名（如 `user_info`） |
| `tableType` | String | TABLE 或 VIEW |
| `remarks` | String | 表注释 |
| `entityName` | String | 实体类名（如 `UserInfo`） |
| `lowerEntityName` | String | 首字母小写实体名（如 `userInfo`） |
| `pathStyle` | String | 路径格式（如 `/user/info`） |

---

#### `GET /ops/codegen/databaseGenCode/downloadCode`

批量生成代码并下载 ZIP 包。

| 参数 | 类型 | 必填 | 说明 |
|---|---|---|---|
| `connName` | String | 是 | 连接池名称 |
| `schemaName` | String | 否 | Schema 名称 |
| `templateGroupId` | long | 是 | 模板分组 ID |
| `filterTableNames` | Set\<String\> | 否 | 指定生成的表名集合；为空则生成全部 |

**响应**：`application/zip` 文件流（`Content-Disposition: attachment; filename="code.zip"`）

---

### 7.2 代码生成 — JSON/XML `(/ops/codegen/jsonxmlGenCode)`

#### `POST /ops/codegen/jsonxmlGenCode/genCode`

从 JSON 或 XML 文本生成单个 Java VO 类。

**请求体** `JsonXmlRequestParam`：

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| `textType` | String | 是 | `json` 或 `xml` |
| `text` | String | 是 | 原始 JSON 或 XML 内容 |
| `takeSwagger` | boolean | 否 | 是否添加 `@Schema` 注解，默认 false |

**响应**：`ResponseData<String>`（Java 源代码字符串）

---

#### `POST /ops/codegen/jsonxmlGenCode/downloadCode`

上传包含多个 JSON/XML 文件的 ZIP，批量生成 Java VO 类并返回 ZIP。

**请求**：`multipart/form-data`，文件字段名 `file`（`.zip` 格式）

**查询参数**：

| 参数 | 类型 | 说明 |
|---|---|---|
| `textType` | String | `json` 或 `xml` |
| `takeSwagger` | boolean | 是否附加 `@Schema` 注解 |

**响应**：`application/zip` 文件流

---

### 7.3 代码生成 — Swagger/OpenAPI `(/ops/codegen/swaggerGenCode)`

#### `GET /ops/codegen/swaggerGenCode/downloadCodeForVue3`

解析 OpenAPI 文档，生成 Vue3 前端代码 ZIP。

| 参数 | 类型 | 必填 | 说明 |
|---|---|---|---|
| `templateGroupId` | long | 是 | 模板分组 ID（类型需为 SWAGGER） |
| `swaggerUrl` | String | 是 | OpenAPI 文档 URL，多个以英文逗号分隔 |

**响应**：`application/zip` 文件流

---

#### `GET /ops/codegen/swaggerGenCode/downloadCodeForJmeter`

解析 OpenAPI 文档，生成 JMeter 压测脚本 ZIP。

| 参数 | 类型 | 必填 | 说明 |
|---|---|---|---|
| `templateGroupId` | long | 是 | 模板分组 ID（类型需为 SWAGGER） |
| `swaggerUrl` | String | 是 | OpenAPI 文档 URL，多个以英文逗号分隔 |

**响应**：`application/zip` 文件流

---

### 7.4 模板分组管理 `(/ops/template/group)`

| 方法 | 路径 | 权限级别 | 说明 |
|---|---|---|---|
| GET | `/ops/template/group/list` | PERM | 分页查询模板分组列表 |
| GET | `/ops/template/group/liteList` | USER | 轻量列表（用于下拉选择控件） |
| GET | `/ops/template/group/load` | PERM | 按 ID 加载单条记录 |
| GET | `/ops/template/group/listDataHistory` | PERM | 查询数据变更历史 |
| GET | `/ops/template/group/listCritLog` | PERM | 查询操作审计日志 |
| POST | `/ops/template/group/save` | PERM + CRIT | 新建模板分组 |
| PUT | `/ops/template/group/update` | PERM + CRIT | 更新模板分组 |
| PUT | `/ops/template/group/enable` | PERM + CRIT | 启用模板分组 |
| PUT | `/ops/template/group/disable` | PERM + CRIT | 禁用模板分组 |
| DELETE | `/ops/template/group/delete` | PERM + CRIT | 软删除模板分组（需先禁用） |

**`CodeTemplateGroup` 请求/响应字段：**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | long | 主键（保存时无需传入，由序列生成） |
| `groupType` | int | 1=数据库类型，2=Swagger 类型 |
| `groupName` | String | 分组名称（最长 100 字符） |
| `groupDesc` | String | 分组描述（最长 100 字符） |
| `state` | int | 1=启用，0=禁用，-1=已删除 |

---

### 7.5 模板内容管理 `(/ops/template/info)`

| 方法 | 路径 | 权限级别 | 说明 |
|---|---|---|---|
| GET | `/ops/template/info/list` | PERM | 分页查询模板列表 |
| GET | `/ops/template/info/liteList` | USER | 轻量列表（不含 `templateBody` 字段） |
| GET | `/ops/template/info/load` | PERM | 按 ID 加载单条记录 |
| GET | `/ops/template/info/listDataHistory` | PERM | 查询数据变更历史 |
| GET | `/ops/template/info/listCritLog` | PERM | 查询操作审计日志 |
| POST | `/ops/template/info/save` | PERM + CRIT | 新建模板 |
| PUT | `/ops/template/info/update` | PERM + CRIT | 更新模板（自动重建 FreeMarker 缓存） |
| PUT | `/ops/template/info/enable` | PERM + CRIT | 启用模板 |
| PUT | `/ops/template/info/disable` | PERM + CRIT | 禁用模板 |
| DELETE | `/ops/template/info/delete` | PERM + CRIT | 软删除模板（需先禁用） |

**`CodeTemplateInfo` 请求/响应字段：**

| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | long | 主键 |
| `groupId` | long | 所属分组 ID |
| `templateType` | int | 模板类型（见下表） |
| `templateName` | String | 模板名称 |
| `templateDesc` | String | 模板描述 |
| `templateFilename` | String | 输出文件名 FreeMarker 模板（如 `${entityName}.java`） |
| `templateBody` | String | FreeMarker 代码模板内容 |
| `state` | int | 1=启用，0=禁用，-1=已删除 |

**`templateType` 枚举值：**

| 值 | 枚举名 | 说明 |
|---|---|---|
| 10 | `DB_COMMON` | 数据库通用模板 |
| 11 | `DB_ENTITY` | 数据库实体类模板 |
| 12 | `DB_CONTROLLER` | 数据库 Controller 模板 |
| 13 | `DB_DTO` | 数据库 DTO/QueryParam 模板 |
| 20 | `VUE_COMMON` | Vue3 通用模板 |
| 21 | `VUE_API` | Vue3 API 客户端模板 |
| 22 | `VUE_ROUTER` | Vue3 Router 配置模板 |
| 23 | `VUE_PAGE` | Vue3 页面组件模板 |
| 24 | `VUE_I18N` | Vue3 国际化模板 |
| 31 | `JMETER` | JMeter 测试脚本模板 |

---

### 7.6 操作日志 `(/ops/log)`

#### `GET /ops/log/critLog/list`

分页查询关键操作日志（`SysCritLog`）。

#### `GET /ops/log/dataHistory/list`

分页查询数据变更历史（`SysDataHistory`）。

---

### 7.7 调试接口（仅 debug/dev profile）

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/open/dao/list` | 测试查询 `CodeTemplateGroup` 列表 |
| GET | `/open/dao/load?id={id}` | 测试按 ID 加载 `CodeTemplateInfo` |
| GET | `/open/enum/getAllEnumMap` | 获取所有枚举类型映射 |
| GET | `/open/test/echo?date={date}` | 回显日期参数（测试反序列化） |
| GET | `/open/test/now` | 返回当前服务器时间戳 |
| GET | `/open/test/timezone` | 返回 JVM 默认时区 |
| GET | `/open/test/exception` | 触发测试异常（验证错误处理） |

---

## 8. 数据库设计

执行 `database/ddl.sql` 初始化以下表：

### `code_template_group` — 模板分组

```sql
CREATE TABLE `code_template_group` (
  `id`          BIGINT       NOT NULL COMMENT '唯一ID',
  `group_type`  INT          NOT NULL DEFAULT 0 COMMENT '分组类型 1-数据库 2-Swagger',
  `group_name`  VARCHAR(100) NOT NULL DEFAULT '' COMMENT '分组名称',
  `group_desc`  VARCHAR(100) NOT NULL DEFAULT '' COMMENT '分组描述',
  `create_date` DATETIME(3)  NOT NULL COMMENT '创建时间',
  `modify_date` DATETIME(3)  NOT NULL COMMENT '更新时间',
  `state`       INT          NOT NULL DEFAULT 1 COMMENT '状态 1-启用 0-禁用 -1-删除',
  PRIMARY KEY (`id`)
);
```

### `code_template_info` — 模板内容

```sql
CREATE TABLE `code_template_info` (
  `id`                BIGINT       NOT NULL COMMENT '唯一ID',
  `group_id`          BIGINT       NOT NULL DEFAULT 0 COMMENT '分组ID',
  `template_type`     INT          NOT NULL DEFAULT 0 COMMENT '模板类型（10-13 DB, 20-24 Vue, 31 JMeter）',
  `template_name`     VARCHAR(100) NOT NULL DEFAULT '' COMMENT '模板名称',
  `template_desc`     VARCHAR(100) NOT NULL DEFAULT '' COMMENT '模板描述',
  `template_filename` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '输出文件名模板（FreeMarker）',
  `template_body`     LONGTEXT     NOT NULL COMMENT '代码体模板（FreeMarker）',
  `create_date`       DATETIME(3)  NOT NULL COMMENT '创建时间',
  `modify_date`       DATETIME(3)  NOT NULL COMMENT '更新时间',
  `state`             INT          NOT NULL DEFAULT 1 COMMENT '状态 1-启用 0-禁用 -1-删除',
  PRIMARY KEY (`id`)
);
```

### `sys_crit_log` — 关键操作日志

记录 CRIT 级别的操作审计信息，包含：用户信息（`saas_id`、`mch_id`、`user_id`、`user_type`、`user_ip`、`user_name`等）、接口信息（`api_uri`、`api_name`）、业务信息（`biz_type`、`biz_id`、`biz_log`）、完整请求/响应体，以及响应耗时和应用部署信息。

### `sys_data_history` — 数据变更历史

记录每次实体 save/update 的字段级变更：用户信息、实体类名+ID+名称、完整实体数据快照（JSON）、字段级变更摘要，以及备注和时间戳。

### `sys_seq` — 序列表

`uw-dao` 框架用于生成序列 ID，字段：`seq_name`、`seq_id`、`seq_desc`、`increment_num`、`create_date`、`last_update`。

---

## 9. 模板系统

### 9.1 FreeMarker 模板缓存

服务使用 `FreeMarker StringTemplateLoader` 将所有已启用（`state=1`）的模板从数据库加载到内存。每条 `CodeTemplateInfo` 记录注册两个 key：

- `{id}#filename` → 对应 `templateFilename` 字段（控制输出文件名）
- `{id}#body` → 对应 `templateBody` 字段（控制代码内容）

### 9.2 模板上下文变量（数据库生成）

在数据库驱动代码生成时，FreeMarker 模板可使用以下上下文变量：

| 变量 | 类型 | 说明 |
|---|---|---|
| `tableInfo` | `MetaTableInfo` | 表元信息（tableName、entityName、remarks 等） |
| `columnList` | `List<MetaColumnInfo>` | 字段列表 |
| `pkList` | `List<MetaPrimaryKeyInfo>` | 主键列表 |

`MetaColumnInfo` 字段：

| 字段 | 说明 |
|---|---|
| `columnName` | 原始列名（下划线格式） |
| `fieldName` | Java 字段名（camelCase） |
| `javaType` | Java 类型字符串（如 `String`、`long`、`java.util.Date`） |
| `remarks` | 列注释 |
| `isPk` | 是否主键 |
| `isNullable` | 是否允许为 null |

### 9.3 模板上下文变量（Swagger 生成）

在 Swagger/OpenAPI 驱动代码生成时，FreeMarker 模板可使用以下上下文变量：

| 变量 | 类型 | 说明 |
|---|---|---|
| `schemaInfoList` | `List<SchemaInfo>` | DTO/模型定义列表 |
| `apiInfoList` | `List<ApiInfo>` | 全量接口列表 |
| `apiGroupInfoList` | `List<ApiGroupInfo>` | 按父路径分组的接口组 |
| `apiCatalogInfoList` | `List<ApiCatalogInfo>` | 顶层菜单目录列表 |

### 9.4 模板热更新

调用 `PUT /ops/template/info/update` 成功后，服务端会自动触发 `TemplateHelper.init()`，重新从数据库加载所有模板到 FreeMarker 缓存，**无需重启服务**即可使模板变更生效。

### 9.5 $PackageInfo$ 约定

在 `controller/ops/` 的每个子包下存在一个 `$PackageInfo$.java` 文件，其中包含一个空的 `GET` 接口（如 `GET /ops/codegen`），用于在权限/导航系统中注册父级菜单节点，遵循 UW 平台的菜单权限体系约定。

---

## 10. 构建与部署

### 10.1 Maven 构建

```bash
# 完整构建（跳过测试）
mvn clean package -Dmaven.test.skip=true

# 完整构建（含测试）
mvn clean package
```

构建产物 `target/uw-code-center-{version}.jar` 为 Spring Boot 可执行 Fat JAR（已启用分层 JAR）。

### 10.2 Docker 构建

```bash
# 构建镜像
docker build -t uw-code-center:1.1.6 .

# 运行容器（最小配置）
docker run -d \
  -p 10050:10050 \
  -e NACOS_SERVER=192.168.1.10:8848 \
  -e NACOS_USERNAME=nacos \
  -e NACOS_PASSWORD=nacos888 \
  -e NACOS_NAMESPACE=prod \
  --name uw-code-center \
  uw-code-center:1.1.6
```

Dockerfile 采用**多阶段构建**：
1. Stage 1（`builder`）：使用 `bellsoft/liberica-openjre-debian:25-cds` 提取分层 JAR
2. Stage 2：将分层依赖（`dependencies/`、`spring-boot-loader/`、`snapshot-dependencies/`、`application/`）分层 COPY，充分利用 Docker 构建缓存
3. 入口点支持通过 `JAVA_OPTS` 和 `SPRING_OPTS` 注入运行时参数

### 10.3 CI/CD（Gitea Actions）

触发条件：向 `main` 分支 push

流水线步骤：
1. 检出代码
2. `mvn clean package -U -Dmaven.test.skip=true`
3. 从 `pom.xml` 提取版本号
4. `docker build` 并推送到私有镜像仓库（`REGISTRY_SERVER` 变量）
5. 执行部署脚本 `deploy-registry-app.sh uw-code-center {VERSION} 10050`

---

## 11. 常见问题解答

**Q：模板更新后未生效怎么办？**

A：调用 `PUT /ops/template/info/update` 接口成功后，服务端会自动重新加载 FreeMarker 缓存。如果仍未生效，检查：
- 模板的 `state` 是否为 `1`（启用状态）
- 服务端日志是否有 `TemplateHelper.init()` 执行记录
- 极端情况下重启服务可强制重建缓存

---

**Q：如何添加新的数据库连接？**

A：数据库连接通过 `uw-dao` 框架的连接池管理配置，连接池名称（`connName`）在 Nacos 配置中心的 `uw-code-center` 配置文件中定义，无需修改代码。添加新连接后，`/ops/codegen/databaseGenCode/list` 接口会自动包含新的连接名称。

---

**Q：Oracle 元数据读取失败怎么排查？**

A：`OracleDataMetaImpl` 依赖以下 Oracle 系统视图权限：
- `ALL_TABLES`
- `USER_TAB_COMMENTS`
- `USER_COL_COMMENTS`
- `ALL_CONS_COLUMNS`

确认连接的数据库用户具有上述视图的查询权限。

---

**Q：批量生成代码时 ZIP 内容为空？**

A：请检查：
1. `templateGroupId` 对应的模板分组是否有已启用（`state=1`）的模板
2. 指定的 `filterTableNames` 是否与数据库中实际表名大小写匹配
3. FreeMarker 模板语法是否有错误（查看服务端日志）

---

**Q：JSON/XML 生成 VO 时字段命名不正确？**

A：`VoCodeGenTools` 自动将下划线字段名转为 camelCase。如果原始 JSON 本身已是 camelCase，则直接使用。当使用 Jackson2 注解风格时，会自动在 `@JsonProperty` 中保留原始名称。

---

**Q：Swagger 生成代码时 `$-package-info-$` 是什么？**

A：这是 UW 平台特有的约定标记。在 OpenAPI 文档的某些路径下若存在该标记，`SwaggerParser` 会将其识别为菜单目录节点（`ApiCatalogInfo`），用于在前端代码中生成对应的菜单/导航结构，而不是生成普通 API 接口代码。

---

**Q：本地开发时 debug profile 的接口访问不到？**

A：确认启动参数包含 `-Dspring.profiles.active=debug` 或 `-Dspring-boot.run.profiles=debug`。`/open/**` 接口通过 `@ConditionalOnExpression` 注解，仅在 `debug` 或 `dev` Profile 下注册为 Bean。

---

**Q：服务注册到 Nacos 时 IP 显示不正确？**

A：通过环境变量 `APP_HOST` 指定期望对外暴露的 IP 地址：
```bash
docker run -e APP_HOST=10.0.1.100 ...
```
不设置时，Nacos 客户端会自动探测，可能在多网卡环境下选择错误。
