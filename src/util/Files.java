package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Files {

	public static Properties loadProperties(File file) throws IOException {
		Properties r = new Properties();
		FileInputStream fis = new FileInputStream(file);
		r.load(fis);
		return r;
	}

	public static Properties loadProperties(String file) throws IOException {
		return loadProperties(new File(file));
	}


}
