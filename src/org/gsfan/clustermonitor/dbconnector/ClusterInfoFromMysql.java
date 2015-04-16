package org.gsfan.clustermonitor.dbconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;

public class ClusterInfoFromMysql {
	private Hashtable<String,String> clusters = new Hashtable<String,String>();
	private int clusterCount = 0;
	private MysqlConnector connector = null;
	
	public ClusterInfoFromMysql(){

		String sql = "select * from cluster ";
		try {
			this.connector = new MysqlConnector();
			Connection connection = connector.getConnection();
			//创建指针可以自由移动的结果集
			PreparedStatement preStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = preStatement.executeQuery();
			
			while(resultSet.next()) {
//				before change database
//				clusters.put(resultSet.getString(2), resultSet.getString(3));
//				after change database 
				clusters.put(resultSet.getString(1), resultSet.getString(2));
				this.clusterCount++;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
/*
 	public ClusterInfoFromMysql(String clusterName){

		String sql = "select * from cluster " + clusterName;
		try {
			this.connector = new MysqlConnector();
			Connection connection = connector.getConnection();
			//创建指针可以自由移动的结果集
			PreparedStatement preStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = preStatement.executeQuery();
			
			while(resultSet.next()) {
				clusters.put(resultSet.getString(2), resultSet.getString(3));
				this.clusterCount++;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
 * */
	public Hashtable<String,String> getClusters(){
		return this.clusters;
	}
	public int getClusterCount(){
		return this.clusterCount;
	}
	
	public Hashtable<String,String> getClusterHosts(String clusterName){
		Hashtable<String,String> hosts = new Hashtable<String,String>();
		String sql = "select * from host where cluster_name = ?" ;
		try {
			Connection connection = connector.getConnection();
			//创建指针可以自由移动的结果集
			PreparedStatement preStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			preStatement.setString(1, clusterName);
			
			ResultSet resultSet = preStatement.executeQuery();
			
			if(resultSet==null){
				return null;
			}
			while(resultSet.next()) {	
//				Before change database
//				hosts.put(resultSet.getString(2), resultSet.getString(3));
//				After change database
				hosts.put(resultSet.getString(1), resultSet.getString(2));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return hosts;
	}
	
	public static void main(String[] args) {
//		ClusterInfoFromMysql clusterInfo = new ClusterInfoFromMysql(MainFrame.currentCluster);
		ClusterInfoFromMysql clusterInfo = new ClusterInfoFromMysql();
		Hashtable<String,String> cluster = clusterInfo.getClusters();
		Hashtable<String,String> hosts = new Hashtable<String,String>();
		Iterator<String> iter = cluster.keySet().iterator();
		while(iter.hasNext()){
			String name = (String)iter.next();
			String ip = cluster.get(name);
			System.out.println(name+" "+ip);
			
			hosts = clusterInfo.getClusterHosts(name);
			if(hosts!=null){
				Iterator<String> iter2 = hosts.keySet().iterator();
				while(iter2.hasNext()){
					String hostName = (String)iter2.next();
					String hostIP = hosts.get(hostName);
					System.out.println("In cluster:"+name+"\nhost:\n\t"+hostName+" "+hostIP);
					
				}
			}
		}
	}
}
