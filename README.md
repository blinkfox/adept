# Adept

[![Build Status](https://secure.travis-ci.org/blinkfox/adept.svg)](https://travis-ci.org/blinkfox/adept) [![codecov](https://codecov.io/gh/blinkfox/adept/branch/master/graph/badge.svg)](https://codecov.io/gh/blinkfox/adept)

## 简介

Adept是一个用于简化JDBC操作的轻量级DAO工具库。如果你不想在例的项目中使用庞大的Hibernate等ORM工具，只是想更简单、灵活而又方便的使用原生的JDBC来操作数据库，那么Adept也许是一个不错的选择。

相对于JDBC原生繁琐的API，Adept大大简化了增删改查(CRUD)的常用操作。为了更大程度的增强数据库连接和数据库操作的性能，默认内置了Java界最快的数据库连接池[HikariCP][1]，当然你也可以根据项目的实际情况，引入和切换为其他数据库连接池，如：[Druid][2]、[Commons Pool][3]、[C3P0][4]等。

  [1]: http://brettwooldridge.github.io/HikariCP
  [2]: https://github.com/alibaba/druid
  [3]: http://commons.apache.org/proper/commons-pool/index.html
  [4]: http://www.mchange.com/projects/c3p0/