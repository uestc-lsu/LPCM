package org.gsfan.clustermonitor.dbconnector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ConfigureFileReader {
	
	private String mysqlLocation = null;
	private File file = null;

	public ConfigureFileReader(String fileName) {
		this.file = new File(fileName);
	}
	
	public String getMysqlLocation() {
		return mysqlLocation;
	}
	
	public void readMysqlLocFromConfFile() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(this.file));
            String lineStr = null;
            while ((lineStr = reader.readLine()) != null) {
            	if(!lineStr.startsWith("mysqllocation:port")) {
            		continue;
            	} else {
            		int loc = lineStr.indexOf('=');
            		String temp = lineStr.substring(loc+1);
            		mysqlLocation = temp.trim();
//            		System.out.println("mysqlLocation:" + mysqlLocation+" end");
            		break;
            	}
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
	
	public static void main(String argv[]) {
		ConfigureFileReader reader = new ConfigureFileReader("G:\\InnovationProject\\Source\\conf\\configure.txt");
		reader.readMysqlLocFromConfFile();
	}
}
