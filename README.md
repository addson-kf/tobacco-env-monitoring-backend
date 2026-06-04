# Tobacco Environment Monitoring Backend

## 项目简介

本项目为烟田生态环境监测平台后端系统，基于 Spring Boot、PostgreSQL、MyBatis 开发，实现烟田监测数据管理、统计分析、Excel 数据导入以及地图可视化平台数据服务。

该仓库为个人学习与项目展示版本，已移除真实业务数据及生产环境配置。

---

## 技术栈

### Backend

* Java
* Spring Boot
* MyBatis
* JWT

### Database

* PostgreSQL

### Build Tool

* Maven

### Data Processing

* Excel Import

---

## 功能模块

### 用户管理

* 用户登录认证
* JWT权限控制

### 监测点管理

* 监测点信息维护
* 图片管理

### 土壤数据管理

* 土壤指标查询
* 条件筛选
* 数据统计

### 气象数据管理

* 气象数据维护
* 数据分析

### 烤烟评价管理

* 烤烟评价数据管理
* 指标查询统计

### 数据导入

* Excel批量导入
* 数据校验

### 数据统计分析

* 县区统计
* 指标分析
* 数据汇总

---

## 项目结构

```text
src/main/java/org/example

├── controller
├── service
├── mapper
├── model
├── dto
├── util
├── config
└── job
```

---

## 快速启动

### 1. 克隆项目

```bash
git clone https://github.com/yourname/tobacco-env-monitoring-backend.git
```

### 2. 创建数据库

```sql
CREATE DATABASE your_database;
```

### 3. 修改配置文件

复制示例配置文件：

```text
application-example.properties
```

修改数据库地址、用户名和密码。

### 4. 启动项目

运行：

```text
Main.java
```

或：

```bash
mvn spring-boot:run
```

---

## 项目亮点

* Spring Boot RESTful API开发
* PostgreSQL数据库管理
* MyBatis数据持久化
* Excel批量导入功能
* 多维度数据统计分析
* 支持地图可视化平台数据服务
* JWT用户认证与权限控制

---

## 项目截图

可在此处添加：

* 登录页面
* 数据管理页面
* 数据统计页面
* 地图可视化页面

---

## 声明

本项目仅用于学习交流与技术展示。

仓库中的数据库配置、密钥、业务数据及生产环境信息均已脱敏处理。
