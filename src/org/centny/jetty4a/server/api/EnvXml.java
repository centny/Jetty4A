package org.centny.jetty4a.server.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

import org.eclipse.jetty.util.IO;

/**
 * the class for convert the xml file string like $(*) to env value.<br/>
 * the value user order:<br/>
 * external properties,system properties,system environment.
 * 
 * @author Centny
 * 
 */
public class EnvXml {

	/**
	 * the external properties.
	 */
	private Properties extEnv;

	/**
	 * @return the extEnv
	 */
	public Properties getExtEnv() {
		return extEnv;
	}

	/**
	 * @param extEnv
	 *            the extEnv to set
	 */
	public void setExtEnv(Properties extEnv) {
		this.extEnv = extEnv;
	}

	/**
	 * the default constructor.
	 */
	public EnvXml() {
		this.extEnv = null;
	}

	/**
	 * the constructor by external properties.
	 * 
	 * @param ext
	 *            the external properties.
	 */
	public EnvXml(Properties ext) {
		this.extEnv = ext;
	}

	/**
	 * convert all string like $(*) to env value.
	 * 
	 * @param src
	 *            the source file
	 * @param dst
	 *            the target file to save.
	 */
	public void convert(File src, File dst) {
		if (!(src.exists() && dst.exists())) {
			return;
		}
		FileInputStream is = null;
		FileOutputStream os = null;
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			is = new FileInputStream(src);
			os = new FileOutputStream(dst);
			reader = new BufferedReader(new InputStreamReader(is));
			writer = new BufferedWriter(new OutputStreamWriter(os));
			String line = null;
			while ((line = reader.readLine()) != null) {
				line = EnvProperties.envVal(line, this.extEnv);
				writer.write(line + "\n");
			}
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IO.close(is);
			IO.close(os);
			IO.close(reader);
			IO.close(writer);
		}
	}
}
