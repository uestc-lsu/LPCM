package org.gsfan.clustermonitor.dbconnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.swing.JOptionPane;

public class MysqlConnector {
	
	private Connection connection = null;
	private String mysqlLoc = null;
//	private static final String USERNAME = "gsfan";
//	private static final String PASSWD = "0620631FGS";
//	private static final String dbURL = "jdbc:mysql://localhost:3306/clusteruser";//使用主机IP会出错，这是为什么？	
//	private static final String dbURL = "jdbc:mysql://192.77.108.249:3307/clustermonitor";//windows下使用的端口为3307
	
	private static final String dbDriver = "com.mysql.jdbc.Driver";
	private String dbURL = null;
	private String username = null;
	private String passwd = null;
	
	private static MysqlConnector instance = new MysqlConnector();
	
	private MysqlConnector(){
		
		Hashtable<String, String> table = ConfigureFileReader.getConfInfoTable();
		username = table.get("username");
		passwd = table.get("password");
		mysqlLoc = table.get("dblocation:port");
		
		dbURL = "jdbc:mysql://"+mysqlLoc+"/clustermonitor";
		
		try {
			Class.forName(dbDriver) ;
		} catch (ClassNotFoundException e1) {
			JOptionPane.showMessageDialog(null, dbDriver+"错误", "Error", JOptionPane.ERROR_MESSAGE);
//			System.exit(-1);
		}

		try {
//			connection = DriverManager.getConnection(dbURL, USERNAME, PASSWD);
			connection = DriverManager.getConnection(dbURL, username, passwd);

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "连接数据库失败：Connect to Mysql failure!", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	public static MysqlConnector getInstance() {
		return instance;
	}
	
	public Connection getConnection(){
		return connection;
	}
}
