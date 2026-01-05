# Dino Spring — AI Agent Coding Instructions

## 项目架构与核心模块
- Dino Spring 是一个模块化的 Spring Boot 微服务框架，支持多租户和前后端分离，目标是提升 10 倍开发速度。
- **核心设计原则**：同一功能同一目录（Entity、VO、Service、Repository 位于同一包下），非必要不定义接口，REST 文档由代码自动生成。

### 主要模块职责
- `dino-spring-core/`：核心业务服务（租户、认证、IAM、OSS、SMS 等），包含 `sys/` 和 `modules/` 子目录
- `dino-spring-auth/`：JWT 认证与权限管理，集成 Spring Security
- `dino-spring-commons/`：通用工具（`DinoContext`、`ContextHelper`、响应对象、类型转换）
- `dino-spring-data/`：数据访问层基础（`CrudRepositoryBase`、`EntityBase`、租户实体接口）
- `dino-spring-assembly/`：项目聚合与打包配置
- `dino-dependencies-root/`：统一依赖版本管理（BOM）
- `dino-spring-boot-starter-parent/` 和 `dino-spring-cloud-starter-parent/`：Spring Boot/Cloud 父 POM

### 模块结构示例
```
dino-spring-core/src/main/java/cn/dinodev/spring/core/sys/tenant/
├── TenantEntity.java          # 实体类
├── TenantVo.java              # 视图对象
├── TenantService.java         # 服务接口（默认方法实现）
├── TenantRepository.java      # 数据仓库接口
├── TenantControllerBase.java  # 控制器基类接口（默认方法实现）
└── impl/
    └── TenantServiceImpl.java # 服务实现（仅在需要时）
```

## 关键开发流程
- **构建**：`mvn clean install`（根目录或单模块）
- **测试**：`mvn test`
- **文档生成**：基于 Springdoc（Swagger3）自动生成 REST API 文档
- **JDK 要求**：最低版本 17，推荐 21（父 POM 默认 Java 21）
- **Spring 版本**：
  - Spring Boot: 3.5.7
  - Spring Framework: 6.2.12
  - Spring Cloud: 2025.0.0

## 多租户架构（核心特性）
### 上下文管理
- `DinoContext` 接口：定义当前用户和租户的访问接口
- `DinoContextThreadLocalImpl`：基于 `InheritableThreadLocalMap` 的 ThreadLocal 实现
- `ContextHelper`：静态辅助类，提供 `currentTenant()` 和 `currentUser()` 等方法

### 租户支持机制
- **行级租户**（`TenantRowEntity`）：实体实现 `getTenantId()/setTenantId()`，数据通过 `tenant_id` 列隔离
- **表级租户**（`TenantTableEntity`）：每个租户使用独立表或 Schema
- **租户级别**（`TenantLevel`）：枚举定义租户隔离级别
- **租户拦截器**（`TenantSupportInterceptor`）：从 URI 路径变量 `{tenant_id}` 提取租户并注入上下文
- **租户参数解析器**（`TenantArgumentResolver`）：自动注入 `Tenant` 类型的 Controller 参数

### 典型用法
```java
// 在 Service 中获取当前租户
String tenantId = ContextHelper.currentTenantId();
Tenant tenant = ContextHelper.currentTenant();

// Controller 自动注入
@GetMapping("/{tenant_id}/data")
public Response<Data> getData(Tenant tenant) { // 自动解析
    // tenant 已由 TenantArgumentResolver 注入
}
```

## 项目约定与模式
### 代码组织
- **功能内聚**：同一业务功能的所有类（Entity/VO/Service/Repository/Controller）位于同一包下
- **接口可选**：Service 和 Repository 优先定义为接口（支持默认方法），实现类放在 `impl/` 子包
  - 示例：`TenantService` 接口包含默认方法实现，`TenantServiceImpl` 仅覆盖 `repository()` 方法
- **ControllerBase 模式**：Controller 通常定义为接口（如 `TenantControllerBase`），包含默认方法实现 REST 端点
  - 避免代码重复，使用 `@GetMapping`/`@PostMapping` 等注解在接口中定义
  - 实现类通常为空或仅覆盖特定方法

### 命名规范
- Entity：`*Entity.java`（继承 `EntityBase<K>`）
- VO：`*Vo.java`（继承 `VoBase<K>` 或 `VoImplBase<K>`）
- Service：`*Service.java`（接口，继承 `Service<E, K>` 或 `ListServiceBase<E, K>`）
- Repository：`*Repository.java`（接口，继承 `CrudRepositoryBase<T, K>`）
- Controller：`*ControllerBase.java`（接口，继承 `ControllerBase<S, E, VO, K>`）

### 文档与注释
- 所有公共类、接口和方法需包含 Javadoc
- REST 接口使用 `@Operation` 和 `@Parameter` 注解（Swagger3）
- 版权声明：`// Copyright 2024 dinosdev.cn.` + `// SPDX-License-Identifier: Apache-2.0`

### 数据访问模式
- 使用 Spring Data JDBC（非 JPA）
- Repository 接口继承 `CrudRepositoryBase<T, K>`，扩展 `JdbcSelectExecutor<T, K>`
- 支持链式 SQL 构建（通过 `dino-sql-builder`，见 `newSelect()` 方法）
- 示例：
  ```java
  default List<TenantEntity> findBindTenants(String appClientId) {
      var sql = newSelect(TenantEntity.class, "t");
      sql.column("t.*").join("sys_app_client_tenant_rel", "r", "t.id=r.tenant_id")
         .eq("r.app_client_id", appClientId);
      return this.queryList(sql, TenantEntity.class);
  }
  ```

## 重要文件与目录
- `dino-spring-boot-starter-parent/pom.xml`：父 POM，定义依赖版本（Java 21、Spring Boot 3.5.7）
- `dino-dependencies-root/pom.xml`：BOM，管理所有模块版本
- `dino-spring-core/src/main/java/cn/dinodev/spring/core/`：
  - `autoconfig/`：自动配置类（`WebMvcConfig`、`TenantArgumentResolver`）
  - `sys/tenant/`：租户模块（Entity、Service、Repository、ControllerBase）
  - `modules/`：业务模块（iam、oss、sms、login 等）
- `dino-spring-commons/src/main/java/cn/dinodev/spring/commons/`：
  - `context/`：上下文管理（`DinoContext`、`ContextHelper`）
  - `response/`：统一响应对象（`Response`、`Status`）
  - `sys/`：系统接口（`Tenant`、`User`）
- `dino-spring-data/src/main/java/cn/dinodev/spring/data/`：
  - `dao/`：Repository 基类（`CrudRepositoryBase`、`JdbcSelectExecutor`）
  - `domain/`：实体基类（`EntityBase`、`TenantRowEntity`、`TenantTableEntity`）

## 扩展与集成
### 新增业务模块
1. 在 `dino-spring-core/src/main/java/cn/dinodev/spring/core/modules/` 下创建模块包
2. 定义 Entity（继承 `EntityBase`）、VO（继承 `VoImplBase`）
3. 定义 Repository（继承 `CrudRepositoryBase`）、Service（继承 `Service` 或 `ServiceBase`）
4. 定义 ControllerBase 接口（继承 `ControllerBase`），使用默认方法实现 REST 端点
5. 如需多租户支持，Entity 实现 `TenantRowEntity` 或标记 `TenantTableEntity`

### 自定义拦截器
- 在 `WebMvcConfig` 中添加拦截器（参考 `TenantSupportInterceptor`）
- 使用 `ContextHelper.setDinoContext()` 设置自定义上下文

### SQL 构建
- 使用 `dino-sql-builder` 进行类型安全的 SQL 构建
- Repository 中调用 `newSelect()` 创建 `SelectSqlBuilder` 实例
- 支持 WHERE、JOIN、GROUP BY、ORDER BY、LIMIT/OFFSET 等子句

## 其他说明
- **许可证**：Apache-2.0
- **官方文档**：https://dinodev.cn/dino-spring/
- **代码仓库**：https://github.com/dino-proj/dino-spring
- **依赖特性**：集成 Druid、Springdoc、MapStruct、Lombok、Weixin-Java-SDK 等
- **测试框架**：JUnit 5 + Spring Boot Test

---
如需补充项目约定、架构细节或模块功能，请提 Issue 或 PR。
