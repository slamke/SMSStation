package com.unityprima.smsstattion.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Reader {
	private Reader ()
	{}
 	/**
	 * ��������ֵ
	 * 
	 * @param instream
	 * @return
	 */
	public static String read(InputStream instream) {
		StringBuilder sb = null;
		try {
			sb = new StringBuilder();
			BufferedReader r = new BufferedReader(new InputStreamReader(
					instream));
			for (String line = r.readLine(); line != null; line = r.readLine()) {
				sb.append(line);
			}
			instream.close();

		} catch (IOException e) {
		}
		return sb.toString();
	}
}
