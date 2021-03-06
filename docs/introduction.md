## Adept简介

[![Build Status](https://secure.travis-ci.org/blinkfox/adept.svg)](https://travis-ci.org/blinkfox/adept) [![codecov](https://codecov.io/gh/blinkfox/adept/branch/master/graph/badge.svg)](https://codecov.io/gh/blinkfox/adept) [![Maven Central](https://img.shields.io/maven-central/v/com.blinkfox/adept.svg)](http://search.maven.org/#artifactdetails%7Ccom.blinkfox%7Cadept%7C1.0.0%7Cjar) [![Javadocs](http://www.javadoc.io/badge/com.blinkfox/adept.svg)](http://www.javadoc.io/doc/com.blinkfox/adept)

Adept是一个用于简化JDBC操作的轻量级DAO工具库。如果你不想在例的项目中使用庞大的Hibernate等ORM工具，只是想更简单、灵活而又方便的使用原生的JDBC来操作数据库，那么Adept也许是一个不错的选择。

相对于JDBC原生繁琐的API，Adept大大简化了增删改查(CRUD)的常用操作。为了更大程度的增强数据库连接和数据库操作的性能，默认内置了Java界最快的数据库连接池[HikariCP][1]，当然你也可以根据项目的实际情况，引入和切换为其他数据库连接池，如：[Druid][2]、[Commons Pool][3]、[C3P0][4]等。

## 功能特性

- 轻量级，jar包仅仅39k大小，简单集成和使用
- API使用简单、灵活，支持链式调用
- 支持多种查询结果类型，支持映射为JavaBean
- 可自定义扩展其他结果类型和类型复用
- 支持快速的批量操作

  [1]: http://brettwooldridge.github.io/HikariCP
  [2]: https://github.com/alibaba/druid
  [3]: http://commons.apache.org/proper/commons-pool/index.html
  [4]: http://www.mchange.com/projects/c3p0/