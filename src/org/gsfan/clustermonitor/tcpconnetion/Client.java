package org.gsfan.clustermonitor.tcpconnetion;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class Client extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	Button button1,button2,button3,button4,button5,button6;
	TextArea text1,text2;
	int size,type;
	int count=0;
	JMenu jm;
	JMenuItem t_over,t;
	JMenuBar  br;
	private String [] outs;
	ServerThread thread1;
	
	public Client(){
		setLayout(null);
		size=80;
		type=Font.BOLD;		
		setTitle("Linux集群状态监测器");
		setSize(630,450);
		setLocationRelativeTo(null);
		setVisible(true);
		
	    jm=new JMenu("查看") ;     //创建JMenu菜单对象
	    br=new  JMenuBar() ;  //创建菜单工具栏
	    t_over=new JMenuItem("退出") ;  //菜单项
		text1=new TextArea("",50,50);
		text2=new TextArea("",50,50);
		button1=new Button("系统");
		button2=new Button("资源");
		button3=new Button("分区");
		button4=new Button("网络");
		button5=new Button("进程");
		button6=new Button("用户");
		text1.setBounds(05,70,600,250);
		text2.setBounds(05,320,600,50);
		button1.setBounds(05, 20, 70, 30);
		button2.setBounds(85, 20, 70, 30);
		button3.setBounds(165, 20, 70, 30);
		button4.setBounds(245, 20, 70, 30);
		button5.setBounds(325, 20, 70, 30);
		button6.setBounds(405, 20, 70, 30);
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		button4.addActionListener(this);
		button5.addActionListener(this);
		button6.addActionListener(this);
		Font font1=new Font("Roman",Font.BOLD,18);
		Font font2=new Font("Times",Font.BOLD,18);
		text1.setFont(font1);
		text2.setFont(font1);
		button1.setFont(font2);
		button2.setFont(font2);
		button3.setFont(font2);
		button4.setFont(font2);
		button5.setFont(font2);
		button6.setFont(font2);
	    jm.add(t_over) ;   //将菜单项目添加到菜单
	    br.add(jm) ;      //将菜单增加到菜单工具栏
	    this.setJMenuBar(br) ;  //为 窗体设置  菜单工具栏
		add(text1);
		add(text2);
		add(button1);
		add(button2);
		add(button3);
		add(button4);
		add(button5);
		add(button6);
		t_over.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
				 System.exit(0);
			}
			   
		});
		
	      try{
	          text2.setText("等待连接");
	          @SuppressWarnings("resource")
	   	      ServerSocket sk = new ServerSocket(4005);
	          while(true){
	        	 Socket connectToClient=sk.accept();
	             text2.append("\nlinux机组"+(++count)+":"+connectToClient.getInetAddress()+"建立连接");
	             t=new JMenuItem("linux_"+(count)) ;//菜单项
	             jm.add(t) ;    //将菜单项目添加到菜单
	             thread1=new ServerThread(connectToClient);
	     		 t.addActionListener(new ActionListener(){
	   			 public void actionPerformed(ActionEvent e) {
	   				 thread1.start();
	   			}	   			   
	   		});
	          }
	         }catch (Exception e) 
	         {
	            e.printStackTrace();
	         }
		
	}
	
	public class ServerThread extends Thread
	{
	   Socket server;
	   public ServerThread(Socket socket){
		   server=socket;
	   }

	   public void run()
	   {
	      try
	      {
		   ObjectInputStream ip=new ObjectInputStream(server.getInputStream());
	       outs = (String[])ip.readObject();
	         
	      }
	      catch (Exception e)
	      {
	         e.printStackTrace();
	      }
	   }
	}

	public void actionPerformed(ActionEvent e) {

		if(e.getSource()==button1)
		{
			//String[] names = ((String)outs[0]).split("\n");
			text1.setText("linux的系统信息：\n\n"+"------------------------------------"
					+ "-----------------------------------\n\n"
			+outs[0]);
		}

		else if(e.getSource()==button2)
		{
			//String[] names = ((String)outs[1]).split("\n");
			text1.setText("linux的资源信息：\n\n"+"--------------------------------------"
					+ "---------------------------------\n\n"
			+outs[1]);
		}

    		else if(e.getSource()==button3)
    		{
    			//String[] names = ((String)outs[2]).split("\n");
    			text1.setText("linux的分区信息：\n\n"+"---------------------------------"
    					+ "--------------------------------------\n\n"
    			+outs[2]);
    		}

    		else if(e.getSource()==button4)
    		{
    			//String[] names = ((String)outs[3]).split("\n");
    			text1.setText("linux的网络信息：\n\n"+"-----------------------------------"
    					+ "------------------------------------\n\n"
    			+outs[3]);
    		}

    		else if(e.getSource()==button5)
    		{
    			//String[] names = ((String)outs[4]).split("\n");
    			text1.setText("linux的进程信息：\n\n"+"-------------------------------------"
    					+ "----------------------------------\n\n"
    			+outs[4]);
    		}

    		else if(e.getSource()==button6)
    		{
    			//String[] names = ((String)outs[5]).split("\n");
    			text1.setText("linux的用户信息：\n\n"+"--------------------------------------"
    					+ "---------------------------------\n\n"
    			+outs[5]);
    		}
	
	}
	
	public  static  void  main(String  args[]){
		new  Client();

    }
}