/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Jul 8, 2015
 */

package com.wehealth.model.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

public class StringConverter implements Converter {
	private String charset;

	public StringConverter() {
		this("UTF-8");
	}

	public StringConverter(String charset) {
		this.charset = charset;
	}

	@Override
	public Object fromBody(TypedInput body, Type type)
			throws ConversionException {
		String charset = this.charset;
		// String text = null;
		// try {
		// text = fromStream(typedInput.in());
		// } catch (IOException ignored) {/*NOP*/ }

		if (body.mimeType() != null) {
			charset = MimeUtil.parseCharset(body.mimeType(), charset);
		}
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(body.in(), charset);
			return fromStream(isr);
		} catch (IOException e) {
		
		} finally {
			if (isr != null)
				try {
					isr.close();
				} catch (IOException localIOException2) {
				}
		}

		return null;
	}

	@Override
	public TypedOutput toBody(Object o) {
		return null;
	}

	public static String fromStream(InputStreamReader in) throws IOException {
		BufferedReader reader = new BufferedReader(in);
		StringBuilder out = new StringBuilder();
		String newLine = System.getProperty("line.separator");
		String line;
		while ((line = reader.readLine()) != null) {
			out.append(line);
			out.append(newLine);
		}
		return out.toString();
	}
}
