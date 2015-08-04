package org.gsfan.clustermonitor.mainframe;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.gsfan.clustermonitor.dbconnector.DatabaseStorage;

@SuppressWarnings("serial")
public class RegisterDialog extends JFrame implements ActionListener {
	
	//�������ô����С��λ��
	private ComponentSizeAndLocation sizeAndLocation = ComponentSizeAndLocation.getInstance();		
		
	private JPanel panel = null;
	private Font font = new Font("΢���ź�", Font.PLAIN, 18);
	
	private JTextField userText = new JTextField(20);
	private JPasswordField passwdText = new JPasswordField(20);
	private JPasswordField confirmPdText = new JPasswordField(20);
	private JTextField clusterText = new JTextField(20);
	
	private JLabel user = new JLabel("*��  ��  ��");
	private JLabel passwd = new JLabel("*��       ��");
	private JLabel confirm = new JLabel("*ȷ������");
	private JLabel cluster = new JLabel("*������Ⱥ");
	
	private JButton submitButton = new JButton();
	private JButton cancelButton = new JButton();
	
	private Dimension textDim = new Dimension(250, 25);
	private Dimension labelDim = new Dimension(85, 25);
	private int xStart = 50;
	private int yStart = 30;

	public RegisterDialog() {
		
		user.setFont(font);
		passwd.setFont(font);
		confirm.setFont(font);
		cluster.setFont(font);
		
		submitButton.setFont(font);
		cancelButton.setFont(font);
		
		panel = new JPanel();
		panel.setLayout(null);
		setAllComponentPosition();
		this.add(panel);
		
		this.setTitle("Register");
		int width = xStart + labelDim.width + textDim.width + xStart;
		int height = yStart + textDim.height + 60;
		sizeAndLocation.setBounds(this, width, height);
		yStart = 30;//the value of yStart changed after setAllComponentPosition()
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);//���ùرմ��ڳ���������У��������ڴ�
		//����������ע�� WindowListener �Ķ�����Զ����ز��ͷŸô��塣
	}
	
	private void setAllComponentPosition() {
		int interval = 15;
		user.setBounds(xStart, yStart, labelDim.width, labelDim.height);
		panel.add(user);
		userText.setBounds(xStart+labelDim.width, yStart, textDim.width, textDim.height);
		panel.add(userText);
		
		yStart = yStart + labelDim.height + interval;
		passwd.setBounds(xStart, yStart, labelDim.width, labelDim.height);
		panel.add(passwd);
		passwdText.setBounds(xStart+labelDim.width, yStart, textDim.width, textDim.height);
		panel.add(passwdText);
		
		yStart = yStart + labelDim.height + interval;
		confirm.setBounds(xStart, yStart, labelDim.width, labelDim.height);
		panel.add(confirm);
		confirmPdText.setBounds(xStart+labelDim.width, yStart, textDim.width, textDim.height);
		panel.add(confirmPdText);
		
		yStart = yStart + labelDim.height + interval;
		cluster.setBounds(xStart, yStart, labelDim.width, labelDim.height);
		panel.add(cluster);
		clusterText.setBounds(xStart+labelDim.width, yStart, textDim.width, textDim.height);
		panel.add(clusterText);
		
		yStart = yStart + labelDim.height + interval;
		submitButton.setText("ȷ��");
		submitButton.setBounds(xStart, yStart, labelDim.width, labelDim.height+5);
		panel.add(submitButton);
		submitButton.addActionListener(this);
		cancelButton.setBounds(xStart+textDim.width, yStart, labelDim.width, labelDim.height+5);
		cancelButton.setText("ȡ��");
		panel.add(cancelButton);
		cancelButton.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent event) {
		
		if(event.getSource()==submitButton) {
			this.submitEventHandle();
		} else if(event.getSource()==cancelButton) {
			this.dispose();	//�رմ���
		} else {
			
		}
	}
	
	private void submitEventHandle() {
		String username = userText.getText();
		
		String passwd = new String(passwdText.getPassword());
		String conPasswd = new String(confirmPdText.getPassword());
		
		String cluster = clusterText.getText();

		if(username.isEmpty() || passwd.isEmpty() || conPasswd.isEmpty() || cluster.isEmpty()) {
			JOptionPane.showMessageDialog(null, "������������Ϊ��", "Warn", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if(!passwd.equals(conPasswd)) {
			JOptionPane.showMessageDialog(null, "������������벻һ��", "Warn", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		//��ע����Ϣд�����ݿ�
		DatabaseStorage storage = new DatabaseStorage();
		int ret = storage.storeInfoToClusterTable(cluster, null);
		ret = storage.storeInfoToUserTable(username, passwd, "ordinary", cluster);	//Ĭ����ͨ�û�
		if(ret==1) {
			JOptionPane.showMessageDialog(null, "***��ӳɹ��� ��ϲ��***", "Prompt Message", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "***ע��ʧ�ܣ� ��ȷ������������Ϣ***", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}
	
	public static void main(String argv[]) {
		new RegisterDialog();
	}
}
