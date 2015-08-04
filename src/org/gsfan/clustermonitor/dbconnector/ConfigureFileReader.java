package org.gsfan.clustermonitor.dbconnector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.JOptionPane;

public class ConfigureFileReader {
	
	private static Hashtable<String, String> confInfoTable = new Hashtable<String, String>(); 
	private String mysqlLocation = null;
	private File file = null;
	
	private static ConfigureFileReader instance = new ConfigureFileReader("./conf/configure.txt"); 
	
	private ConfigureFileReader(String fileName) {
		this.file = new File(fileName);
		parseConfingureFile();
	}
	
	public static ConfigureFileReader getInstance() {
		return instance;
	}
	
	public static Hashtable<String, String> getConfInfoTable() {
		return confInfoTable;
	}
	public String getMysqlLocation() {
		return mysqlLocation;
	}
	
	public Hashtable<String, String> parseConfingureFile() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(this.file));
            String lineStr = null;
            while ((lineStr = reader.readLine()) != null) {
            	int loc = lineStr.indexOf('=');
            	if(loc<0)
            		continue;
            	String key = lineStr.substring(0, loc-1);
            	key = key.trim();
            	String value = lineStr.substring(loc+1);
            	value = value.trim();
            	if(key==null || value==null) {
            		JOptionPane.showMessageDialog(null, "警告", "配置文件出错", JOptionPane.ERROR_MESSAGE);
            		return null;
            	}
            	
            	confInfoTable.put(key, value);
            }
            reader.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e1) {
                } 
            }
        }
        return confInfoTable;
    }
	
	public static void main(String argv[]) {
		ConfigureFileReader reader = new ConfigureFileReader("G:\\InnovationProject\\Source\\conf\\configure.txt");
		reader.parseConfingureFile();
	}
}
