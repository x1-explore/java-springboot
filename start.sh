#!/bin/bash

# 设置环境变量
export MYSQL_HOST=localhost
export MYSQL_PORT=3306
export MYSQL_DATABASE=admin_system
export MYSQL_USER=admin_user
export MYSQL_PASSWORD=admin123
export PORT=8080

# 创建日志目录
mkdir -p logs

# 启动应用
java -jar admin-system-1.0-SNAPSHOT.jar 