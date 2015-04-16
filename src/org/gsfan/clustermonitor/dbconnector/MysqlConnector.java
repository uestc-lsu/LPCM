package org.gsfan.clustermonitor.dbconnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.gsfan.clustermonitor.mainfram.WarnMsgDialog;

public class MysqlConnector {
	
	private Connection connection = null;
	
	private String mysqlLoc = null;
	
	private static final String USERNAME = "gsfan";
	private static final String PASSWD = "0620631FGS";
	
	private static final String dbDriver = "com.mysql.jdbc.Driver";
//	private static final String dbURL = "jdbc:mysql://localhost:3306/clusteruser";//使用主机IP会出错，这是为什么？
	
//	private static final String dbURL = "jdbc:mysql://192.77.108.249:3307/clustermonitor";//windows下使用的端口为3307
	private String dbURL = null;
	public MysqlConnector(){
		
		ConfigureFileReader reader = new ConfigureFileReader("./conf/configure.txt");
		reader.readMysqlLocFromConfFile();
		mysqlLoc = reader.getMysqlLocation();
		
		dbURL = "jdbc:mysql://"+mysqlLoc+"/clustermonitor";
		
		try {
			Class.forName(dbDriver) ;
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
		
			connection = DriverManager.getConnection(dbURL, USERNAME, PASSWD);
			
////			sql = "select * from clusteruser";
//			sql = "select * from student";
////			preStatement = connection.prepareStatement(sql);
//			preStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//			resultSet = preStatement.executeQuery();
//			
//			while(resultSet.next()){//没有resultSet.next()会出现异常
//				System.out.println("userName = "+resultSet.getString(2)+"\t password = "+resultSet.getString(3));
//			}
		} catch (SQLException e) {

			WarnMsgDialog msgDialog = new WarnMsgDialog("Connect to Mysql failure!", 5);
			Thread thread = new Thread(msgDialog);
			thread.start();
//			System.out.println("Connect to Mysql failure!");
//			System.exit(0);
		}
		
	}
	
	public Connection getConnection(){
		return connection;
	}
}
