package org.gsfan.clustermonitor.mainframe;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.gsfan.clustermonitor.dbconnector.ObtainUsersInformation;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;

@SuppressWarnings("serial")
public class LoginDialog extends JFrame implements ActionListener{
	
	private ComponentSizeAndLocation sizeAndLocation = ComponentSizeAndLocation.getInstance();
	private JLabel userLabel = new JLabel("用户");
	private JLabel passwdLabel = new JLabel("密码");
	
	private JTextField userText = new JTextField(20);
	private JPasswordField passwdText = new JPasswordField(20);
	
	private Font loginFont = new Font("微软雅黑", Font.PLAIN, 20);
	private Font checkFont = new Font("微软雅黑", Font.PLAIN, 14);//instantiation Font
	
	private JButton submitButton = new JButton();
	private JButton registerButton = new JButton();
	
	private JCheckBox savePasswd = new JCheckBox();
	private JCheckBox autoLogin = new JCheckBox();
	
	private String username = null;
	private String passwd = null;
	
	private Dimension labelDim = new Dimension(40, 25);
	private Dimension textDim = new Dimension(180, 25);
	private Dimension boxAndBtnDim = new Dimension(90, 30);
	private int xOrgStart = 70;
	private int yOrgStart = 40;
	
	public LoginDialog(){
		this.setLayout(null);	//set layout manager
		
		userLabel.setFont(loginFont);
		passwdLabel.setFont(loginFont);
		
		savePasswd.setFont(checkFont);
		savePasswd.setText("记住密码");
		autoLogin.setFont(checkFont);
		autoLogin.setText("自动登录");
		
		submitButton.setFont(loginFont);
		submitButton.setText("登录");
		registerButton.setFont(loginFont);
		registerButton.setText("注册");
		setAllComponentPosition();

		this.setTitle("Cluster Monitor");
		this.setResizable(false);
//		this.setBounds((ComponentSizeAndLocation.screenWidth-400)/2,(ComponentSizeAndLocation.screenHeight-300)/2,400,300); //set window's position and size,This method inherited from Window.class
		sizeAndLocation.setBounds(this, 400, 300);
				
		this.setVisible(true);//set window visable
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);//设置关闭窗口程序结束运行，否则会吃内存
		
	}
	
	private void setAllComponentPosition(){ //set position for all components
		int vSpace = 20;
		int xStart = xOrgStart;
		int yStart = yOrgStart;
		
		userLabel.setBounds(xStart, yStart, labelDim.width, labelDim.height);
		this.add(userLabel);
		userText.setBounds(xStart+labelDim.width+5, yStart, textDim.width, textDim.height);
		this.add(userText);
		
		yStart = yStart + textDim.height + vSpace;
		passwdLabel.setBounds(xStart, yStart, labelDim.width, labelDim.height);
		this.add(passwdLabel);
		passwdText.setBounds(xStart+labelDim.width+5, yStart, textDim.width, textDim.height);
		this.add(passwdText);
		
		yStart = yStart + textDim.height + vSpace;
		savePasswd.setBounds(xStart, yStart, boxAndBtnDim.width, boxAndBtnDim.height);
		this.add(savePasswd);
		autoLogin.setBounds(xStart+boxAndBtnDim.width+50, yStart, boxAndBtnDim.width, boxAndBtnDim.height);
		this.add(autoLogin);
		
		
		yStart = yStart + boxAndBtnDim.height + vSpace;
		submitButton.setBounds(xStart, yStart, boxAndBtnDim.width, boxAndBtnDim.height);
		this.add(submitButton);
		registerButton.setBounds(xStart+boxAndBtnDim.width+45, yStart, boxAndBtnDim.width, boxAndBtnDim.height);
		this.add(registerButton);
		
		savePasswd.addActionListener(this);	//add events
		autoLogin.addActionListener(this);	//add events
		submitButton.addActionListener(this);	//add events
		registerButton.addActionListener(this);	//add events
		
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

	private void loginEventHandle(){
		ObtainUsersInformation userInfo = new ObtainUsersInformation();
		this.username = userText.getText();
		this.passwd = new String(passwdText.getPassword());
		
		int res = userInfo.userAuthorization(this.username, this.passwd);
		if(0==res){
			new MainFrame();
			this.dispose();
		}else if(1==res){
			JOptionPane.showMessageDialog(null, "password error", "Warn", JOptionPane.ERROR_MESSAGE);
		}else {
			JOptionPane.showMessageDialog(null, "user not exits", "Warn", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void registerEventHandle(){	
		new RegisterDialog();
	}
	
	public void setLookAndFeel() {
		
//		PlasticXPLookAndFeel.setPlasticTheme(new DesertRed());
//		PlasticLookAndFeel.setTabStyle(PlasticLookAndFeel.TAB_STYLE_METAL_VALUE);
//		PlasticLookAndFeel.setTabStyle("Metal");
		
//		PlasticLookAndFeel.setHighContrastFocusColorsEnabled(true);
//		PlasticLookAndFeel.setPlasticTheme(new PlasticTheme(){});
//		Plastic3DLookAndFeel.setCurrentTheme(new ExperienceRoyale());
		try{

			UIManager.setLookAndFeel(new Plastic3DLookAndFeel());//using jgoodies look
//			UIManager.setLookAndFeel(new PlasticXPLookAndFeel());//using jgoodies look
//			UIManager.setLookAndFeel(new WindowsLookAndFeel());//using jgoodies look
//			UIManager.setLookAndFeel(new PlasticLookAndFeel());//using jgoodies look
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String argv[]){
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				LoginDialog login = new LoginDialog();
				login.setLookAndFeel();
				login.setVisible(true);
			}
		});
//		new LoginDialog();
	}
}