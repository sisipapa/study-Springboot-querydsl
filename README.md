# Springboot querydsl + docker mysql 예제입니다.

## PreSetting

### docker mysql 설치  
```shell
docker run -d --name test_mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=rootpwd mysql:5.7 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
```  


### docker mysql 접속 및 database 생성
```shell
C:\Users\user>docker ps -a
CONTAINER ID   IMAGE       COMMAND                  CREATED          STATUS          PORTS                                                  NAMES
5b591d0d3972   mysql:5.7   "docker-entrypoint.s…"   15 seconds ago   Up 13 seconds   0.0.0.0:3306->3306/tcp, :::3306->3306/tcp, 33060/tcp   test_mysql

C:\Users\user>docker exec -ti 5b591d0d3972 bash
root@5b591d0d3972:/# mysql -u root -p
Enter password:
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 2
Server version: 5.7.35 MySQL Community Server (GPL)

Copyright (c) 2000, 2021, Oracle and/or its affiliates.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| mysql              |
| performance_schema |
| sys                |
+--------------------+
4 rows in set (0.00 sec)

mysql> create database testdb;
Query OK, 1 row affected (0.00 sec)
```  

