package com.filescompressorgroup.files_compressor.filecompressor.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class ReadingFiles {

	public static String readFile(String filePath) throws IOException {
		StringBuilder content = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line).append("\n");
			}
		}
		return content.toString();
	}

	public static String readFileBinary(String filePath) throws IOException {
		try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filePath))) {
			int padding = inputStream.read(); // lê o byte de preenchimento
			StringBuilder content = new StringBuilder();
			int byteRead;
			while ((byteRead = inputStream.read()) != -1) {
				content.append(byteToBitString(byteRead));
			}
			// remove os bits extras, se houver
			if (padding > 0) {
				content.delete(content.length() - padding, content.length());
			}
			return content.toString();
		}
	}

	private static String byteToBitString(int b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 7; i >= 0; i--) {
			sb.append((b >> i) & 1);
		}
		return sb.toString();
	}
}
