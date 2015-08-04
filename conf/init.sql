use clustermonitor;
insert into clusters value("cluster1","192.77.108.22");
insert into clusters value("cluster2","192.77.108.239");
insert into users value("gsfan","123456","ordinary", "cluster1");
insert into users value("root","123456","superuser", "cluster2");
insert into hosts value("225","192.77.108.225","cluster1");
insert into hosts value("226","192.77.108.226","cluster1");
insert into hosts value("227","192.77.108.22","cluster1");
insert into hosts value("228","192.77.108.228","cluster1");
insert into hosts value("229","192.77.108.229","cluster1");
insert into hosts value("230","192.77.108.230","cluster1");
insert into hosts value("231","192.77.108.231","cluster1");
insert into hosts value("130","192.77.108.130","cluster2");
insert into hosts value("131","192.77.108.131","cluster2");
insert into hosts value("132","192.77.108.132","cluster2");
insert into hosts value("133","192.77.108.133","cluster2");


