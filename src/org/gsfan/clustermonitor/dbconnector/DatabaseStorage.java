package org.gsfan.clustermonitor.dbconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseStorage {
	
	private MysqlConnector connector = null;
	
	public DatabaseStorage() {
		connector = MysqlConnector.getInstance();
	}
	
	@SuppressWarnings("resource")
	public int storeInfoToClusterTable(String cluster, String IP) {
		String sql = "select * from clusters where cluster_name = ? ;";
		Connection connection = connector.getConnection();
		try {
			PreparedStatement preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, cluster);

			ResultSet resultSet = preStatement.executeQuery();

			while(resultSet.next()) {
				if(resultSet.getString(2).isEmpty()) {
					sql = "update clusters set server_ip=? where cluster_name=? ;";
					
					preStatement = connection.prepareStatement(sql);
					preStatement.setString(1, IP);
					preStatement.setString(2, cluster);

					preStatement.executeUpdate();

					return 1;
				}
				if(resultSet.isLast()) {
					return 1;
				}
			}
			
			sql = "insert into clusters value(?, ?);";
			preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, cluster);
			preStatement.setString(2, IP);

			preStatement.executeUpdate();
			return 1;
			
			
		} catch (SQLException e) {
			return 0;
		}
	}
	
	public int removeInfoFromClusterTable(String clusterName) {
		String sql = null;
		
		Connection connection = connector.getConnection();
		try {
			sql = "delete from users where cluster_name = ? ;";
			PreparedStatement preStatement = connection.prepareStatement(sql);
			preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, clusterName);
			preStatement.execute();
			
			sql = "delete from hosts where cluster_name = ? ;";
			preStatement = connection.prepareStatement(sql);
			preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, clusterName);
			preStatement.execute();
			
			sql = "delete from clusters where cluster_name = ? ;";
			preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, clusterName);
			preStatement.execute();
			return 1;	//success
		} catch (SQLException e) {
//			WarnMsgDialog msgDialog = new WarnMsgDialog(e.getMessage(), 10, false);
//			Thread thread = new Thread(msgDialog);
//			thread.start();
			return 0;	//failure
		}
		
	}
	
	public int storeInfoToUserTable(String user, String passwd, String userType, String cluster) {
		
		String sql = "insert into users value(?, ?, ?, ?);";
		Connection connection = connector.getConnection();
		//创建指针可以自由移动的结果集
		try {
			PreparedStatement preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, user);
			preStatement.setString(2, passwd);
			preStatement.setString(3, userType);
			preStatement.setString(4, cluster);
			
			preStatement.executeUpdate();
			return 1;	//success
		} catch (SQLException e) {
//			WarnMsgDialog msgDialog = new WarnMsgDialog(e.getMessage(), 10, false);
//			Thread thread = new Thread(msgDialog);
//			thread.start();
			return 0;	//failure
		}
	}
	
//	public void removeInfoFromUserTable(String user/*, String cluster*/) {
//
//	}
//	
	public int storeInfoToHostTable(String cluster, String hostIP) {
		String sql = "insert into hosts value(?, ?, ?);";
		Connection connection = connector.getConnection();
		int start = hostIP.lastIndexOf('.');
		String hostName = hostIP.substring(start+1);
		//创建指针可以自由移动的结果集
		try {
			PreparedStatement preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, hostName);
			preStatement.setString(2, hostIP);
			preStatement.setString(3, cluster);
			
			preStatement.executeUpdate();
			return 1;	//success
		} catch (SQLException e) {
//			WarnMsgDialog msgDialog = new WarnMsgDialog(e.getMessage(), 10, false);
//			Thread thread = new Thread(msgDialog);
//			thread.start();
			return 0;	//failue
		}
	}
	
	public int removeInfoFromHostTable(String clusterName, String hostIP) {
		String sql = null;
		
		Connection connection = connector.getConnection();
		try {
			//delete information from nodeinfo table before delete information from host
			sql = "delete from nodeinfo where host_name = (select host_name from hosts where host_ip = ? and cluster_name = ? ) ;";
			PreparedStatement preStatement = connection.prepareStatement(sql);
			preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, hostIP);
			preStatement.setString(2, clusterName);
			preStatement.execute();
			
			sql = "delete from hosts where host_ip = ? and cluster_name = ? ;";
//			sql = "delete from nodeinfo where host_name = (select host_name from host where host_ip = ?) ;";
//			preStatement = connection.prepareStatement(sql);
			preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, hostIP);
			preStatement.setString(2, clusterName);
			preStatement.execute();
			return 1;	//success
		} catch (SQLException e) {
//			WarnMsgDialog msgDialog = new WarnMsgDialog(e.getMessage(), 10, false);
//			Thread thread = new Thread(msgDialog);
//			thread.start();
			return 0;	//failure
		}
	}
	
	public int storeInfoToNodesinfoTable(String host, String cluster, String time, String cpuinfo, String diskinfo, String memoryinfo, String networkinfo) {
		String sql = "insert into hosts value(?, ?, ?, ?, ?, ?, ?);";
		Connection connection = connector.getConnection();
		//创建指针可以自由移动的结果集
		try {
			PreparedStatement preStatement = connection.prepareStatement(sql);
			preStatement.setString(1, host);
			preStatement.setString(2, cluster);
			preStatement.setString(3, time);
			preStatement.setString(4, cpuinfo);
			preStatement.setString(5, diskinfo);
			preStatement.setString(6, memoryinfo);
			preStatement.setString(7, networkinfo);
			
			preStatement.executeUpdate();
			return 1;	//success
		} catch (SQLException e) {
			return 0;	//failue
		}
	}
}
