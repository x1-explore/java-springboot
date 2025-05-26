@echo off
start javaw -jar target/admin-system-1.0-SNAPSHOT.jar
echo 程序已在后台启动，您可以关闭此窗口
timeout /t 3 