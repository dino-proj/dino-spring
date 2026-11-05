<div align="center">
  <div><img src="./.assert/intro.svg" style="max-height:300px"  /></div>
</div>

## 🦖 Dino Spring Framework

Spring Boot是非常优秀的Java后端开发框架，其解决了很多重复开发的问题，能做到对各种常用组件的自动配置。其解决了基础依赖问题。

然而，在很多项目开发中，存在大量功能代码复用问题，Dino Spring希望从这个层面解决问题，提升业务功能的开发速度和开发质量。

Dino Spring用于快速构建前后端分离应用，支持微服务，多租户，模块化设计.

## 🎯 开发主张

- **目标：** 提高10倍开发速度；降低10倍Bug量；避免复制粘贴。

- **模块化结构：** 按照模块划分而非按照Java Class类型划分，同一个功能下的Entity、VO、Service、Repository位于同一目录结构下。

- **非必要不定义接口：** 非必要情况下，不采用Interface-Implement方式，因为大部分业务功能没有定义接口的必要，无需浪费开发时间。

- **开发不写文档：** 开发不应该再写文档，rest接口文档应在代码中完成，无需再写接口文档，前后端应尽量避免沟通，一切以rest接口文档为准。如果rest接口不够详尽明确，后端应修改代码，完善接口描述。

## 💡 版本说明
- **Java JDK:** >=17
- **Spring Framework:** 6.0.11
- **Spring Boot:** 3.1.2
- **Spring Cloud:** 2022.0.3

## ✨ 内置模块

### 🦖 dino-spring-common

### 🦖 dino-spring-data

### 🦖 dino-spring-auth

### 🦖 dino-spring-core
## 🚀 快速开始

[详细开发文档](https://dinodev.cn/dino-spring/)https://dinodev.cn/dino-spring/

java spring项目maven pom.xml配置如下：
```xml
<project>
  <parent>
    <groupId>org.dinospring</groupId>
    <artifactId>dino-spring-boot-starter-parent</artifactId>
    <version>3.0.5</version>
  </parent>
  <groupId>your group id</groupId>
  <artifactId>your artifact id</artifactId>

  <dependencies>
    <dependency>
      <groupId>org.dinospring</groupId>
      <artifactId>dino-spring-core</artifactId>
      <version>${dino-spring.version}</version>
    </dependency>
  <dependencies>
</project>
```
## 发布到 Maven Central

OSSRH 已于 2025-06-30 关闭，发布使用 [Central Portal](https://central.sonatype.com/)
和 `central-publishing-maven-plugin`，不再使用旧的 `s01.oss.sonatype.org` 地址。

运行 `.github/workflows/release.yml` 前，在仓库 Actions secrets 中配置：

- `NEXUS_USERNAME` / `NEXUS_PASSWORD`：名称保留兼容现有配置，值必须替换为
  [Central Portal user token](https://central.sonatype.org/publish/generate-portal-token/)
  的 username / password，不是旧 OSSRH 凭据或登录密码。
- `GPG_PRIVATE_KEY`：用于签名的 ASCII-armored 私钥；对应公钥需发布到公共 keyserver。
- `GPG_PASSPHRASE`：私钥口令（无口令时可留空）。

确认 token 所属账号有 `cn.dinodev` 和 `org.dinospring` 两个 namespace 的发布权限。
不再需要 `MVN_SETTINGS_SECURITY`。

`dino-dependencies-root` 的 Maven groupId 从 `org.dino` 迁移到 `cn.dinodev`，
其他模块保留 `org.dinospring`，Java 包名及 API 不变。使用 boot parent 的项目
无需修改坐标；直接导入该 BOM 的项目只需将其 groupId 改为 `cn.dinodev`：

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>cn.dinodev</groupId>
      <artifactId>dino-dependencies-root</artifactId>
      <version>3.0.5</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

推送 `RELEASE-*` tag 会自动发布并等待 `published`；手动运行时可关闭 `autoPublish`，
此时 `waitUntil` 必须选择 `uploaded` 或 `validated`，之后在 Portal 中手动发布。
已经发布的相同版本不可覆盖；重试时跳过已发布模块，或使用新版本。

本地发布使用 `mvn deploy -P publish`，默认等待验证完成后在 Portal 手动发布。
Maven `settings.xml` 中的 server id 必须是 `central`，凭据使用 Portal token。
自动发布可追加 `-Dcentral.autoPublish=true -Dcentral.waitUntil=published`。
三个独立项目定义发布 profile，其余模块从 boot parent 继承；源码、Javadoc 和签名
由发布 profile 生成。
assembly 是纯资源模块，其 Javadoc JAR 包含本 README，源码 JAR 包含脚本和配置资源。
现有 Java 注释存在格式问题，发布 profile 关闭 doclint 格式检查，但 Javadoc 生成失败仍会终止构建。

## 📄 License

dino-spring is an open source software licensed as [Apache-2.0](./LICENSE).

## 🫶 Citation
If you find our work useful for your research, please consider citing the paper:

```bibtex
@misc{Dino-spring,
  author = {Cody Lu},
  title = {Dino-spring},
  year = {2023},
  publisher = {GitHub},
  journal = {GitHub Repository},
  howpublished = {\url{https://github.com/dino-proj/dino-spring}}
}