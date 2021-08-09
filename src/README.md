# PreSetting  

## Docker MySQL 설치
```shell
$ docker run -d --name test_mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=rootpwd mysql:5.7 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
```

## MySQL Docker 컨테이너 접속
```shell
$ docker exec -ti 33de56fa7acd bash
root@33de56fa7acd:/# mysql -u root -p
Enter password:
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 9
Server version: 8.0.22 MySQL Community Server - GPL

Copyright (c) 2000, 2020, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> show databases;
| Database           |
+--------------------+
| information_schema |
| mysql              |
| performance_schema |
| sys                |
+--------------------+
4 rows in set (0.01 sec)

mysql> CREATE DATABASE testdb default CHARACTER SET UTF8;
```  
# 참고
## MySQL Docker 컨테이너 중지  
```shell
$ docker stop mysql-container
```  

## MySQL Docker 컨테이너 시작  
```shell
$ docker start mysql-container
```  

## MySQL Docker 컨테이너 재시작  
```shell
$ docker restart mysql-container
```  

