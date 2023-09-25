# 🦖 Dino-spring Framework
<div align="center">
<a href="https://dinodev.cn"><img src="./.assert/logo.svg" width="76" height="60" alt="icon"/></a>

<div><img src="./.assert/intro.svg" width="600px" /></div>

<hr height="1" style="height: 1px; border-width: 0px 0 0 0 !important;"/>

用于快速构建前后端分离应用，支持微服务，多租户，模块化设计.
</div>

---

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
    <version>3.0.0</version>
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