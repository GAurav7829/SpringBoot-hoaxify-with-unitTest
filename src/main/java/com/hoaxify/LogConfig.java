package com.hoaxify;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class LogConfig {
	
	private String className;
	public LogConfig(String className) {
		this.className = className;
	}

	public void logInfo(LogLevel level,String data) {
		try {
			File file = new File("D:\\test\\testFileMethod.log");
			if (!file.exists())
				file.createNewFile();
			FileWriter fileWriter = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fileWriter);
			
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(new Date());
			stringBuilder.append(" ");
			stringBuilder.append(level);
			stringBuilder.append(" : ");
			stringBuilder.append(className);
			stringBuilder.append(" : ");
			stringBuilder.append(data);
			stringBuilder.append("\n");
			
			bw.write(stringBuilder.toString());
			bw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}

enum LogLevel {
	INFO, WARNING
}
