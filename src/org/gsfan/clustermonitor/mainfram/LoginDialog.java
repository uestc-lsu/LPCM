package org.gsfan.clustermonitor.mainfram;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.gsfan.clustermonitor.dbconnector.ObtainUsersInformation;

@SuppressWarnings("serial")
public class LoginDialog extends JFrame implements ActionListener{
	
//	private JFrame loginWinFrame = new JFrame("Cluster Monitor");//Creat a JFrame for Login Window,specified title named Easy to use Cluster
	private JPanel loginPanel = new JPanel();
	
	private JLabel userLabel = new JLabel("用户");
	private JLabel passwdLabel = new JLabel("密码");
	private JLabel error = new JLabel();//用户名或密码错误提示
	
	private JTextField userText = new JTextField(20);
	private JPasswordField passwdText = new JPasswordField(20);
	
	private Font loginFont = new Font("宋体", Font.PLAIN, 20);//instantiation Font
	private Font check = new Font("宋体", Font.PLAIN, 14);//instantiation Font
	
	private JButton submitButton = new JButton();
	private JButton registerButton = new JButton();
	
	private JCheckBox savePasswd = new JCheckBox();
	private JCheckBox autoLogin = new JCheckBox();
	
	private String username = null;
	private String passwd = null;
	public LoginDialog(){
		this.setTitle("Cluster Monitor");
		loginPanel.setLayout(null);//set layout manager
		
		userLabel.setFont(loginFont);//设置字体
		passwdLabel.setFont(loginFont);//设置字体
		
		setAllComponentPosition();
		this.add(loginPanel);//将非顶层容器JPanel加到顶层容器JFrame之上
		
//		this.setUndecorated(true);//设置是否显示标题栏
		this.setTitle("Cluster Monitor");
		this.setResizable(false);
		this.setBounds(500,300,400,300); //set window's position and size,This method inherited from Window.class
		this.setVisible(true);//set window visable
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);//设置关闭窗口程序结束运行，否则会吃内存
	}
	
	private void setAllComponentPosition(){ //set position for all components
		userLabel.setBounds(70, 40, 40, 25);
		loginPanel.add(userLabel);
		userText.setBounds(120, 40, 180, 25);
		loginPanel.add(userText);
		
		passwdLabel.setBounds(70, 90, 40, 25);
		loginPanel.add(passwdLabel);
		passwdText.setBounds(120, 90, 180, 25);
		loginPanel.add(passwdText);
		
		//添加事件
		savePasswd.addActionListener(this);
		savePasswd.setFont(check);
		savePasswd.setText("记住密码");
		savePasswd.setBounds(70, 130, 90, 30);
		loginPanel.add(savePasswd);
		
		//添加事件
		savePasswd.addActionListener(this);
		autoLogin.setFont(check);
		autoLogin.setText("自动登录");
		autoLogin.setBounds(210, 130, 90, 30);
		loginPanel.add(autoLogin);
		
		//添加事件
		submitButton.addActionListener(this);
		submitButton.setBounds(70, 180, 90, 30);
		submitButton.setFont(loginFont);
		submitButton.setText("登陆");
		loginPanel.add(submitButton);
		
		//在此添加事件
		registerButton.addActionListener(this);
		registerButton.setBounds(210, 180, 90, 30);
		registerButton.setFont(loginFont);
		registerButton.setText("注册");
		loginPanel.add(registerButton);
		
//		设置背景色
//		Color bg = new Color(51, 153, 51);	// 006633
//		registerButton.setBorderPainted(false);
//		registerButton.setBackground(bg);
//		submitButton.setBorderPainted(false);
//		submitButton.setBackground(bg);
//		savePasswd.setBackground(bg);
//		autoLogin.setBackground(bg);
//		
//		loginPanel.setOpaque(true);
//		bg = new Color(0, 102, 51);	// 006633
//		loginPanel.setBackground(bg);
	}
	
	public void actionPerformed(ActionEvent event){

		if(event.getSource() == savePasswd){
			
		}else if(event.getSource() == autoLogin){
			
		}else if(event.getSource()==submitButton){
			this.loginEventHandle();
		}else if(event.getSource()==registerButton){
			this.registerEventHandle();
		}else {
			
		}

	}
	/**
	 * 
	 */
	private void loginEventHandle(){
		ObtainUsersInformation userInfo = new ObtainUsersInformation();
		this.username = userText.getText();
		this.passwd = new String(passwdText.getPassword());
		
		int res = userInfo.userAuthorization(this.username, this.passwd);
		if(0==res){
//			System.out.println("Login success");
			new MainFrame();
			this.dispose();
		}else if(1==res){
//			修改于2015.04.15
//			System.out.println("password error");
//			error.setText("密码错误，请保证您的密码输入正确!");
//			error.setFont(check);
//			Rectangle rect = submitButton.getBounds();
//			error.setBounds(rect.x, rect.y+rect.height+20, this.getWidth(), 20);
//			loginPanel.add(error);
//			this.repaint();
			WarnMsgDialog msgDialog = new WarnMsgDialog("password error",3);
			Thread thread = new Thread(msgDialog);
			thread.start();
		}else {
//			修改于2015.04.15
//			System.out.println("user not exits");
//			error.setText("用户"+userText.getText()+"不存在，请重新输入或注册新用户!");
//			error.setFont(check);
//			Rectangle rect = submitButton.getBounds();
//			error.setBounds(rect.x, rect.y+rect.height+20, this.getWidth(), 20);
//			loginPanel.add(error);
//			this.repaint();
			WarnMsgDialog msgDialog = new WarnMsgDialog("user not exits",3);
			Thread thread = new Thread(msgDialog);
			thread.start();
		}
	}
	private void registerEventHandle(){
		try {
			URI uri = new URI("http://www.baidu.com");
			Desktop.getDesktop().browse(uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String argv[]){
		new LoginDialog();
	}
}