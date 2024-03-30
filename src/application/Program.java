package application;

import java.io.IOException;

import entities.Node;
import util.FileEnconding;

public class Program {

	public static void main(String[] args) {
		String inputFilePath = "arquivo.txt";
		String encodedBinaryFilePath = "arquivo_codificado.bin";
		String decodedFilePath = "arquivo_decodificado.txt";

		try {
			// Codifica o arquivo de texto e obtém a raiz da árvore de Huffman
			Node root = FileEnconding.encodeFile(inputFilePath, encodedBinaryFilePath);

			// Decodifica o arquivo binário e transforma em texto
			FileEnconding.decodeFile(encodedBinaryFilePath, decodedFilePath, root);
		} catch (IOException e) {
			System.err.println("Erro ao processar o arquivo: " + e.getMessage());
		}
	}

}
