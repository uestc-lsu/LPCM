package org.gsfan.clustermonitor.dbconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.gsfan.clustermonitor.mainframe.MainFrame;

public class ObtainUsersInformation {
	
	private MysqlConnector connector = null;
	
	public ObtainUsersInformation(){
		connector = MysqlConnector.getInstance();
	}
	
	public int userAuthorization(String name, String passwd){
		
		Connection connection = connector.getConnection();
		
		int loginSucess = 0;
		int passwdError = 1;
		int userNotExit = 2;
		
		String sql = "select * from users";
		
		try {
			PreparedStatement preStatement = connection.prepareStatement(sql);
			ResultSet result = preStatement.executeQuery();
			while(result.next()){
				if(result.getString(1).equals(name) ){
					if(result.getString(2).equals(passwd)){
						MainFrame.currentCluster = result.getString(4);
						return loginSucess;
					}else {
						return passwdError;//密码错误
					}	
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		return userNotExit;//用户不存在
	}
}
