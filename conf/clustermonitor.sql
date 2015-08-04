drop DATABASE clustermonitor;
create database clustermonitor;
use clustermonitor;
create table clusters(cluster_name char(20) primary key, server_ip char(25));
create table users(user_name char(10) primary key, user_passwd char(10), user_type char(10), cluster_name char(20), foreign key (cluster_name) references clusters(cluster_name));
create table hosts(host_name char(10), host_ip char(25), cluster_name char(20), foreign key (cluster_name) references clusters(cluster_name));
Create table nodeinfo(host_name char(10), cluster_name char(20), storage_time char(10), cpuinfo char(100), diskinfo char(100), memoryinfo char(100), networkinfo char(100));