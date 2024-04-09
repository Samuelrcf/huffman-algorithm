package com.filescompressorgroup.files_compressor.filecompressor.util;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class SavingFiles {

	public static void saveToFile(String content, String filePath) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			writer.write(content);
		}
	}

	public static void saveToFileBinary(String content, String filePath) throws IOException {
		try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath))) {
			int padding = 8 - (content.length() % 8); // calcula a quantidade de bits de preenchimento
			outputStream.write(padding); // coloca o byte de preenchimento no início do arquivo
			StringBuilder paddedContent = new StringBuilder(content);
			for (int i = 0; i < padding; i++) {
				paddedContent.append('0'); // adiciona zeros à direita para preenchimento
			}
			int i = 0;
			while (i + 8 <= paddedContent.length()) {
				int byteValue = Integer.parseInt(paddedContent.substring(i, i + 8), 2);
				outputStream.write(byteValue);
				i += 8;
			}
		}
	}
}
